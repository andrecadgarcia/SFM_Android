package com.andrecadgarcia.sfm.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrecadgarcia.sfm.R;
import com.andrecadgarcia.sfm.activity.MainActivity;
import com.andrecadgarcia.sfm.fragment.ExampleMultiviewSceneReconstruction;
import com.andrecadgarcia.sfm.fragment.ModelViewerFragment;

import org.rajawali3d.lights.ALight;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

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

                    ExecuteSFM sfm = new ExecuteSFM();
                    sfm.execute();
                }
                else {
                    ModelViewerFragment viewer = (ModelViewerFragment)((MainActivity) context).getClass(MainActivity.MODELVIEWER_FRAGMENT);
                    viewer.setModel(Environment.getExternalStorageDirectory() + File.separator + "SFM" +
                            File.separator + "Media" + File.separator + "Models" + File.separator + ((TextView) v.findViewById(R.id.tv_folder_title)).getText());
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
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class ExecuteSFM extends AsyncTask<String, Void, String> {

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
                    .create();
            alert.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            ExampleMultiviewSceneReconstruction example = new ExampleMultiviewSceneReconstruction();
            IntrinsicParameters intrinsic = new IntrinsicParameters(325.55,325.55,1,125.55,125.55,260,320);
            return example.process(intrinsic, pictures, context);

        }

        @Override
        protected void onPostExecute(String result) {

            alert.dismiss();
            after = System.currentTimeMillis();
            System.out.println("Elapsed time " + (after - before) / 1000.0 + " (s)");
            FileOutputStream outStream = null;
            OutputStreamWriter myOutWriter = null;
            try {
                String path = Environment.getExternalStorageDirectory() +
                        File.separator + "SFM";
                File dir = new File(path);
                if(!dir.exists()){
                    dir.mkdir();
                    Log.d("Log", "onDirCreated");
                }
                path += File.separator + "Media";
                dir = new File(path);
                if(!dir.exists()){
                    dir.mkdir();
                    Log.d("Log", "onDirCreated");
                }
                path += File.separator + "Models";
                dir = new File(path);
                if(!dir.exists()){
                    dir.mkdir();
                    Log.d("Log", "onDirCreated");
                }
                path += File.separator + String.valueOf(System.currentTimeMillis()) + ".obj";

                dir = new File(path);
                outStream = new FileOutputStream(dir);
                myOutWriter = new OutputStreamWriter(outStream);
                myOutWriter.append(result);
                myOutWriter.close();

                outStream.flush();
                outStream.close();
                Log.d("Log", "onPictureTaken - wrote bytes: " + result.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }


            ((MainActivity) context).setProcessingSFM(false);
            ((MainActivity) context).setResult(result);
            ((MainActivity) context).fragmentTransaction(MainActivity.SFMRESULT_FRAGMENT);

        }
    }
}
