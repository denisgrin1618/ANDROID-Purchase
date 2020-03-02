package com.account.sunstones.sunstones_purchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Offer {


    public static final int FILTER_ALL                 = 1;
    public static final int FILTER_PRODUCTS_EQUEL_ZERO = 2;
    public static final int FILTER_PRODUCTS_ABOVE_ZERO = 3;

    public int curentFilter = 1;

    public int id;
    public long date;
    public int number;
    public Boolean sync;
    public int total_quantity_products;
    public String uid;
    public ArrayList<Photo> Photos = new ArrayList();



    public Offer(){ this.sync = false;}


    public int getTotalQuantityProducts(){

        int total_quantity = 0;
        for (int i = 0; i < Photos.size(); i++) {
            total_quantity += Photos.get(i).products.size();
        }

        return total_quantity;
    }

    public int getIndexPhoto(int phohto_id){

        for (int i = 0; i < Photos.size(); i++) {
            if(phohto_id == Photos.get(i).photo_id){
                return i;
            }
        }

        return 0;
    }

    public Photo getGridPhotoItemData(int phohto_id){

        for (int i = 0; i < Photos.size(); i++) {
            if(phohto_id == Photos.get(i).photo_id){
                return Photos.get(i);
            }
        }

        return null;
    }

    public JSONObject get_json(){

        try {

            JSONArray json_products = new JSONArray();
            for (int i = 0; i < Photos.size(); i++)
            {
                Photo photo_item = Photos.get(i);
                for (int j = 0; j < photo_item.products.size(); j++) {
                    Product product = photo_item.products.get(j).product;

                    JSONObject json_product = new JSONObject();
                    json_product.put("product_uid",         product.uid);
                    json_product.put("product_vendor_code", product.vendor_code);
                    json_product.put("photo_uid",           photo_item.photo_uid);
                    json_product.put("price",               photo_item.products.get(j).price);
                    json_product.put("quantity",            photo_item.products.get(j).quantity);



                    json_products.put(json_product);
                }
            }

            JSONObject json = new JSONObject();
            json.put("date", date);
            json.put("number", number);
            json.put("uid", uid);
            json.put("products", json_products);
            return json;
        }

        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return  new JSONObject();
    }

    public ArrayList<Photo> getFilteredListPhotos(){

        ArrayList<Photo> list = new ArrayList<Photo>();

        for (int i = 0; i < Photos.size(); i++) {

            Photo photo = Photos.get(i);

            if(curentFilter == FILTER_ALL){

            }
            if(curentFilter == FILTER_PRODUCTS_EQUEL_ZERO && photo.products.size() > 0){
                continue;
            }
            if(curentFilter == FILTER_PRODUCTS_ABOVE_ZERO && photo.products.size() == 0){
                continue;
            }


            list.add(photo);
        }

        return list;
    }

    public void deletePhotos(ArrayList<Photo> listDelete){

        for (Object photo : listDelete) {
            Photos.remove(photo);
        }

    }

}
