package com.cartago.sfm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import boofcv.struct.calib.IntrinsicParameters;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
