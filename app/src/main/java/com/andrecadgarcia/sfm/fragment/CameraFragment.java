package com.andrecadgarcia.sfm.fragment;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.andrecadgarcia.sfm.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Andre Garcia on 01/10/16.
 */

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    private final String tag = "Camera";

    private View rootview;

    private Camera mCamera = null;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private Camera.PictureCallback rawCallback;
    private Camera.ShutterCallback shutterCallback;
    private Camera.PictureCallback pngCallback;

    private ImageButton capture;

    private String group_name;
    private int pictures_taken;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        group_name = String.valueOf(System.currentTimeMillis());
        pictures_taken = 0;

        if(rootview == null) {

            rootview = inflater.inflate(R.layout.fragment_camera, container, false);

            mSurfaceView = (SurfaceView) rootview.findViewById(R.id.sv_camera_preview);
            capture = (ImageButton) rootview.findViewById(R.id.bt_shutter);

            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            rawCallback = new Camera.PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    Log.d("Log", "onPictureTaken - raw");
                }
            };

            shutterCallback = new Camera.ShutterCallback() {
                public void onShutter() {
                    Log.i("Log", "onShutter'd");
                }
            };

            pngCallback = new Camera.PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    FileOutputStream outStream = null;
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
                        path += File.separator + "Pictures";
                        dir = new File(path);
                        if(!dir.exists()){
                            dir.mkdir();
                            Log.d("Log", "onDirCreated");
                        }
                        path += File.separator + group_name;
                        dir = new File(path);
                        if(!dir.exists()){
                            dir.mkdir();
                            Log.d("Log", "onDirCreated");
                        }
                        path += File.separator + pictures_taken + ".png";
                        dir = new File(path);
                        outStream = new FileOutputStream(dir);
                        pictures_taken++;
                        outStream.write(data);
                        outStream.close();
                        Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }
                    Log.d("Log", "onPictureTaken - jpeg");
                }
            };

            capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                }
            });

        }


        return rootview;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop_camera();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void captureImage() {
        // TODO Auto-generated method stub
        mCamera.takePicture(shutterCallback, rawCallback, pngCallback);
    }

    private void start_camera() {
        try{
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            mCamera.setDisplayOrientation(90);
        }catch(RuntimeException e){
            Log.e(tag, "failed to open Camera " + e);
            return;
        }
        Camera.Parameters param;
        param = mCamera.getParameters();
        //modify parameter
        param.setPreviewFrameRate(20);
        param.setPreviewSize(176, 144);
        mCamera.setParameters(param);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            //camera.takePicture(shutter, raw, jpeg)
        } catch (Exception e) {
            Log.e(tag, "failed to preview Camera: " + e);
            return;
        }
    }

    private void stop_camera() {
        mCamera.stopPreview();
        mCamera.release();
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    public void surfaceCreated(SurfaceHolder holder) {
        start_camera();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

}