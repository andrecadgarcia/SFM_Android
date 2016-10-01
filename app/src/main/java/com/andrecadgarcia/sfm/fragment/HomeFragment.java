package com.andrecadgarcia.sfm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andrecadgarcia.sfm.R;
import com.andrecadgarcia.sfm.activity.MainActivity;

/**
 * Created by Andre Garcia on 01/10/16.
 */

public class HomeFragment extends Fragment {

    private View rootview;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootview == null) {

            rootview = inflater.inflate(R.layout.fragment_home, container, false);

            ((Button) rootview.findViewById(R.id.bt_camera)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getContext()).fragmentTransaction(MainActivity.CAMERA_FRAGMENT);
                }
            });
            ((Button) rootview.findViewById(R.id.bt_calibration)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getContext()).fragmentTransaction(MainActivity.CALIBRATION_FRAGMENT);
                }
            });
            ((Button) rootview.findViewById(R.id.bt_gallery)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getContext()).fragmentTransaction(MainActivity.GALERRY_FRAGMENT);
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
    public void onResume() {
        super.onResume();
    }
}
