package com.andrecadgarcia.sfm;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.io.File;

/**
 * Created by Andre Garcia on 04/10/16.
 */

public class Renderer extends RajawaliRenderer {

    Context context;

    private DirectionalLight directionalLight;
    private Object3D object;

    public Renderer(Context context, String path) {
        super(context);
        this.context = context;
        setFrameRate(60);
        object = getObject(path);
    }

    @Override
    protected void initScene() {
        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);
        getCurrentScene().addLight(directionalLight);

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());

        object.setMaterial(material);
        object.setColor(Color.BLUE);

        getCurrentScene().addChild(object);

        getCurrentCamera().setLookAt(object.getLookAt());

        getCurrentScene().setBackgroundColor(Color.rgb(255,255,255));
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        object.rotate(Vector3.Axis.Y, 1.0);
        //getCurrentCamera().rotate(Vector3.Axis.X, 0.5);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    /*
    public void rotCamera(float touchOffset) {
        xOffset = touchOffset;
        if(xOffset > 360) xOffset -= 360;

        float x = (float) (5*Math.sin(Math.toRadians(xOffset)));
        float z = (float) (5*Math.cos(Math.toRadians(xOffset)));

        mCamera.setPosition(x, 0, z);
    }
*/
    public void clean() {
        getCurrentScene().clearChildren();
    }

    public Object3D getObject(String path) {
        LoaderOBJ parser = null;
        try {
            File object = new File(path);

            parser = new LoaderOBJ(this, object);

            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }

        return parser.getParsedObject();
    }

    public void addObject(String path) {
        LoaderOBJ parser = null;
        try {
            File object = new File(path);

            parser = new LoaderOBJ(this, object);

            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());

        object = parser.getParsedObject();

        object.setMaterial(material);
        object.setColor(Color.BLUE);

        getCurrentScene().addChild(object);

        getCurrentCamera().setLookAt(object.getLookAt());

    }
}
