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

    private static final String CURRENT_FRAGMENT = "current_fragment_index_flag";

    private HashMap<Integer, Fragment> hashFragment;
    private int currentFragmentIndex;

    private boolean doubleBackToExitPressedOnce;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        hashFragment = new HashMap<>();

        hashFragment.put(HOME_FRAGMENT, new HomeFragment());
        hashFragment.put(CAMERA_FRAGMENT, new CameraFragment());
        hashFragment.put(CALIBRATION_FRAGMENT, new CalibrationFragment());
        hashFragment.put(GALERRY_FRAGMENT, new GalleryFragment());

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

        /*
        List<Integer> images = new ArrayList<>();

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

        images.add(R.drawable.dino1);
        images.add(R.drawable.dino2);
        images.add(R.drawable.dino3);
        images.add(R.drawable.dino4);
        images.add(R.drawable.dino5);
        images.add(R.drawable.dino6);
        images.add(R.drawable.dino7);
        images.add(R.drawable.dino8);
        images.add(R.drawable.dino9);
        images.add(R.drawable.dino10);
        images.add(R.drawable.dino11);
        images.add(R.drawable.dino12);
        images.add(R.drawable.dino13);
        images.add(R.drawable.dino14);
        images.add(R.drawable.dino15);
        images.add(R.drawable.dino16);

        ExampleMultiviewSceneReconstruction example = new ExampleMultiviewSceneReconstruction();

        IntrinsicParameters intrinsic = new IntrinsicParameters(325.55,325.55,1,125.55,125.55,260,320);

        long before = System.currentTimeMillis();
        example.process(intrinsic, images, this);
        long after = System.currentTimeMillis();

        System.out.println("Elapsed time " + (after - before) / 1000.0 + " (s)");
        */
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
            default:
                toolbar.setTitle(getString(R.string.app_name));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
