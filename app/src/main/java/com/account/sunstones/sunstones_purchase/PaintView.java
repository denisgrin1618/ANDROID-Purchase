package com.account.sunstones.sunstones_purchase;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaintView extends View {

    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastPencilX;
    private float mLastPencilY;
    private float mPosX;
    private float mPosY;
    private Bitmap bitmap;
    private Rect rect;
    private float cX, cY; // circle coords
    private float mLastPosX = 0;
    private float mLastPosY = 0;
    private Boolean isOutOfBorders = false;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    private float lastScaleFactor = 1.0f;
    private float minScaleFactor;
    private float scalePointX;
    private float scalePointY;
    private float lastScalePointX;
    private float lastScalePointY;


    private Path mPath;
    private FingerPath mFingerPath;
    private Paint mPaint;
//    private ArrayList<FingerPath> paths = new ArrayList<>();

    long startTimeTouch;
    private int currentMod;
    public static int BRUSH_SIZE = 20;
    private static final float TOUCH_TOLERANCE = 4;
    public static final int MOD_TOUCH = 55;
    public static final int MOD_PANCIL = 50;
    public static final int MOD_ZOOM = 22;


    private int mLastVendorCode;

    private Photo mPhotoItemData;

    public int display_width;
    public int display_height;

    int mTextSize = 50;
    int mToolbarHeight = 0;

    public PaintView(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache(true);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        init();

    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache(true);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        init();
    }

    void init(){
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic2);


        currentMod = MOD_ZOOM;

        mPaint = new Paint();
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
        mPaint.setStrokeWidth(BRUSH_SIZE);

//        mPaint.setAlpha(0xff);




    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        setDrawingCacheEnabled(true); // cache


        Paint p = new Paint();
        p.setColor(Color.RED);
//        p.setAlpha(125);
        p.setTextSize(mTextSize);

//        rect = canvas.getClipBounds();


        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor, scalePointX, scalePointY);

        ///////////////////////////////////////////
        //
        float posBorderLeft     = (0 - scalePointX)/ mScaleFactor + scalePointX;
        float posBorderTop      = (0 - scalePointY)/ mScaleFactor + scalePointY;
        float posBorderRight    = (getWidth()  - bitmap.getWidth()*mScaleFactor  - scalePointX) /mScaleFactor + scalePointX;
        float posBorderBottom   = (getHeight() - bitmap.getHeight()*mScaleFactor - scalePointY) /mScaleFactor + scalePointY - mToolbarHeight/mScaleFactor;

//        Log.v("TEST_2", "mPosY:"+mPosY + " posBorderTop:"+posBorderTop + " posBorderBottom:"+ posBorderBottom );
        Log.v("TEST_2", "mToolbarHeight:"+mToolbarHeight );
        mPosX = Math.min(Math.max(mPosX,posBorderRight), posBorderLeft);
        mPosY = Math.min(Math.max(mPosY,posBorderBottom), posBorderTop);

        ///////////////////////////////////////////

        canvas.translate(mPosX, mPosY);

        //запретим выход за границы картинки
        rect = canvas.getClipBounds();


        canvas.drawARGB(255, 125, 125, 125);
        canvas.drawBitmap(bitmap, 0, 0, null);

        for (Photo.Photo_products pp : mPhotoItemData.products) {
            canvas.drawCircle(pp.product.x, pp.product.y, 10, p);

            String text = Integer.toString(pp.product.vendor_code);
            float width_text = p.measureText(text);
            float X = (bitmap.getWidth() < pp.product.x + width_text ?   bitmap.getWidth() - width_text : pp.product.x);
            float Y = pp.product.y;
            canvas.drawText(text, X, Y, p);
        }

        for (FingerPath fp : mPhotoItemData.paths) {
            if(fp.path != null){
                canvas.drawPath(fp.path, fp.paint);
            }
        }



        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();

        float _x = ev.getX();
        float _y = ev.getY();

        switch(action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                touchStart(_x,_y);
                invalidate();
                this.postDelayed(contin, 500);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                touchMove(_x,_y);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                touchUp(_x,_y);
                invalidate();
                break;
            }



        }
        return true;
    }

    private void touchStart(float _x, float _y) {

        final float x = (_x - scalePointX)/mScaleFactor;
        final float y = (_y - scalePointY)/mScaleFactor;
        cX = x - mPosX + scalePointX; // canvas X
        cY = y - mPosY + scalePointY; // canvas Y


        startTimeTouch = System.currentTimeMillis();

        /////////////////////////////////////////
        if(currentMod == MOD_PANCIL) {
            mPath = new Path();
            mFingerPath = new FingerPath(mPaint, BRUSH_SIZE);
            mFingerPath.setPath(mPath);
//            paths.add(mFingerPath);
            mPath.reset();
            mPath.moveTo(cX, cY);
            mFingerPath.addPathPoint(cX, cY);

            mPhotoItemData.addFingerPath(mFingerPath);
            mPhotoItemData.photo_sync = false;
            mLastPencilX = cX;
            mLastPencilY = cY;
        }
        ///////////////////////////////////////////////////////



        mLastTouchX = x;
        mLastTouchY = y;
    }
    private void touchMove(float _x, float _y) {

        final float x = (_x - scalePointX)/mScaleFactor;
        final float y = (_y - scalePointY)/mScaleFactor;

        ////////////////////////////////////////
        if(currentMod == MOD_PANCIL) {
            float dx = Math.abs(x - mLastTouchX);
            float dy = Math.abs(y - mLastTouchY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//                mPath.quadTo(mLastTouchX, mLastTouchY, (x + mLastTouchX) / 2, (y + mLastTouchY) / 2);

                float X1 = mLastPencilX;
                float Y1 = mLastPencilY;
                mLastPencilX = x - mPosX + scalePointX;
                mLastPencilY = y - mPosY + scalePointY;
                float X2 = (X1 + mLastPencilX) / 2;
                float Y2 = (Y1 + mLastPencilY) / 2;
                mPath.quadTo(X1, Y1, X2, Y2);
//                mPath.lineTo(cX, cY);

                mFingerPath.addPathPoint(X2, Y2);

            }
//            invalidate();
        }
        ////////////////////////////////////////


        cX = x - mPosX + scalePointX; // canvas X
        cY = y - mPosY + scalePointY; // canvas Y
        // Only move if the ScaleGestureDetector isn't processing a gesture.
        if (currentMod == MOD_ZOOM && !mScaleDetector.isInProgress()) {
            final float dx = x - mLastTouchX; // change in X
            final float dy = y - mLastTouchY; // change in Y


                mPosX += dx;
                mPosY += dy;

//            invalidate();
        }





        mLastTouchX = x;
        mLastTouchY = y;

//        Log.v("TEST", "x:" + x + " cX:" + cX + " mPosX:"+mPosX + " scalePointX:"+scalePointX + " mScaleFactor:"+mScaleFactor + " bitmap.width:" + bitmap.getWidth());
    }
    private void touchUp(float _x, float _y) {

        final float x = (_x - scalePointX)/mScaleFactor;
        final float y = (_y - scalePointY)/mScaleFactor;
        cX = x - mPosX + scalePointX; // canvas X
        cY = y - mPosY + scalePointY; // canvas Y


        if (currentMod == MOD_TOUCH && startTimeTouch != 0) {

//            long secondsDurationTouch = (System.currentTimeMillis() - startTimeTouch) / 1000;
//            if(secondsDurationTouch >= 0.3) {
//                createNewProductOnCanvas(getContext());
//                mPhotoItemData.photo_sync = false;
//            }
            startTimeTouch = 0;

        }else if(currentMod == MOD_PANCIL){
            mPath.lineTo(mLastPencilX, mLastPencilY);
            mFingerPath.addPathPoint(mLastPencilX, mLastPencilY);
            mPhotoItemData.photo_sync = false;

            mLastPencilX = 0;
            mLastPencilY = 0;

        }

        mLastTouchX = 0;
        mLastTouchY = 0;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            lastScaleFactor = mScaleFactor;
            mScaleFactor *= detector.getScaleFactor();
            lastScalePointX = scalePointX;
            lastScalePointY = scalePointY;
            scalePointX =  detector.getFocusX();
            scalePointY = detector.getFocusY();



            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(minScaleFactor, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }

    public void setLastVendorCode(int lastVendorCode){
        mLastVendorCode = lastVendorCode;
    }
    public void setPhotoItemData(Photo itdata){

        mPhotoItemData = itdata;
//        bitmap = mPhotoItemData.photo_original;

        Bitmap photo_original = Bitmap.createBitmap(mPhotoItemData.photo_original);
        bitmap = photo_original.copy(Bitmap.Config.ARGB_8888, true);


        if(display_width > 0){
            minScaleFactor  = (float)display_width / (float)bitmap.getWidth();
            mScaleFactor    = minScaleFactor;
            lastScaleFactor = minScaleFactor;
        }

    }

    public void saveModifiedPhoto(){

        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache(true);

//        mScaleFactor = 1.0f;
//        scalePointX  = 0;
//        scalePointY  = 0;
//        mPosX        = 0;
//        mPosY        = 0;
//        invalidate();
//
//        mPhotoItemData.photo_modified = Bitmap.createBitmap(this.getDrawingCache());


        mPhotoItemData.photo_modified = makePhotoModified();;

        this.setDrawingCacheEnabled(false); // clear drawing cache




    }
    public void setBitmap(Bitmap bit){
        bitmap = bit;
    }
    public void setTouchMod(){
        currentMod = MOD_TOUCH;
    }
    public void setZoomMod(){
        currentMod = MOD_ZOOM;
    }
    public void setPencilMod(){
        currentMod = MOD_PANCIL;
    }
    public void undoLastPaint(){

        if(currentMod == MOD_PANCIL){
            if(mPhotoItemData.paths.size() > 0){
                mPhotoItemData.paths.remove(mPhotoItemData.paths.size() -1);
            }
        }

        if(currentMod == MOD_TOUCH){
            if(mPhotoItemData.products.size() > 0){
                mPhotoItemData.products.remove(mPhotoItemData.products.size() -1);
            }
        }


        invalidate();
    }

    void createNewProductOnCanvas(Context con){

        mLastVendorCode += 1;

        final Dialog dialog_price = new Dialog(con);
        dialog_price.setContentView(R.layout.dialog_price_layout);


        EditText dialog_price_vendor_code = (EditText) dialog_price.findViewById(R.id.dialog_price_vendor_code);
        dialog_price_vendor_code.setText(String.valueOf(mLastVendorCode));

        Button button_ok = (Button) dialog_price.findViewById(R.id.dialog_price_button_ok);
        button_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Paint p = new Paint();
                p.setColor(Color.RED);
                p.setTextSize(mTextSize);

                FingerPath fp = new FingerPath(p, BRUSH_SIZE);
                fp.setPointTouch(cX, cY);
//                mPhotoItemData.paths.add(fp);

                EditText ed_vendor_code = (EditText) dialog_price.findViewById(R.id.dialog_price_vendor_code);
                EditText ed_price       = (EditText) dialog_price.findViewById(R.id.dialog_price_price);
                EditText ed_quantity    = (EditText) dialog_price.findViewById(R.id.dialog_price_quantity);
                EditText ed_coment      = (EditText) dialog_price.findViewById(R.id.dialog_price_coment);

                int vendor_code = Integer.parseInt(ed_vendor_code.getText().toString().isEmpty() ? "0" : ed_vendor_code.getText().toString());
                double price    = Double.parseDouble(ed_price.getText().toString().isEmpty() ? "0" : ed_price.getText().toString());
                int quantity    = Integer.parseInt(ed_quantity.getText().toString().isEmpty() ? "0" : ed_quantity.getText().toString());
                String coment   = ed_coment.getText().toString();

                Product new_product = new Product();
                new_product.x           = cX;
                new_product.y           = cY;
                new_product.vendor_code = vendor_code;
                new_product.photo_id    = mPhotoItemData.photo_id;
                new_product.coment      = coment;

                mPhotoItemData.addProduct(new_product, price, quantity);

                invalidate();
                dialog_price.dismiss();
            }
        });

        dialog_price.setCancelable(false);
        dialog_price.setTitle("Price");
        dialog_price.show();

    }

    private Bitmap makePhotoModified(){

        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(mTextSize);


        Bitmap bitmap = Bitmap.createBitmap(mPhotoItemData.photo_original);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        for (Photo.Photo_products pp : mPhotoItemData.products) {
            canvas.drawCircle(pp.product.x, pp.product.y, 10, p);

            String text = Integer.toString(pp.product.vendor_code);
            float width_text = p.measureText(text);
            float X = (bitmap.getWidth() < pp.product.x + width_text ?   bitmap.getWidth() - width_text : pp.product.x);
            float Y = pp.product.y;
            canvas.drawText(text, X, Y, p);
        }

        for (FingerPath fp : mPhotoItemData.paths) {
            if(fp.path != null){
                canvas.drawPath(fp.path, fp.paint);
            }
        }



        Bitmap bitmap_compressed = Photo.compressBitmap(mutableBitmap);

        try {

            if(mPhotoItemData.path_photo_modified == null || mPhotoItemData.path_photo_modified.isEmpty()){
                File newFile = createImageFile();
                mPhotoItemData.path_photo_modified = newFile.getAbsolutePath();
            }

            FileOutputStream out = new FileOutputStream(mPhotoItemData.path_photo_modified);
            bitmap_compressed.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return mutableBitmap;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp        = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName    = "SUNSTONES_" + timeStamp + "_";
        File storageDir         = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image              = File.createTempFile("SUNSTONES", ".jpg", storageDir);

        return image;
    }


    public Runnable contin = new Runnable()
    {

        @Override
        public void run() {
            if(currentMod == MOD_TOUCH && startTimeTouch != 0)
            {
                createNewProductOnCanvas(getContext());
                mPhotoItemData.photo_sync = false;
            }
        }

    };


    public void setTextSize(int textSize){

        mTextSize = textSize;
        invalidate();
    }

    public void setToolbarHeight(int toolbarHeight){
        mToolbarHeight = toolbarHeight;
    }
}
