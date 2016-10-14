package com.andrecadgarcia.sfm.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrecadgarcia.sfm.model3D.Feature3D;
import com.andrecadgarcia.sfm.R;
import com.andrecadgarcia.sfm.activity.MainActivity;
import com.andrecadgarcia.sfm.model3D.StructureFromMotion;
import com.andrecadgarcia.sfm.fragment.ModelViewerFragment;

import org.rajawali3d.renderer.RajawaliRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import boofcv.struct.calib.IntrinsicParameters;


/**
 * Created by Andre Garcia on 03/10/16.
 */

public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder> {

    private List<String> mDataset;
    private Context context;

    List<String> pictures;

    private boolean showingPNG = true;

    private ExecuteSFM sfm;

    StructureFromMotion example;
    IntrinsicParameters intrinsic;

    RajawaliRenderer renderer;

    private static final String ASSETS_TARGET_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator
            + "SFM" + File.separator + "Media" + File.separator + "Models" + File.separator;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mTextView = ((TextView) v.findViewById(R.id.tv_folder_title));
            mImageView = ((ImageView) v.findViewById(R.id.iv_gallery));
        }
    }

    public GalleryRecyclerAdapter(List<String>  myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }


    @Override
    public GalleryRecyclerAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(showingPNG) {
                    File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "SFM" +
                        File.separator + "Media" + File.separator + "Pictures" + File.separator + ((TextView) v.findViewById(R.id.tv_folder_title)).getText());
                    File[] dirFiles = dir.listFiles();
                    pictures = new ArrayList<>();
                    for (File folder : dirFiles) {
                        System.out.println(folder.getAbsolutePath());
                        pictures.add(folder.getAbsolutePath());
                    }

                    intrinsic = ((MainActivity) context).preference.intrinsic;
                    if (intrinsic == null) {
                        new AlertDialog.Builder(context)
                                .setTitle("Camera Parameters Error")
                                .setMessage("Please calibrate camera")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .create().show();
                    }
                    else {
                        sfm = new ExecuteSFM();
                        sfm.execute();
                    }

                }
                else {
                    ModelViewerFragment viewer = (ModelViewerFragment)((MainActivity) context).getClass(MainActivity.MODELVIEWER_FRAGMENT);
                    viewer.setFolder(((TextView) v.findViewById(R.id.tv_folder_title)).getText() + "");
                    viewer.setModel("sequential.obj");
;                   ((MainActivity) context).fragmentTransaction(MainActivity.MODELVIEWER_FRAGMENT);
                }
            }
        });

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setPNGViwer(boolean pngViwer) {
        this.showingPNG = pngViwer;
    }

    @Override
    public void onBindViewHolder(GalleryRecyclerAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position));

        String path = Environment.getExternalStorageDirectory() + File.separator + "SFM" +
                File.separator + "Media" + File.separator;

        if (this.showingPNG) {
            path += "Pictures" + File.separator + mDataset.get(position) + File.separator + "0.png";
        }
        else {
            path += "Models" + File.separator + mDataset.get(position) + File.separator + ".png";
        }

        Bitmap ThumbImage;

        ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path),
                64, 64);

        if (ThumbImage == null) {
            ThumbImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.model_default);
        }

        holder.mImageView.setImageBitmap(ThumbImage);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class ExecuteSFM extends AsyncTask<String, Void, List<Feature3D>> {

        long before, after;
        AlertDialog alert;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            before = System.currentTimeMillis();
            ((MainActivity) context).setProcessingSFM(true);
            alert = new AlertDialog.Builder(context)
                    .setTitle("SFM Reconstruction")
                    .setMessage("This may take a while.")
                    .setCancelable(false)
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            sfm.cancel(true);
                        }
                    })
                    .create();
            alert.show();

        }

        @Override
        protected List<Feature3D> doInBackground(String... urls) {

            example = new StructureFromMotion();
            if (!isCancelled()) {
                return example.process(intrinsic, pictures, context);
            }
            else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Feature3D> result) {

            if (result != null) {
                after = System.currentTimeMillis();

                System.out.println("Elapsed time " + (after - before) / 1000.0 + " (s)");
                Log.d("Log", "onPictureTaken - wrote bytes: " + result.size());

                String points = "";

                for (Feature3D p : result) {
                    points += "v " + p.worldPt.x + " " + p.worldPt.y + " " + p.worldPt.z + "\n";
                }

                createObjs(result, points);

                alert.dismiss();

                ((MainActivity) context).setProcessingSFM(false);
                ((MainActivity) context).setResult(points);
                ((MainActivity) context).fragmentTransaction(MainActivity.SFMRESULT_FRAGMENT);

            }
        }
    }


    public void createObjs(List<Feature3D> cloud, String vertices) {

        FileOutputStream outStream = null;
        OutputStreamWriter myOutWriter = null;

        try {
            String basePath = Environment.getExternalStorageDirectory() +
                    File.separator + "SFM";
            File dir = new File(basePath);
            if(!dir.exists()){
                dir.mkdir();
                Log.d("Log", "onDirCreated");
            }
            basePath += File.separator + "Media";
            dir = new File(basePath);
            if(!dir.exists()){
                dir.mkdir();
                Log.d("Log", "onDirCreated");
            }
            basePath += File.separator + "Models";
            dir = new File(basePath);
            if(!dir.exists()){
                dir.mkdir();
                Log.d("Log", "onDirCreated");
            }
            basePath += File.separator + String.valueOf(System.currentTimeMillis());
            dir = new File(basePath);
            if(!dir.exists()){
                dir.mkdir();
                Log.d("Log", "onDirCreated");
            }

            System.out.printf("Point cloud size: " + cloud.size());
            System.out.println("Getting point cloud");
            String points = getPoints(cloud, vertices);
            System.out.println("Getting sequential");
            String sequential = getSequential(cloud, vertices);
            String allToAll = getAllToAll(cloud, vertices);

            String filePath;

            System.out.println("Saving");

            filePath = basePath + File.separator + "points.obj";
            dir = new File(filePath);
            outStream = new FileOutputStream(dir);
            myOutWriter = new OutputStreamWriter(outStream);
            myOutWriter.append(points);
            myOutWriter.close();

            filePath = basePath + File.separator + "sequential.obj";
            dir = new File(filePath);
            outStream = new FileOutputStream(dir);
            myOutWriter = new OutputStreamWriter(outStream);
            myOutWriter.append(sequential);
            myOutWriter.close();


            filePath = basePath + File.separator + "allToAll.obj";
            dir = new File(filePath);
            outStream = new FileOutputStream(dir);
            myOutWriter = new OutputStreamWriter(outStream);
            myOutWriter.append(allToAll);
            myOutWriter.close();


            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public String getPoints(List<Feature3D> cloud, String vertices) {
        String result = "";

        for (Feature3D point : cloud) {
            for (int i = 0; i < 8; i++) {
                result += "v ";
                result += (point.worldPt.x + (((i % 2) == 0) ? 0.05 : -0.05)) + " " + (point.worldPt.y + (((i > 1) && (i < 5)) ? 0.05 : -0.05)) + " " + (point.worldPt.z + ((i < 4) ? 0.05 : -0.05));
                result += '\n';
            }
        }

        int index = -1;
        for (Feature3D point : cloud) {
            index++;
            int start = (index * 8) + 1;

            result += "f ";
            result += (start + 0) + " " + (start + 1) + " " + (start + 2);
            result += '\n';
            result += "f ";
            result += (start + 2) + " " + (start + 1) + " " + (start + 3);
            result += '\n';
            result += "f ";
            result += (start + 2) + " " + (start + 3) + " " + (start + 4);
            result += '\n';
            result += "f ";
            result += (start + 4) + " " + (start + 3) + " " + (start + 5);
            result += '\n';
            result += "f ";
            result += (start + 4) + " " + (start + 5) + " " + (start + 6);
            result += '\n';
            result += "f ";
            result += (start + 6) + " " + (start + 5) + " " + (start + 7);
            result += '\n';
            result += "f ";
            result += (start + 6) + " " + (start + 7) + " " + (start + 0);
            result += '\n';
            result += "f ";
            result += (start + 0) + " " + (start + 7) + " " + (start + 1);
            result += '\n';
            result += "f ";
            result += (start + 1) + " " + (start + 7) + " " + (start + 3);
            result += '\n';
            result += "f ";
            result += (start + 3) + " " + (start + 7) + " " + (start + 5);
            result += '\n';
            result += "f ";
            result += (start + 6) + " " + (start + 0) + " " + (start + 4);
            result += '\n';
            result += "f ";
            result += (start + 4) + " " + (start + 0) + " " + (start + 2);
            result += '\n';
        }


        return result;
    }

    public String getSequential(List<Feature3D> cloud, String vertices) {
        String result = vertices;
        result += "\n";

        for(int i = 0 ; i <= cloud.size(); i++) {
            result += "f ";
            result += ((i % (cloud.size())) + 1) + " " + (((i + 1) % (cloud.size())) + 1) + " " + (((i + 2) % (cloud.size())) + 1);
            result += '\n';
            result += "f ";
            result += (((i + 2)  % (cloud.size())) + 1) + " " + (((i + 1) % (cloud.size())) + 1) + " " + ((i % (cloud.size())) + 1);
            result += '\n';
        }



        return result;
    }

    public String getAllToAll(List<Feature3D> cloud, String vertices) {
        String result = vertices;
        result += "\n";

        for (int i = 1; i <= cloud.size(); i++) {
            for (int j = 1; j <= cloud.size(); j++) {
                result += "f ";
                result += 1 + " " + i + " " + j;
                result += '\n';
                result += "f ";
                result += j + " " + i + " " + 1;
                result += '\n';
            }
        }


        return result;
    }
}
