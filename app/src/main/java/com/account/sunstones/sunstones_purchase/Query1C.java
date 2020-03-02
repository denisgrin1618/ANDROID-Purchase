package com.account.sunstones.sunstones_purchase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

public class Query1C extends AsyncTask<String,Void,Void> {

    public static final String TASK_AUTORIZATION            = "TASK_AUTORIZATION";
    public static final String TASK_SYNCHRONIZATION         = "TASK_SYNCHRONIZATION";
    public static final String TASK_GET_LAST_VENDOR_CODE    = "TASK_GET_LAST_VENDOR_CODE";


    private Activity act;
    private final String NameBase1C = "7kmdevel1";
    private String answer = "";
    private String current_task;
    private int synchronization_progress_bar_current_max;
    private int synchronization_progress_bar_current_progress;
    private int progress_bar_total_max;
    private int progress_bar_total_progress;
    private String synchronization_text_rezalt = "";

    private String synchronization_text_view_current;
    DataBase data;

    public Query1C(Activity act){
        this.act  = act;
        this.data = DataBase.getInstance(act);
    }

    @Override
    protected Void doInBackground(String... params) {

        switch(params[0]) {

            case TASK_AUTORIZATION:
                current_task = TASK_AUTORIZATION;
                Authorization();
                break;

            case TASK_SYNCHRONIZATION:
                current_task = TASK_SYNCHRONIZATION;
                Synchronization();
                break;

            case TASK_GET_LAST_VENDOR_CODE:
                current_task = TASK_GET_LAST_VENDOR_CODE;
                Authorization();
                break;

            default:
                break;

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        switch(current_task) {

            case TASK_AUTORIZATION:
                if(answer.equals("Authorization success")){
                    ((AuthorizationActivity)act).openOffers();
                }else{
                    ((AuthorizationActivity)act).text_view_error.setText(answer);
                }


                break;


            case TASK_GET_LAST_VENDOR_CODE:
//                ((SynchronizationActivity)act).synchronization_text_rezalt.setText(answer);
                break;

            default:
                break;

        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        switch(current_task) {

            case TASK_AUTORIZATION:
                ((AuthorizationActivity)act).text_view_error.setText(answer);
                break;


            case TASK_GET_LAST_VENDOR_CODE:
//                ((SynchronizationActivity)act).synchronization_text_rezalt.setText(answer);
                break;


            case TASK_SYNCHRONIZATION:
                ((SynchronizationActivity)act).text_view_current.setText(synchronization_text_view_current);
                ((SynchronizationActivity)act).progress_bar_current.setMax(synchronization_progress_bar_current_max);
                ((SynchronizationActivity)act).progress_bar_current.setProgress(synchronization_progress_bar_current_progress);

                ((SynchronizationActivity)act).progress_bar_total.setMax(progress_bar_total_max);
                ((SynchronizationActivity)act).progress_bar_total.setProgress(progress_bar_total_progress);

                ((SynchronizationActivity)act).synchronization_text_rezalt.setText(synchronization_text_rezalt);


                break;

            default:
                break;

        }
    }


    private String GET(String urlSpec, String login, String password) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();


        String userCredentials = login + ":" + password;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
        connection.setRequestProperty ("Authorization", basicAuth);


        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);

        }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return new String(out.toByteArray());
        }
        finally {
            connection.disconnect();
        }
    }

    private String POST(String urlSpec, String post_data, String login, String password) throws IOException {

        URL url;
        String response = "";
        try {
            url = new URL(urlSpec);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(15000);
//            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            String userCredentials = login + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            conn.setRequestProperty ("Authorization", basicAuth);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(post_data);
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="error " + String.valueOf(responseCode) ;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }


    private void Authorization(){

        String AnswerJsonString = "";
//        DataBase data = new DataBase(act);

        try {

            String login    = ((AuthorizationActivity)act).edit_login.getText().toString();
            String password = ((AuthorizationActivity)act).edit_password.getText().toString();
            data.setLogin(login);
            data.setPassword(password);

            String url = Uri.parse("https://purchases.sunstones.ua/"+NameBase1C+"/hs/mobile/authorization")
                    .buildUpon()
//                    .appendQueryParameter("method", "flickr.photos.getRecent")
//                    .appendQueryParameter("api_key", API_KEY)
//                    .appendQueryParameter("format", "json")
//                    .appendQueryParameter("nojsoncallback", "1")
//                    .appendQueryParameter("extras", "url_s")
                    .build().toString();

            AnswerJsonString = GET(url, login, password);
            Log.i("TEST", "Received JSON: " + AnswerJsonString);

        } catch (IOException ioe) {
            Log.e("TEST", "Authorization Failed", ioe);
            answer = "Authorization Failed";
        }




        JSONObject obj = null;
        try {

            obj = new JSONObject(AnswerJsonString);

            String error            = obj.optString ( "error" );
//            JSONObject result       = obj.getJSONObject("result");
//            String name_user        = result.getString("name");

            if(error.isEmpty() || error == null){
                answer = "Authorization success";
                data.setAuthorizationStatus("success");
            } else{
                answer = "Authorization error. " + error;
                data.setAuthorizationStatus("error");
            }

        } catch (JSONException e) {
            answer = "Authorization error...";
            e.printStackTrace();
        }

    }

    private void Synchronization(){

        synchronization_text_rezalt         = "";
        synchronization_text_view_current   = "";
        progress_bar_total_max              = 6;
        progress_bar_total_progress         = 0;
        publishProgress();


        //1/
        uploadPhotos();
        progress_bar_total_progress = 1;
        publishProgress();

        //2.
        uploadProducts();
        progress_bar_total_progress = 2;
        publishProgress();


        //4.
        uploadOffers();
        progress_bar_total_progress = 4;
        publishProgress();

        //5.
        uploadAssembly();
        progress_bar_total_progress = 5;
        publishProgress();

        //6.
        downloadOrders();
        progress_bar_total_progress = 6;


        synchronization_text_view_current = "sync done";
        synchronization_progress_bar_current_max = 1;
        synchronization_progress_bar_current_progress = 1;
        publishProgress();

    }

    private void uploadPhotos(){

        String login            = data.getLogin();
        String password         = data.getPassword();
        ArrayList<Photo> photos = data.getPhotosNotUploaded();

        String url = Uri.parse("https://purchases.sunstones.ua/"+NameBase1C+"/hs/mobile/photo").buildUpon().build().toString();

        synchronization_progress_bar_current_max        = photos.size();
        synchronization_progress_bar_current_progress   = 0;
        synchronization_text_view_current               = "Upload photos";
        publishProgress();

        for (int i = 0; i < photos.size(); i++)
        {
            Photo photo         = photos.get(i);
            String json_photo   = photo.get_json().toString();
            try {
                answer = POST(url, json_photo, login, password);

                String photo_uid = readJSON(answer, "result");
                if(!photo_uid.isEmpty()){
                    photo.photo_uid = photo_uid;
                    photo.photo_sync = true;
                    data.addPhoto(photo);
                }

                synchronization_progress_bar_current_progress = i+1;
                publishProgress();

                synchronization_text_rezalt += "";

            } catch (IOException e) {
                synchronization_text_rezalt += "\nPhotos was not uploaded";

            }
        }

    }

    private void uploadProducts(){

        String login                = data.getLogin();
        String password             = data.getPassword();
        ArrayList<Product> products = data.getProductsNotUploaded();

        String url = Uri.parse("https://purchases.sunstones.ua/"+NameBase1C+"/hs/mobile/product").buildUpon().build().toString();

        synchronization_progress_bar_current_max        = products.size();
        synchronization_progress_bar_current_progress   = 0;
        synchronization_text_view_current               = "Upload products";
        publishProgress();

        for (int i = 0; i < products.size(); i++)
        {
            Product product     = products.get(i);
            String json_photo   = product.get_json().toString();
            try {
                answer = POST(url, json_photo, login, password);

                String product_uid = readJSON(answer, "result");
                if(!product_uid.isEmpty()){
                    product.uid  = product_uid;
                    product.sync = true;
                    data.addProduct(product);
                }

                synchronization_progress_bar_current_progress = i+1;
                synchronization_text_rezalt += "";
                publishProgress();


            } catch (IOException e) {
                synchronization_text_rezalt += "\nProducts was not uploaded";

            }
        }


    }

    private void uploadOffers(){

        String login            = data.getLogin();
        String password         = data.getPassword();
        ArrayList<Offer> offers = data.getOffersNotUploaded();

        String url = Uri.parse("https://purchases.sunstones.ua/"+NameBase1C+"/hs/mobile/offer").buildUpon().build().toString();

        synchronization_progress_bar_current_max        = offers.size();
        synchronization_progress_bar_current_progress   = 0;
        synchronization_text_view_current               = "Upload offers";
        publishProgress();

        for (int i = 0; i < offers.size(); i++)
        {
            Offer offer         = offers.get(i);
            String json_offer   = offer.get_json().toString();
            try {
                answer = POST(url, json_offer, login, password);

                String offer_uid = readJSON(answer, "result");
                if(!offer_uid.isEmpty()){
                    offer.uid  = offer_uid;
                    offer.sync = true;
                    data.addOffer(offer);
                }

                synchronization_progress_bar_current_progress = i+1;
                synchronization_text_rezalt += "";
                publishProgress();

            } catch (IOException e) {
                synchronization_text_rezalt += "\nOffers was not uploaded";
            }
        }


    }

    private void downloadOrders(){

        String login     = data.getLogin();
        String password  = data.getPassword();

        String url = Uri.parse("https://purchases.sunstones.ua/"+NameBase1C+"/hs/mobile/orders").buildUpon().build().toString();
        synchronization_text_view_current = "Download orders";
        publishProgress();

            try{
                answer = GET(url, login, password);
                synchronization_text_rezalt += "";
            } catch (IOException e) {
                synchronization_text_rezalt += "\nOrders was not downloaded";

                return;
            }



        try {

            JSONObject obj          = new JSONObject(answer);
            String error            = obj.optString ( "error" );
            JSONObject result       = obj.getJSONObject("result");

            if(error.isEmpty() || error == null){

                JSONArray json_orders = result.getJSONArray("orders");

                synchronization_progress_bar_current_max = json_orders.length();
                synchronization_progress_bar_current_progress = 0;
                publishProgress();

                for(int i=0; i<json_orders.length(); i++){
                    JSONObject json_order = json_orders.getJSONObject(i);

                    Order order = new Order();
                    order.id        = json_order.getInt("id");
                    order.uid       = json_order.getString("uid");
                    order.date      = json_order.getLong("date");
                    order.number    = json_order.getInt("number");
                    order.sync      = true;


                    JSONArray json_products = json_order.getJSONArray("products");
                    for(int j=0; j<json_products.length(); j++){

                        JSONObject json_product = json_products.getJSONObject(j);

                        ProductType product_type = null;
                        if(json_product.has("type")){
                            JSONObject json_product_type = json_product.getJSONObject("type");
                            product_type = new ProductType();
                            product_type.id         = json_product_type.getInt("id");
                            product_type.name_en    = json_product_type.getString("name_en");
                            product_type.name_chn   = json_product_type.getString("name_chn");
                            data.addProductType(product_type);
                        }else{
                            product_type = new ProductType();
                            product_type.id         = 0;
                            product_type.name_en    = "";
                            product_type.name_chn   = "";
                        }


                        ProductCatalog product_catalog = null;
                        if(json_product.has("catalog")){
                            JSONObject json_product_catalog = json_product.getJSONObject("catalog");
                            product_catalog = new ProductCatalog();
                            product_catalog.id         = json_product_catalog.getInt("id");
                            product_catalog.name_en    = json_product_catalog.getString("name_en");
                            product_catalog.name_chn   = json_product_catalog.getString("name_chn");
                            data.addProductCatalog(product_catalog);
                        }else{
                            product_catalog = new ProductCatalog();
                            product_catalog.id         = 0;
                            product_catalog.name_en    = "";
                            product_catalog.name_chn   = "";
                        }



                        String product_uid      = json_product.getString("product_uid");
                        int quantity            = json_product.getInt("quantity");
                        int quantity_assembly   = json_product.getInt("quantity_assembly");
                        Boolean sey_aside       = false; // json_product.getBoolean("set_aside");
                        Boolean out_of_stock    = false; // json_product.getBoolean("out_of_stock");
                        Double price            = json_product.getDouble("price");
//                        Product p               = data.getProduct(product_uid);



                        ////////////

                        String photo_uid = json_product.getString("photo_uid");
                        Photo photo = data.getPhoto(photo_uid);
                        if( photo_uid.isEmpty() || photo == null){

                            photo = new Photo();
                            photo.photo_uid      = photo_uid;
                            photo.photo_sync     = true;
                            photo.photo_original = downloadPhoto(photo_uid);

                            File file = data.saveBitmapToFile(act, photo.photo_original);
                            photo.path_photo_original = file.getAbsolutePath();

                            data.addPhoto(photo);
                        }

                        Product product = data.getProduct(product_uid);
                        if( product == null) {
                            //get photo from 1C

                            product = new Product();
                            product.uid         = product_uid;
                            product.x           = (float) json_product.getDouble("product_x");
                            product.y           = (float) json_product.getDouble("product_y");
                            product.vendor_code = json_product.getInt("product_vendor_code");
                            product.sync        = true;
                            product.photo_uid   = photo.photo_uid;
                            product.photo_id    = photo.photo_id;
                            product.price       = price;

                            product.type = product_type;
                            product.catalog = product_catalog;

                            data.addProduct(product);
                            data.addPhotoProduct(product);

                        }else{
                            product.type = product_type;
                            product.catalog = product_catalog;
                            data.addProduct(product);
                        }



                        order.addProduct(product, price, quantity, quantity_assembly, sey_aside, out_of_stock);

                    }

                    data.addOrder(order);
                    synchronization_progress_bar_current_progress = i+1;
                    publishProgress();
                }

            }

        } catch (JSONException e) {

        }




    }

    private void uploadAssembly(){

        String login            = data.getLogin();
        String password         = data.getPassword();
        ArrayList<Assembly> assemblies = data.getAssembliesNotUploaded();

        String url = Uri.parse("https://purchases.sunstones.ua/"+NameBase1C+"/hs/mobile/assembly").buildUpon().build().toString();

        synchronization_progress_bar_current_max        = assemblies.size();
        synchronization_progress_bar_current_progress   = 0;
        synchronization_text_view_current               = "Upload assembled assemblies";
        publishProgress();

        for (int i = 0; i < assemblies.size(); i++)
        {
            Assembly assembly   = assemblies.get(i);
            String json_offer   = assembly.get_json().toString();
            try {
                answer = POST(url, json_offer, login, password);

                String assembly_uid = readJSON(answer, "result");
                if(!assembly_uid.isEmpty()){
                    assembly.uid  = assembly_uid;
                    assembly.sync = true;
                    data.addAssembly(assembly);
                }

                synchronization_progress_bar_current_progress = i+1;
                publishProgress();

            } catch (IOException e) {

                synchronization_text_rezalt += "\nAssemlies was not uploaded";

            }
        }


    }



    private Bitmap downloadPhoto(String photo_uid){

        String login     = data.getLogin();
        String password  = data.getPassword();

        String url = Uri.parse("https://purchases.sunstones.ua/"+NameBase1C+"/hs/mobile/photo")
                .buildUpon()
                .appendQueryParameter("photo_uid", photo_uid)
                .build()
                .toString();

        try{
            answer = GET(url, login, password);
        } catch (IOException e) {
            return null;
        }



        try {

            JSONObject obj          = new JSONObject(answer);
            String error            = obj.optString ( "error" );
            JSONObject result       = obj.getJSONObject("result");

            if(error.isEmpty() || error == null){
                String encodedImage = result.optString("original");
                byte[] decodedString = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                return decodedByte;
            }

        } catch (JSONException e) {

        }

        return null;
    }

    private String readJSON(String body_json, String name_field) throws IOException {

        JSONObject obj = null;
        try {
            obj = new JSONObject(body_json);
//            JSONArray arr = obj.getJSONArray(name_field);
//            arr.getJSONObject(i).getString("name");

            return  obj.getString(name_field);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }


}
