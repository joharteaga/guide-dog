package com.cs4351.guidedogtest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {
    private Camera mCamera;
    private CameraPreview mPreview;

    private Button returnMenu;
    private Button demo;
    private ToggleButton lightNav;

    private Boolean hasLight;
    private Boolean lightOn;
    private int demoCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //setup buttons
        returnMenu = (Button) findViewById(R.id.navigationReturn);
        demo = (Button) findViewById(R.id.demoButton);
        lightNav = (ToggleButton) findViewById(R.id.lightButtonNav);


        //start button click listeners
        returnMenu.setOnClickListener(this);
        lightNav.setOnClickListener(this);
        demo.setOnClickListener(this);

        //determine phone has if light available
        hasLight = NavigationActivity.this.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH);

        if (hasLight) {
            lightOn = false;

        //if no flashlight detected on camera, inform user
        } else {
            Toast toast = Toast.makeText(NavigationActivity.this, "NO LIGHT DETECTED. "
                    + "LIGHT FEATURE DISABLED.", Toast.LENGTH_LONG);

            //set toast message to display in center of screen (horiz/vert)
            toast.setGravity(Gravity.CENTER, 0, 0);

            //display toast
            toast.show();
        }

        mCamera = getCameraInstance();

        // create CameraPreview
        mPreview = new CameraPreview(NavigationActivity.this, mCamera);

        //start preview in FrameLayout
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_previewNav);
        preview.addView(mPreview);
    }

    //get camera instance safely; from Google Tutorial
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

            //if return to main menu button pressed
            case R.id.navigationReturn: {
                //stop camera instance and release
                mCamera.stopPreview();
                mCamera.release();

                Intent intent = new Intent(NavigationActivity.this, MainMenu.class);

                //clear back history
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                //start main activity and close current activity
                startActivity(intent);
                finish();
                break;
            }
            //if light togglebutton pressed
            case R.id.lightButtonNav: {
                //check bool to see if camera has a light and if the light is currently off
                if (hasLight && !lightOn) {
                    Camera.Parameters cameraParams = mCamera.getParameters();

                    //get flashlight parameter and set parameter to 'TORCH' mode (constant light)
                    cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(cameraParams);

                    //set bool to indicate light is currently on
                    lightOn = true;

                //check bool to see if camera has a light and if the light is currently off
                } else if (hasLight && lightOn) {
                    //get camera params and set mode to light off
                    Camera.Parameters cameraParams = mCamera.getParameters();
                    cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(cameraParams);

                    //set bool to indicate flashlight is turned off
                    lightOn = false;
                }
                break;
            }
            //if demo button pressed
            case R.id.demoButton: {

                if (demoCount == 0) {
                    //display 10meter object message on first demo button press
                    Toast demoToast = Toast.makeText(NavigationActivity.this, "OBJECT DETECTED:"
                            + " 10 METERS", Toast.LENGTH_LONG);
                    demoToast.getView().setBackgroundColor(Color.RED);  //for visibility
                    demoToast.setGravity(Gravity.CENTER, 0, 0);     //center message
                    demoToast.show();       //display message

                    //create vibration instance
                    Vibrator rumble = (Vibrator) this.getApplicationContext().getSystemService(
                            Context.VIBRATOR_SERVICE);

                    //vibration pattern: short vibration burst long pause
                    final long[] pattern = { 0, 200, 500, 200, 500, 200 };
                    rumble.vibrate(pattern , -1);       //-1 indicates pattern not to repeat

                    //increase demo button count
                    demoCount++;

                //if demo button pressed a second time display 5 meter message
                } else if (demoCount == 1) {
                    Toast demoToast = Toast.makeText(NavigationActivity.this, "OBJECT DETECTED:"
                            + " 5 METERS", Toast.LENGTH_LONG);
                    demoToast.getView().setBackgroundColor(Color.RED);  //visibility
                    demoToast.setGravity(Gravity.CENTER, 0, 0);     //center
                    demoToast.show();       //display


                    Vibrator rumble = (Vibrator) this.getApplicationContext().getSystemService(
                            Context.VIBRATOR_SERVICE);

                    //vibration pattern medium length burst with medium pause
                    final long[] pattern = { 0, 400, 300, 400, 300, 400 };
                    rumble.vibrate(pattern , -1);   //does not repeat

                    demoCount++;

                } else if (demoCount == 2) {
                    Toast demoToast = Toast.makeText(NavigationActivity.this, "!!CAUTION!! OBJECT"
                            + " DETECTED: 2 METERS", Toast.LENGTH_LONG);
                    demoToast.getView().setBackgroundColor(Color.RED);
                    demoToast.setGravity(Gravity.CENTER, 0, 0);
                    demoToast.show();

                    Vibrator rumble = (Vibrator) this.getApplicationContext().getSystemService(
                            Context.VIBRATOR_SERVICE);
                    final long[] pattern = { 0, 1000, 100, 1000, 100, 1000 };
                    rumble.vibrate(pattern , -1);
                    demoCount = 0;
                }
                break;
            }
        }
    }
}
