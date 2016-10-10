package com.andrecadgarcia.sfm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.andrecadgarcia.sfm.R;
import com.andrecadgarcia.sfm.Renderer;

import org.rajawali3d.surface.RajawaliSurfaceView;

/**
 * Created by Andre Garcia on 04/10/16.
 */

public class ModelViewerFragment extends Fragment {

    private View rootview;

    private RajawaliSurfaceView surface;
    private Renderer renderer;

    private SeekBar x,y,z;
    private CheckBox rotation;
    private Spinner objs;

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

            x = (SeekBar) rootview.findViewById(R.id.sk_x);
            x.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    System.out.println(renderer.getCurrentCamera().getPosition());
                    renderer.getCurrentCamera().setX(i-25);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            y = (SeekBar) rootview.findViewById(R.id.sk_y);
            y.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    renderer.getCurrentCamera().setY(i-50);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            z = (SeekBar) rootview.findViewById(R.id.sk_z);
            z.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    renderer.getCurrentCamera().setZ(i-40);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            rotation = (CheckBox) rootview.findViewById(R.id.cb_rot);
            rotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    renderer.setRotation(b);
                }
            });

            objs = (Spinner) rootview.findViewById(R.id.sp_objs);
            objs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:

                             break;
                        case 1:

                            break;
                        case 2:

                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
        else {
            surface.invalidate();
        }

        x.setProgress(50);
        y.setProgress(50);
        z.setProgress(50);
        rotation.setChecked(true);

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
