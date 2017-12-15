package com.popland.pop.facecontroldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hai on 12/12/2017.
 */

public class GraphicOverlay extends View {
Object Lock = new Object();
int previewW, previewH;
static float widthScaleFactor = 1.0f;
static float heightScaleFactor = 1.0f;
Set<Graphic> mGraphics = new HashSet<>();

    public static abstract class Graphic{
        GraphicOverlay gOverlay;

        Graphic(GraphicOverlay go){
            gOverlay = go;
        }

        public float scaleX(float horizontal){
            return horizontal * widthScaleFactor;
        }

        public float scaleY(float vertical){
            return vertical * heightScaleFactor;
        }

        public float translateX(float x){
            return scaleX(x);//camera facing front
        }

        public float translateY(float y){
            return scaleY(y);
        }

        public abstract void draw(Canvas c);

        public void postInvalidate(){
            gOverlay.postInvalidate();
        }
    }

    public void clear(){
        synchronized (Lock) {
            mGraphics.clear();
        }
        postInvalidate();
    }

    public void add(FaceGraphic fg){
        synchronized (Lock) {
            mGraphics.add(fg);
        }
        postInvalidate();
    }

    public void remove(FaceGraphic fg){
        synchronized (Lock) {
            mGraphics.remove(fg);
        }
        postInvalidate();
    }

    public GraphicOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCameraInfo(int width, int height){
        synchronized (Lock) {
            previewW = width;
            previewH = height;
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (Lock) {
            Paint p = new Paint();
            p.setColor(Color.MAGENTA);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(5);
            canvas.drawLine(canvas.getWidth()/2,0,canvas.getWidth()/2,canvas.getHeight(),p);
            if (previewW != 0 && previewH != 0) {
                widthScaleFactor = (float) canvas.getWidth() / (float) previewW;
                heightScaleFactor = (float) canvas.getHeight() / (float) previewH;
            }
            for (Graphic graphic : mGraphics)
                graphic.draw(canvas);
        }
    }
}
