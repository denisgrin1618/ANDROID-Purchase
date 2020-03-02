package com.account.sunstones.sunstones_purchase;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

public class FingerPath {

    public class Point{
        public float x;
        public float y;
        public Point(){}
    }

    public int strokeWidth;
    public Path path;
    public ArrayList<Point> path_points = new ArrayList();
    public Point pointTouch;
    public Paint paint;

    public FingerPath(Paint paint, int strokeWidth) {
        this.strokeWidth    = strokeWidth;
        this.paint          = paint;
    }

    public FingerPath() {

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setMaskFilter(null);
        mPaint.setColor(Color.RED);
//        mPaint.setAlpha(125); //прозрачность
        mPaint.setTextSize(20);
        mPaint.setStrokeWidth(PaintView.BRUSH_SIZE);

        this.strokeWidth    = PaintView.BRUSH_SIZE;
        this.paint          = mPaint;
    }

    public void setPath(Path path){
        this.path = path;
    }

    public void addPathPoint(float x, float y){
        Point p = new Point();
        p.x = x;
        p.y = y;
        path_points.add(p);
    }

    public void setPointTouch(float x, float y){
        pointTouch = new Point();
        pointTouch.x = x;
        pointTouch.y = y;
    }



}
