package com.popland.pop.facecontroldemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.view.View;

import com.google.android.gms.vision.face.Face;

/**
 * Created by hai on 12/12/2017.
 */

public class FaceGraphic extends GraphicOverlay.Graphic {
Paint boxPaint, textPaint;
volatile Face mFace;//volatile to tell compiler it's an unusual variable in multi-threading program
Bitmap character;
int xC, yC;

    public FaceGraphic(GraphicOverlay go) {
        super(go);
        boxPaint = new Paint();
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStrokeWidth(5.0f);

        textPaint = new TextPaint();
        textPaint.setColor(Color.CYAN);
        textPaint.setTextSize(100);

        character = BitmapFactory.decodeResource(Resources.getSystem(),R.mipmap.ic_launcher);

        xC = 720;
        yC = 1200;
    }

    public void update(Face face){
        mFace = face;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        if(mFace==null)
            return;
//        float x = translateX(mFace.getPosition().x);
//        float y = translateY(mFace.getPosition().y);
//        canvas.drawCircle(x,y,20,boxPaint);
//        float xPreview = mFace.getPosition().x;
//        float yPreview = mFace.getPosition().y;
//        Paint p = new Paint();
//        p.setColor(Color.RED);
//        p.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(xPreview,yPreview,20,p);
//
//        float boxW = scaleX(mFace.getWidth());
//        float boxH = scaleY(mFace.getHeight());
//        canvas.drawRect(x, y ,x+boxW, y+boxH,boxPaint);

//        RectF rectF = new RectF(mFace.getPosition().x,mFace.getPosition().y,mFace.getPosition().x +mFace.getWidth(),mFace.getPosition().y +mFace.getHeight());
//        Matrix matrix = new Matrix();
//        matrix.setScale(1,1);
//        matrix.postScale(canvas.getWidth()/2000f,canvas.getHeight()/2000f);
//        matrix.postTranslate(canvas.getWidth()/2f,canvas.getHeight()/2f);
//        matrix.mapRect(rectF);
//        canvas.drawRect(rectF,p);

        //EulerZ indicate head's orientation with face opposite to camera
        float EulerZ = mFace.getEulerZ();
        float EulerY = mFace.getEulerY();
//        canvas.drawText(""+EulerZ,canvas.getWidth()/2,100,textPaint);
//        if(EulerZ>10) {//fine-tune sensitivity
//            canvas.drawText("RIGHT", canvas.getWidth() - 100, 100, textPaint);
//            canvas.drawRect(canvas.getWidth()-100,canvas.getHeight()-100,canvas.getWidth(),canvas.getHeight(),boxPaint);
//        }
//        else if(EulerZ<-10) {
//            canvas.drawText("LEFT", 100, 100, textPaint);
//            canvas.drawRect(0,canvas.getHeight()-100,100,canvas.getHeight(),boxPaint);
//        }else{
//            canvas.drawRect(canvas.getWidth()/2 -50,canvas.getHeight()-100,canvas.getWidth()/2 +50,canvas.getHeight(),boxPaint);
//        }

        if((EulerY<0 && EulerZ<0) || (EulerY>0 && EulerZ>0)){//head up
            yC += 1;
            canvas.drawBitmap(character,xC,yC,null);
        }
        //if((EulerY>0 && EulerZ<0) || (EulerY<0 && EulerZ>0))// head down
    }
}
