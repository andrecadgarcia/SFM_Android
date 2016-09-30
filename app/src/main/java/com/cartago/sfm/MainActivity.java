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

        List<Bitmap> images = new ArrayList<>();

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap dino1 = BitmapFactory.decodeResource(getResources(), R.drawable.dino1, opt);
        Bitmap dino2 = BitmapFactory.decodeResource(getResources(), R.drawable.dino2, opt);
        //Bitmap dino3 = BitmapFactory.decodeResource(getResources(), R.drawable.dino3, opt);
        //Bitmap dino4 = BitmapFactory.decodeResource(getResources(), R.drawable.dino4, opt);
        //Bitmap dino5 = BitmapFactory.decodeResource(getResources(), R.drawable.dino5, opt);
        //Bitmap dino6 = BitmapFactory.decodeResource(getResources(), R.drawable.dino6, opt);
        //Bitmap dino7 = BitmapFactory.decodeResource(getResources(), R.drawable.dino7, opt);
        //Bitmap dino8 = BitmapFactory.decodeResource(getResources(), R.drawable.dino8, opt);
        //Bitmap dino9 = BitmapFactory.decodeResource(getResources(), R.drawable.dino9, opt);
        //Bitmap dino10 = BitmapFactory.decodeResource(getResources(), R.drawable.dino10, opt);
        //Bitmap dino11 = BitmapFactory.decodeResource(getResources(), R.drawable.dino11, opt);
        //Bitmap dino12 = BitmapFactory.decodeResource(getResources(), R.drawable.dino12, opt);
        //Bitmap dino13 = BitmapFactory.decodeResource(getResources(), R.drawable.dino13, opt);
        //Bitmap dino14 = BitmapFactory.decodeResource(getResources(), R.drawable.dino14, opt);
        //Bitmap dino15 = BitmapFactory.decodeResource(getResources(), R.drawable.dino15, opt);
        //Bitmap dino16 = BitmapFactory.decodeResource(getResources(), R.drawable.dino16, opt);

        images.add(dino1);
        images.add(dino2);
        //images.add(dino3);
        //images.add(dino4);
        //images.add(dino5);
        //images.add(dino6);
        //images.add(dino7);
        //images.add(dino8);
        //images.add(dino9);
        //images.add(dino10);
        //images.add(dino11);
        //images.add(dino12);
        //images.add(dino13);
        //images.add(dino14);
        //images.add(dino15);
        //images.add(dino16);

        ExampleMultiviewSceneReconstruction example = new ExampleMultiviewSceneReconstruction();

        IntrinsicParameters intrinsic = new IntrinsicParameters(325.55,325.55,1,125.55,125.55,260,320);

        long before = System.currentTimeMillis();
        example.process(intrinsic, images);
        long after = System.currentTimeMillis();

        System.out.println("Elapsed time " + (after - before) / 1000.0 + " (s)");
    }
}
