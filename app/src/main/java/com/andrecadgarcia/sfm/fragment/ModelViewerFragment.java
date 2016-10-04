package com.andrecadgarcia.sfm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrecadgarcia.sfm.R;
import com.andrecadgarcia.sfm.Renderer;
import com.andrecadgarcia.sfm.activity.MainActivity;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

/**
 * Created by Andre Garcia on 04/10/16.
 */

public class ModelViewerFragment extends Fragment {

    private View rootview;

    private RajawaliSurfaceView surface;
    private Renderer renderer;

    private String path;

    public ModelViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootview == null) {

            rootview = inflater.inflate(R.layout.fragment_model_viewer, container, false);

            surface = (RajawaliSurfaceView) rootview.findViewById(R.id.rajwali_surface);
            renderer = new Renderer(getContext(), this.path);
            surface.setSurfaceRenderer(renderer);

        }
        else {
            surface.invalidate();
        }

        return rootview;
    }

    public void setModel(String path) {
        this.path = path;
        if(renderer != null) {
            renderer.clean();
            renderer.addObject(this.path);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
