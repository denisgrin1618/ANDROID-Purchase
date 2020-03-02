package com.account.sunstones.sunstones_purchase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Photo {

    public class Photo_products{
        public Product product;
        public Double price;
        public int quantity;
        public Photo_products(){}
    }


    public String title;
    public int photo_id;
    public String photo_uid;
    public int offer_id;
    public Bitmap photo_original;
    public Bitmap photo_modified;
    public String path_photo_original;
    public String path_photo_modified;
    public Boolean photo_sync;


    public ArrayList<FingerPath> paths = new ArrayList<>();
    public ArrayList<Photo_products> products = new ArrayList();

    public Photo(){this.photo_sync     = false;}
    public Photo(String title, Bitmap image, int photo_id){
        this.photo_original = image;
        this.title          = title;
        this.photo_id       = photo_id;
        this.photo_sync     = false;
    }


    public void addFingerPath(FingerPath fp){
        paths.add(fp);
    }

    public void addProduct(Product product, Double price, int quantity){

        Photo_products pp = new Photo_products();
        pp.price        = price;
        pp.quantity     = quantity;
        pp.product      = product;
        products.add(pp);

    }

    public int getQuantityProducts(){
        return products.size();
    }

    public JSONObject get_json(){

        try {
            JSONObject json = new JSONObject();
            json.put("photo_id", photo_id);
            json.put("photo_uid", photo_uid);
            json.put("photo_original", get_img_Base64(photo_original));
            json.put("photo_modified", get_img_Base64(photo_modified));
            return json;
        }

        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return  new JSONObject();
    }

    public String get_img_Base64(Bitmap bitmap){

        String img_str = "";

        if(bitmap != null){
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] image=stream.toByteArray();
            img_str = Base64.encodeToString(image, 0);
        }

        return img_str;
    }

    public static Bitmap compressBitmap(Bitmap original){

//        return original;


//        Bitmap decodedBitmap = original.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap decodedBitmap = original;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            decodedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            decodedBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

//            int currSize;
//            int currQuality = 90;
//            int maxSizeBytes = 1024 *1024; //1Mb
//
//            do {
//                decodedBitmap.compress(Bitmap.CompressFormat.JPEG, currQuality, out);
//                currSize = out.toByteArray().length;
//                // limit quality by 5 percent every time
//                currQuality -= 5;
//
//            } while (currSize >= maxSizeBytes || currQuality <= 0);
//
//            decodedBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));


        }catch(Exception e){

        }

        return decodedBitmap;
    }


    public static Bitmap scaleBitmap(final Bitmap input) {
        final long maxBytes     = 1024 * 1024 * 2; //1mb
        final int currentWidth = input.getWidth();
        final int currentHeight = input.getHeight();
        final int currentPixels = currentWidth * currentHeight;
        // Get the amount of max pixels:
        // 1 pixel = 4 bytes (R, G, B, A)
        final long maxPixels = maxBytes / 4; // Floored
        if (currentPixels <= maxPixels) {
            // Already correct size:
            return input;
        }
        // Scaling factor when maintaining aspect ratio is the square root since x and y have a relation:
        final double scaleFactor = Math.sqrt(maxPixels / (double) currentPixels);
        final int newWidthPx = (int) Math.floor(currentWidth * scaleFactor);
        final int newHeightPx = (int) Math.floor(currentHeight * scaleFactor);
//        Timber.i("Scaled bitmap sizes are %1$s x %2$s when original sizes are %3$s x %4$s and currentPixels %5$s and maxPixels %6$s and scaled total pixels are: %7$s",
//                newWidthPx, newHeightPx, currentWidth, currentHeight, currentPixels, maxPixels, (newWidthPx * newHeightPx));
        final Bitmap output = Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true);
        return output;
    }

    public static void saveBitmapToFile(File newFile, Bitmap bitmap) {

         try {
            FileOutputStream out = new FileOutputStream(newFile.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }catch(IOException e){

        }

    }
}
