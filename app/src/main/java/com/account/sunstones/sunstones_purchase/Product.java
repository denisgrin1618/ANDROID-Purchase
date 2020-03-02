package com.account.sunstones.sunstones_purchase;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {

    public int id;
    public int photo_id;
    public String photo_uid;
    public String uid;
    public float x;
    public float y;
    public int vendor_code;
    public Boolean sync;
    public Double price;
    public String coment="";
    public Boolean out_of_stock=false;
    public ProductType type;
    public ProductCatalog catalog;

    public Product(){

        sync = false;

    }

    public JSONObject get_json(){

        try {
            JSONObject json = new JSONObject();
            json.put("vendor_code", vendor_code);
            json.put("uid", uid);
            json.put("photo_uid", photo_uid);
            json.put("x", x);
            json.put("y", y);
            json.put("coment", coment);
            json.put("out_of_stock", out_of_stock);

            return json;
        }

        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return  new JSONObject();
    }
}
