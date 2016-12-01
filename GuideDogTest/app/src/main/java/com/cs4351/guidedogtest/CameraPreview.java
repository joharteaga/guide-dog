package com.cs4351.guidedogtest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfHolder;
    private Camera mCamera;
    private float touchDist;

    public CameraPreview(Context context, Camera camera) {
        super(context);

        //check if camera enabled
        if(checkCameraHardware(context))  {
            mCamera = camera;
        }

        surfHolder = getHolder();
        surfHolder.addCallback(this);

        surfHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //rotage camera preview by 90 degrees to show in portrait mode
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceCreated(SurfaceHolder newHolder) {
        try {
            //start camerapreview
            mCamera.setPreviewDisplay(newHolder);
            mCamera.startPreview();
        } catch (IOException e) {
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder newHolder, int format, int width, int height) {
        if (surfHolder.getSurface() == null){
            return;
        }

        //stop current pewview
        try {
            mCamera.stopPreview();
        } catch (Exception e){

        }

        //update preview with changes
        try {
            mCamera.setPreviewDisplay(surfHolder);
            mCamera.startPreview();
        } catch (Exception e){
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //handles pinch to zoon functions
    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {
        //get camera params
        Camera.Parameters cameraParams = mCamera.getParameters();
        int action = touchEvent.getAction();


        if (touchEvent.getPointerCount() > 1) {
            // if two touches moving apart
            if (action == MotionEvent.ACTION_POINTER_DOWN) {
                //determine finger spacing
                touchDist = getTouchDist(touchEvent);

            } else if (action == MotionEvent.ACTION_MOVE && cameraParams.isZoomSupported()) {

                //stop current auto focus and begin zoom
                mCamera.cancelAutoFocus();
                zoom(touchEvent, cameraParams);
            }
        } else {

            // if only one touch (one finger)
            if (action == MotionEvent.ACTION_UP) {
                //focus camera
                focusCamera(touchEvent, cameraParams);
            }
        }
        return true;
    }

    private void zoom(MotionEvent newTouchEvent, Camera.Parameters newParams) {
        //get zoom limits
        int maxMagnify = newParams.getMaxZoom();
        int magnification = newParams.getZoom();

        //get new touch distance
        float distance = getTouchDist(newTouchEvent);

        //if distance is greater (fingers spread) then zoom in
        if (distance > touchDist) {
            if (magnification < maxMagnify)
                magnification++;
        //if distance is less (fingers came together) then zoom out
        } else if (distance < touchDist) {
            if (magnification > 0)
                magnification--;
        }

        touchDist = distance;
        newParams.setZoom(magnification);
        mCamera.setParameters(newParams);
    }

    public void focusCamera(MotionEvent event, Camera.Parameters newParams) {
        int pointer = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointer);

        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> focusModes = newParams.getSupportedFocusModes();
        if (focusModes != null && focusModes.contains(
                Camera.Parameters.FOCUS_MODE_AUTO)) {
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean b, Camera camera) {
                        }
         });
        }
    }

    private float getTouchDist(MotionEvent event) {
        //get distance between finger press
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float)Math.sqrt(x * x + y * y);
    }

    //check if camera available; Google Android tutorial
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }
}
