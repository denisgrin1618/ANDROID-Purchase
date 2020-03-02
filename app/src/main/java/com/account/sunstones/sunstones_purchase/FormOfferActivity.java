package com.account.sunstones.sunstones_purchase;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FormOfferActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST             = 1337;
    private static final int REQUEST_GALLERY            = 22;
    private static final int REQUEST_CAPTURE_IMAGE      = 100;


    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int REQUEST_CODE_PERMISSIONS                = 121;
    String[] filters = {"no filter", "show products=0", "show products>0"};

//


    private GridView grid_photo_view;
    private ArrayList<Photo> listData = new ArrayList<Photo>();
    private DataBase m_data;
    private Offer offer;
    private String mCurrentPhotoPath;
    private Uri mUriFilePhotoFromCamera;
    private GridPhotoViewAdapter mGridPhotoViewAdapter;
    private Boolean m_delete_photo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_offer);

        m_data = DataBase.getInstance(getApplicationContext());


        int offer_id = getIntent().getIntExtra("offer_id", 0);
        offer = m_data.getOffer(offer_id);


//        listData = data.getPhotoFromOffer(1);

        grid_photo_view = (GridView) findViewById(R.id.grid_photo_view);

        offer.curentFilter = Offer.FILTER_ALL;
        ArrayList<Photo> listData = offer.getFilteredListPhotos();
        mGridPhotoViewAdapter = new GridPhotoViewAdapter(this, offer);
        grid_photo_view.setAdapter(mGridPhotoViewAdapter);
//        grid_photo_view.setAdapter(new GridPhotoViewAdapter(this, offer.Photos));


    }

    @Override
    protected void onDestroy() {
        offer.sync = false;
        m_data.saveOffer(offer);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(!allPermissionsGranted()){
                showError("Permission not granted");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent



            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mUriFilePhotoFromCamera);
                if (bitmap != null) {
//                    Bitmap bitmap_compressed = Photo.compressBitmap(bitmap);
                    Bitmap bitmap_compressed = Photo.scaleBitmap(bitmap);

                    File file = new File(mUriFilePhotoFromCamera.getPath());
                    Photo.saveBitmapToFile(file, bitmap_compressed);

                    Photo photo = new Photo(" ", bitmap_compressed, 0);
                    photo.path_photo_original = mCurrentPhotoPath;

                    offer.Photos.add(photo);
                }
            } catch (Exception error) { }

        }

        try {
            // When an Image is picked
            if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != data) {

                if(data.getData()!=null){

                    Uri mImageUri=data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
//                    Bitmap bitmap_compressed = Photo.compressBitmap(bitmap);
                    Bitmap bitmap_compressed = Photo.scaleBitmap(bitmap);

                    Photo photo = new Photo(" ", bitmap_compressed, 0);
                    File file = m_data.saveBitmapToFile(this, bitmap_compressed);
                    photo.path_photo_original = file.getAbsolutePath();
                    offer.Photos.add(photo);

                }
                else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                            Bitmap bitmap_compressed = Photo.compressBitmap(bitmap);
                            Bitmap bitmap_compressed = Photo.scaleBitmap(bitmap);


                            Photo photo = new Photo(" ", bitmap_compressed, 0);

                            File file = m_data.saveBitmapToFile(this, bitmap_compressed);
                            photo.path_photo_original = file.getAbsolutePath();
                            offer.Photos.add(photo);

                        }

                    }
                }

//                grid_photo_view.setAdapter(new GridPhotoViewAdapter(this, offer.Photos));

            } else {
//                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


        m_data.saveOffer(offer);
        grid_photo_view.setAdapter(new GridPhotoViewAdapter(this, offer));
        grid_photo_view.invalidate();

    }




    public void onClickButtonPhoto(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_GALLERY);
    }

    public void onClickButtonCamera(View view) {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,  "${applicationId}.provider", photoFile);
//                Uri photoURI = Uri.fromFile(new File(mCurrentPhotoPath));
                mUriFilePhotoFromCamera = Uri.fromFile(photoFile);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUriFilePhotoFromCamera);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }

    }

    public void onClickButtonSave(View view) {

        offer.sync = false;
        m_data.saveOffer(offer);

        Intent intent = new Intent(this, OffersActivity.class);
        startActivity(intent);

    }

    public void onClickButtonDelete(View view){

        m_delete_photo = !m_delete_photo;

        if(m_delete_photo){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete")
                    .setMessage("Delete selected photos?")
                    .setIcon(R.drawable.delete)
                    .setCancelable(false)
                    .setPositiveButton("Оk",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    deleteSelectedPhotos();

                                }
                            })
                    .setNegativeButton("Cansel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();




        }else{

//            mGridPhotoViewAdapter.setVisibleCheckBoxDelete(true);
//            grid_photo_view.setAdapter(mGridPhotoViewAdapter);

        }


        mGridPhotoViewAdapter = new GridPhotoViewAdapter(this, offer);
        mGridPhotoViewAdapter.setVisibleCheckBoxDelete(m_delete_photo);
        grid_photo_view.setAdapter(mGridPhotoViewAdapter);

    }


    public void deleteSelectedPhotos(){

        ArrayList<Photo> list_photo_delete = mGridPhotoViewAdapter.getSelectedPhotosForDelete();
        offer.deletePhotos(list_photo_delete);

        for(Photo photo : list_photo_delete){
            m_data.deletePhoto(photo);
        }





    }

    public void onClickButtonFilter(View view){

        openDialogFilter();

    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp        = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName    = "SUNSTONES_" + timeStamp + "_";
        File storageDir         = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image              = File.createTempFile("SUNSTONES", ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    public void showError(String erorr){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!")
                .setMessage(erorr)
                .setIcon(R.drawable.delete)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void setFilter() {
        grid_photo_view.setAdapter(new GridPhotoViewAdapter(this, offer));
    }

    private void openDialogFilter(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_offer_filter);

        RadioButton radio_button_show_all_photos                        = (RadioButton) dialog.findViewById(R.id.radio_button_show_all_photos);
        RadioButton radio_button_show_photos_where_products_equvel_0    = (RadioButton) dialog.findViewById(R.id.radio_button_show_photos_where_products_equvel_0);
        RadioButton radio_button_show_photos_where_products_more_0      = (RadioButton) dialog.findViewById(R.id.radio_button_show_photos_where_products_more_0);


        radio_button_show_all_photos.setChecked(offer.curentFilter == Offer.FILTER_ALL);
        radio_button_show_photos_where_products_equvel_0.setChecked(offer.curentFilter == Offer.FILTER_PRODUCTS_EQUEL_ZERO);
        radio_button_show_photos_where_products_more_0.setChecked(offer.curentFilter == Offer.FILTER_PRODUCTS_ABOVE_ZERO);

        Button button_ok = (Button) dialog.findViewById(R.id.dialog_offer_filter_button_ok);
        button_ok.setOnClickListener(new FilterOnClickListener(dialog, this));

        dialog.setCancelable(true);
        dialog.setTitle("FILTER");
        dialog.show();

    }




    public class FilterOnClickListener implements View.OnClickListener {


        Dialog dialog;
        FormOfferActivity ac;

        public FilterOnClickListener(Dialog dialog, FormOfferActivity ac) {
            this.dialog = dialog;
            this.ac = ac;
        }

        @Override
        public void onClick(View v)
        {
            RadioButton radio_button_show_all_photos                        = (RadioButton) dialog.findViewById(R.id.radio_button_show_all_photos);
            RadioButton radio_button_show_photos_where_products_equvel_0    = (RadioButton) dialog.findViewById(R.id.radio_button_show_photos_where_products_equvel_0);
            RadioButton radio_button_show_photos_where_products_more_0      = (RadioButton) dialog.findViewById(R.id.radio_button_show_photos_where_products_more_0);

            if(radio_button_show_all_photos.isChecked()){
                ac.offer.curentFilter = Offer.FILTER_ALL;
            }else if(radio_button_show_photos_where_products_equvel_0.isChecked()){
                ac.offer.curentFilter = Offer.FILTER_PRODUCTS_EQUEL_ZERO;
            }else if(radio_button_show_photos_where_products_more_0.isChecked()){
                ac.offer.curentFilter = Offer.FILTER_PRODUCTS_ABOVE_ZERO;
            }
            ac.setFilter();
            dialog.dismiss();
        }

    };

}
