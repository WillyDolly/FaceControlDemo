package com.popland.pop.facecontroldemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Movement{
CameraSourcePreview csPreview;
GraphicOverlay gOverlay;
CameraSource mCameraSource;
ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        csPreview = (CameraSourcePreview)findViewById(R.id.csPreview);
        gOverlay = (GraphicOverlay)findViewById(R.id.gOverlay);
        iv = (ImageView)findViewById(R.id.imageView);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
            createCameraSource();
        else
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100) {
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                createCameraSource();
        }
    }

    public void createCameraSource(){
        FaceDetector faceDetector = new FaceDetector.Builder(this)
                .setProminentFaceOnly(true)
                .build();
        faceDetector.setProcessor(new LargestFaceFocusingProcessor.Builder(faceDetector,new FaceTracker(gOverlay)).build());

        if(!faceDetector.isOperational())
            Toast.makeText(MainActivity.this,"not operational",Toast.LENGTH_SHORT).show();
        mCameraSource = new CameraSource.Builder(this,faceDetector)
                .setRequestedPreviewSize(640,480)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCameraSource!=null){
            mCameraSource.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCameraSource!=null){
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    public void startCameraSource(){
            try {
                if(mCameraSource!=null)
                    csPreview.start(mCameraSource, gOverlay);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
    }

    @Override
    public void left() {

    }

    @Override
    public void right() {

    }

    public class FaceTracker extends Tracker<Face>{
        GraphicOverlay graphicOverlay;
        FaceGraphic faceGraphic;

        FaceTracker(GraphicOverlay go){
            graphicOverlay = go;
            faceGraphic = new FaceGraphic(go);
        }

        @Override
        public void onNewItem(int i, Face face) {
            super.onNewItem(i, face);
        }

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face) {//continuously update
            super.onUpdate(detections, face);
            graphicOverlay.add(faceGraphic);
            faceGraphic.update(face);
            Log.i("FFF","update");

           // Log.i("size",csPreview.getWidth()+"-"+mCameraSource.getPreviewSize().getWidth());
//            float midXFace = face.getPosition().x + face.getWidth()/2;
//            float widthScale = (float)csPreview.getWidth() / (float) mCameraSource.getPreviewSize().getWidth();
//            float toX = midXFace * widthScale;
//            float X = 50;
//            TranslateAnimation animation = new TranslateAnimation((iv.getLeft()+iv.getRight())/2,iv.getLeft(),iv.getTop(),iv.getTop());
//            animation.setDuration(1000);
//            animation.setFillAfter(true);
//            iv.startAnimation(animation);
        }

        @Override
        public void onMissing(Detector.Detections<Face> detections) {
            super.onMissing(detections);
            graphicOverlay.remove(faceGraphic);
            Log.i("FFF","missing");
        }

        @Override
        public void onDone() {
            super.onDone();
            graphicOverlay.remove(faceGraphic);
            Log.i("FFF","done");
        }
    }
}
