package com.andrecadgarcia.sfm.fragment;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.andrecadgarcia.sfm.R;
import com.andrecadgarcia.sfm.adapter.GalleryRecyclerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre Garcia on 01/10/16.
 */

public class GalleryFragment extends Fragment {

    private View rootview;

    private RecyclerView rv_cardList;

    private GalleryRecyclerAdapter galleryAdapter;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootview == null) {

            rootview = inflater.inflate(R.layout.fragment_gallery, container, false);

            rv_cardList = (RecyclerView) rootview.findViewById(R.id.cardList);
        }

        try {
            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "SFM" +
                    File.separator + "Media" + File.separator + "Pictures");
            File[] dirFiles = dir.listFiles();
            List<String> folders = new ArrayList<>();
            for (File folder : dirFiles) {
                System.out.println(folder.getName());
                folders.add(folder.getName());
            }

            galleryAdapter = new GalleryRecyclerAdapter(folders);
            galleryAdapter.notifyDataSetChanged();


            GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
            rv_cardList.setLayoutManager(glm);
            rv_cardList.setAdapter(galleryAdapter);
        } catch(Exception e) {
            Log.d("GALLERY","NO FILES");
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
