package com.account.sunstones.sunstones_purchase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class PhotoActivity extends AppCompatActivity {

    Button button_pencil;
    Button button_zoom;
    Button button_add;
    PaintView paint_view;
    DataBase data;
    Offer offer;
    int current_photo_id;
    int display_width;
    int display_height;
    SeekBar seek_bar_size_phont;
    LinearLayout liner_layout_toolbar_1;
    LinearLayout liner_layout_toolbar_2;

    Photo mPhotoItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        data = DataBase.getInstance(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point sizeWindow = new Point();
        display.getSize(sizeWindow);
        display_width = sizeWindow.x;
        display_height = sizeWindow.y;


        int textSize = data.getPhontSize();
        button_pencil           = (Button) findViewById(R.id.button_pencil);
        button_zoom             = (Button) findViewById(R.id.button_zoom);
        button_add              = (Button) findViewById(R.id.button_add);
        paint_view              = (PaintView)findViewById(R.id.paint_view);
        liner_layout_toolbar_1  = (LinearLayout)findViewById(R.id.liner_layout_toolbar_1);
        liner_layout_toolbar_2  = (LinearLayout)findViewById(R.id.liner_layout_toolbar_2);
        seek_bar_size_phont     = (SeekBar)findViewById(R.id.seek_bar_size_phont);
//        seek_bar_size_phont.setVisibility(View.INVISIBLE);
        seek_bar_size_phont.setProgress(textSize);
        seek_bar_size_phont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                data.setPhontSize(seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                paint_view.setTextSize(progress);
            }
        });

        paint_view.display_width = display_width;
        paint_view.display_height = display_height;


        int photo_id    = getIntent().getIntExtra("photo_id", 0);
        int offer_id    = getIntent().getIntExtra("offer_id", 0);
        int offer_current_filter = getIntent().getIntExtra("offer_current_filter", 1);
        offer           = data.getOffer(offer_id);
        offer.curentFilter = offer_current_filter;
        current_photo_id= photo_id;


        paint_view.setPhotoItemData( offer.getGridPhotoItemData(current_photo_id) );
        paint_view.setTextSize(textSize);

        int lastVendorCode = data.getLastProductVendorCode();
        paint_view.setLastVendorCode(lastVendorCode);

//        int paintToolbarHeight = liner_layout_toolbar_1.getHeight() + liner_layout_toolbar_2.getHeight() + 50;
        paint_view.setToolbarHeight(300);


    }

    @Override
    protected void onDestroy() {
        data.saveOffer(offer);
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("OFFER #" + String.valueOf(offer.id));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Boolean rezalt = MainMenu.onOptionsItemSelected(item, this);
        if(rezalt)
            return true;
        else
            return super.onOptionsItemSelected(item);

    }


    public void onClickButtonPencil(View view){

//        Animation anim = new AlphaAnimation(1.0f, 0.0f);
//        anim.setDuration(1000);
//        button_pencil.startAnimation(anim);

        button_pencil.setBackgroundResource(R.drawable.button_pencil_gray);
        button_zoom.setBackgroundResource(R.drawable.button_zoom);
        button_add.setBackgroundResource(R.drawable.button_add);
//        seek_bar_size_phont.setVisibility(View.INVISIBLE);
        paint_view.setPencilMod();
    }

    public void onClickButtonZoom(View view){

        button_pencil.setBackgroundResource(R.drawable.button_pencil);
        button_zoom.setBackgroundResource(R.drawable.button_zoom_gray);
        button_add.setBackgroundResource(R.drawable.button_add);

//        seek_bar_size_phont.setVisibility(View.INVISIBLE);
        paint_view.setZoomMod();
    }

    public void onClickButtonAdd(View view){

        button_pencil.setBackgroundResource(R.drawable.button_pencil);
        button_zoom.setBackgroundResource(R.drawable.button_zoom);
        button_add.setBackgroundResource(R.drawable.button_add_gray);

//        seek_bar_size_phont.setVisibility(View.VISIBLE);
        paint_view.setTouchMod();
    }

    public void onClickButtonUndo(View view){
        paint_view.undoLastPaint();
    }

    public void onClickButtonSave(View view){

        paint_view.saveModifiedPhoto();
        offer.sync = false;
        data.saveOffer(offer);

//        finish();

        Intent intent = new Intent(this, FormOfferActivity.class);
        intent.putExtra("offer_id", offer.id);
        startActivity(intent);
    }

    public void onClickButtonLeft(View view){

        paint_view.saveModifiedPhoto();
        int j = offer.getIndexPhoto(current_photo_id) -1;


        for (int i=j; i >= 0 && i < offer.Photos.size(); i--) {

            Photo photo = offer.Photos.get(i);

            if(offer.curentFilter == Offer.FILTER_ALL){

            }
            if(offer.curentFilter == Offer.FILTER_PRODUCTS_EQUEL_ZERO && photo.products.size() > 0){
                continue;
            }
            if(offer.curentFilter == Offer.FILTER_PRODUCTS_ABOVE_ZERO && photo.products.size() == 0){
                continue;
            }


            current_photo_id = photo.photo_id;
            Bitmap bitmap   = data.getPhotoOriginal(current_photo_id);

            if(bitmap == null){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.empty_image);
            }
//            paint_view.setBitmap(bitmap);
            paint_view.setPhotoItemData(photo);
            paint_view.invalidate();
            return;
        }





//        if(i >= 0){
//
//            Photo gpid = offer.Photos.get(i);
//            current_photo_id = gpid.photo_id;
//            Bitmap bitmap   = data.getPhotoOriginal(current_photo_id);
//
//            if(bitmap == null){
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.empty_image);
//            }
////            paint_view.setBitmap(bitmap);
//            paint_view.setPhotoItemData(gpid);
//            paint_view.invalidate();
//
//        }
    }

    public void onClickButtonRight(View view){

        paint_view.saveModifiedPhoto();
        int j = offer.getIndexPhoto(current_photo_id) + 1;


        for (int i=j; i < offer.Photos.size(); i++) {

            Photo photo = offer.Photos.get(i);

            if(offer.curentFilter == Offer.FILTER_ALL){

            }
            if(offer.curentFilter == Offer.FILTER_PRODUCTS_EQUEL_ZERO && photo.products.size() > 0){
                continue;
            }
            if(offer.curentFilter == Offer.FILTER_PRODUCTS_ABOVE_ZERO && photo.products.size() == 0){
                continue;
            }


            current_photo_id = photo.photo_id;
            Bitmap bitmap   = data.getPhotoOriginal(current_photo_id);

            if(bitmap == null){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.empty_image);
            }
//            paint_view.setBitmap(bitmap);
            paint_view.setPhotoItemData(photo);
            paint_view.invalidate();
            return;
        }



//        if(i < offer.Photos.size()){
//
//
//            Photo gpid = offer.Photos.get(i);
//            current_photo_id = gpid.photo_id;
////            Bitmap bitmap   = data.getPhotoOriginal(current_photo_id);
////
////            if(bitmap == null){
////                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.empty_image);
////            }
////            paint_view.setBitmap(bitmap);
//            paint_view.setPhotoItemData(gpid);
//            paint_view.invalidate();
//        }

    }





}
