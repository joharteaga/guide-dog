package com.cs4351.guidedogtest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ReaderActivity extends AppCompatActivity implements View.OnClickListener {
    private CameraPreview mPreview;
    private Camera mCamera;

    private Button readerReturnMenu;
    private Button ocr;
    private ToggleButton light;

    private Boolean hasLight;
    private Boolean lightOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        //setup buttons
        readerReturnMenu = (Button) findViewById(R.id.readerReturn);
        light = (ToggleButton) findViewById(R.id.lightButton);
        ocr = (Button) findViewById(R.id.ocrButton);

        //determine if light available
        hasLight = ReaderActivity.this.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH);

        if (hasLight) {
            lightOn = false;

        } else {
            Toast toast = Toast.makeText(ReaderActivity.this, "NO LIGHT DETECTED. "
                    + "LIGHT FEATURE DISABLED.", Toast.LENGTH_LONG);

            //set toast message to display in center of screen (horiz/vert)
            toast.setGravity(Gravity.CENTER, 0, 0);

            //display toast
            toast.show();

        }

        // create CameraPreview and start view in framelayout
        mPreview = new CameraPreview(ReaderActivity.this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        //if return to main menu button pressed
        readerReturnMenu.setOnClickListener(this);
        light.setOnClickListener(this);
        ocr.setOnClickListener(this);
    }

    //safely get camera instance; per Google Tutorial
    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    //button press detected
    @Override
    public void onClick(View v) {
        //get id of button pressed
        switch (v.getId()) {

            //if main menu button pressed
            case R.id.readerReturn: {
                //stop camera preview and release
                mCamera.stopPreview();
                mCamera.release();

                //setup fragment transaction
                Intent intent = new Intent(ReaderActivity.this, MainMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                //start main menu activity and stop current activity
                startActivity(intent);
                finish();
                break;
            }
            //if light togglebutton pressed
            case R.id.lightButton: {

                //check if camera has light AND light is currently off
                if (hasLight && !lightOn) {
                    Camera.Parameters cameraParams = mCamera.getParameters();

                    //set camera light mode to constantly on
                    cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(cameraParams);

                    //indicate light is on
                    lightOn = true;

                //check if camera has light AND light is currently on
                } else if (hasLight && lightOn) {
                    Camera.Parameters cameraParams = mCamera.getParameters();

                    //set camera light to off
                    cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(cameraParams);

                    lightOn = false;
                }
                break;
            }
        }
    }
}
