package com.andrecadgarcia.sfm.fragment;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.andrecadgarcia.sfm.R;

/**
 * Created by Andre Garcia on 01/10/16.
 */

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    private final String tag = "Camera";

    private View rootview;

    Camera mCamera = null;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    Camera.PictureCallback rawCallback;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootview == null) {

            rootview = inflater.inflate(R.layout.fragment_camera, container, false);

            mSurfaceView = (SurfaceView) rootview.findViewById(R.id.sv_camera_preview);
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            rawCallback = new Camera.PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    Log.d("Log", "onPictureTaken - raw");
                }
            };

        }


        return rootview;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void captureImage() {
        // TODO Auto-generated method stub
        //mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    private void start_camera()
    {
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

    private void stop_camera()
    {
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
