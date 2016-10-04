package com.andrecadgarcia.sfm.activity;

import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.andrecadgarcia.sfm.R;
import com.andrecadgarcia.sfm.fragment.CalibrationFragment;
import com.andrecadgarcia.sfm.fragment.CameraFragment;
import com.andrecadgarcia.sfm.fragment.GalleryFragment;
import com.andrecadgarcia.sfm.fragment.HomeFragment;
import com.andrecadgarcia.sfm.fragment.ModelViewerFragment;
import com.andrecadgarcia.sfm.fragment.SFMResultFragment;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Andre Garcia on 01/10/16.
 */


public class MainActivity extends AppCompatActivity {

    public static final Integer HOME_FRAGMENT = 0;
    public static final Integer CAMERA_FRAGMENT = 1;
    public static final Integer CALIBRATION_FRAGMENT = 2;
    public static final Integer GALERRY_FRAGMENT = 3;
    public static final Integer SFMRESULT_FRAGMENT = 4;
    public static final Integer MODELVIEWER_FRAGMENT = 5;

    private static final String CURRENT_FRAGMENT = "current_fragment_index_flag";

    private HashMap<Integer, Fragment> hashFragment;
    private int currentFragmentIndex;

    private boolean doubleBackToExitPressedOnce, processingSFM;

    private Toolbar toolbar;

    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        hashFragment = new HashMap<>();

        hashFragment.put(HOME_FRAGMENT, new HomeFragment());
        hashFragment.put(CAMERA_FRAGMENT, new CameraFragment());
        hashFragment.put(CALIBRATION_FRAGMENT, new CalibrationFragment());
        hashFragment.put(GALERRY_FRAGMENT, new GalleryFragment());
        hashFragment.put(SFMRESULT_FRAGMENT, new SFMResultFragment());
        hashFragment.put(MODELVIEWER_FRAGMENT, new ModelViewerFragment());

        if(savedInstanceState != null){
            currentFragmentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT);
            fragmentTransaction(currentFragmentIndex);
        } else {
            fragmentTransaction(HOME_FRAGMENT);
        }

        Locale locale = new Locale("pt","BR");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        doubleBackToExitPressedOnce = false;
        processingSFM = false;

    }

    public Fragment getClass(Integer name) {
        return hashFragment.get(name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CURRENT_FRAGMENT, currentFragmentIndex);

    }

    public void fragmentTransaction(int id) {

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, hashFragment.get(id))
                    .commit();
            currentFragmentIndex = id;


            setToolBarTitle(id);


        } catch (Exception e) {
            Toast.makeText(this, "Em Desenvolvimento... :D", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (currentFragmentIndex == HOME_FRAGMENT) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Precione voltar mais uma vez para sair", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        else if (currentFragmentIndex == SFMRESULT_FRAGMENT || currentFragmentIndex == MODELVIEWER_FRAGMENT ) {
            if (!processingSFM) {
                fragmentTransaction(GALERRY_FRAGMENT);
            }
        }
        else{
            fragmentTransaction(HOME_FRAGMENT);
        }
    }

    private void setToolBarTitle(int id){
        id++;
        switch (id){
            case 1:
                toolbar.setTitle(getString(R.string.menu_item_title1));
                break;
            case 2:
                toolbar.setTitle(getString(R.string.menu_item_title2));
                break;
            case 3:
                toolbar.setTitle(getString(R.string.menu_item_title3));
                break;
            case 4:
                toolbar.setTitle(getString(R.string.menu_item_title4));
                break;
            case 5:
                toolbar.setTitle(getString(R.string.menu_item_title4));
                break;
            case 6:
                toolbar.setTitle(getString(R.string.menu_item_title4));
                break;
            default:
                toolbar.setTitle(getString(R.string.app_name));
        }
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setProcessingSFM(boolean processingSFM) {
        this.processingSFM = processingSFM;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
