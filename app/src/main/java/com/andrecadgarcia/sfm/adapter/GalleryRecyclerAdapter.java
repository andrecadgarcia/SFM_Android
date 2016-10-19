package com.andrecadgarcia.sfm.adapter;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrecadgarcia.sfm.async.ExecuteSFM;
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

    IntrinsicParameters intrinsic;

    ProgressDialog progress;
    GalleryRecyclerAdapter class_adapter = this;
    int steps = 0;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;

    long before, after;
    long before_sfm, after_sfm;
    long before_points, after_points;
    long before_sequential, after_sequential;
    long before_alltoall, after_alltoall;

    double elapsed = 0;

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

    public void setProgress(int progress) {
        this.progress.setProgress(progress);
        // Displays the progress bar for the first time.
        mBuilder.setProgress(100, progress, false);
        mNotifyManager.notify(id, mBuilder.build());
    }

    public void setMessages(String message) {
        this.progress.setMessage(message);
        mBuilder.setContentTitle(message);
        mNotifyManager.notify(id, mBuilder.build());
    }

    public void dismissAlert() {
        this.progress.dismiss();
        mBuilder.setProgress(0, 0, false);
        mBuilder.setContentTitle("Reconstruction Complete");

        after_alltoall = System.currentTimeMillis();
        after = System.currentTimeMillis();
        elapsed = (after - before) / 1000.0;
        System.out.println("Elapsed time " + (after - before) / 1000.0 + " (s)");
    }

    public void addStep() {

        this.steps++;

        switch (this.steps) {
            case 1:
                before_sfm = System.currentTimeMillis();
                break;
            case 2:
                after_sfm = System.currentTimeMillis();
                before_points = System.currentTimeMillis();
                break;
            case 3:
                after_points = System.currentTimeMillis();
                before_sequential = System.currentTimeMillis();
                break;
            case 4:
                after_sequential = System.currentTimeMillis();
                before_alltoall = System.currentTimeMillis();
                break;
        }
    }

    public int getSteps() {
        return this.steps;
    }

    public String getElapsed() {
        return  "\nSFM Reconstruction: " + (after_sfm - before_sfm) / 1000.0 + " s\n" +
                "Point Cloud: " + (after_points - before_points) / 1000.0 + " s\n" +
                "Sequential: " + (after_sequential - before_sequential) / 1000.0 + " s\n" +
                "All to All: " + (after_alltoall - before_alltoall) / 1000.0 + " s\n\n" +
                "Elapsed Time: " + (after - before) / 1000.0 + " s\n\n";

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
                        ((MainActivity) context).setProcessingSFM(true);

                        progress = new ProgressDialog(context);
                        progress.setMessage("");
                        progress.setProgress(0);
                        progress.setIndeterminate(false);
                        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progress.setMax(100);
                        progress.show();

                        before = System.currentTimeMillis();

                        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mBuilder = new NotificationCompat.Builder(context);
                        mBuilder.setSmallIcon(R.drawable.shutter);
                        // Displays the progress bar for the first time.
                        mBuilder.setProgress(100, 0, false);
                        mNotifyManager.notify(id, mBuilder.build());

                        sfm = new ExecuteSFM(intrinsic, pictures, context, class_adapter);
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
}
