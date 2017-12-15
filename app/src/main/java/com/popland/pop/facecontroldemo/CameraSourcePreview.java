package com.popland.pop.facecontroldemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

/**
 * Created by hai on 11/12/2017.
 */

public class CameraSourcePreview extends ViewGroup {
Boolean startRequest, surfaceAvailable;
SurfaceView surfaceView;
CameraSource mCameraSource;
GraphicOverlay gOverlay;

    public CameraSourcePreview(Context c, AttributeSet attrs){
        super(c,attrs);
        startRequest = false;
        surfaceAvailable = false;
        surfaceView = new SurfaceView(c);
        surfaceView.getHolder().addCallback(SurfaceCallback);
        addView(surfaceView);
    }

    public void start(CameraSource cs, GraphicOverlay go) throws IOException{
        gOverlay = go;
        if(mCameraSource==null)
            stop();
        mCameraSource = cs;
        if(mCameraSource!=null)
            startRequest = true;
    }

    public void stop(){
        if(mCameraSource!=null){
            mCameraSource.stop();
        }
    }

    public void startIfReady() throws IOException{
        if(startRequest && surfaceAvailable){
            mCameraSource.start(surfaceView.getHolder());
            if(gOverlay!=null){
                Size size = mCameraSource.getPreviewSize();
                    int min = Math.min(size.getWidth(), size.getHeight());
                    int max = Math.max(size.getWidth(), size.getHeight());
                    gOverlay.setCameraInfo(min, max);
                gOverlay.clear();
            }
            startRequest = false;
        }
    }

    SurfaceHolder.Callback SurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            surfaceAvailable = true;
            try {
                startIfReady();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            surfaceAvailable = false;
        }
    };

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int layoutW = i2-i;
        int layoutH = i3-i1;
        for(int j=0;j<getChildCount();j++){
            getChildAt(j).layout(0,0,layoutW,layoutH);
        }
    }
}
