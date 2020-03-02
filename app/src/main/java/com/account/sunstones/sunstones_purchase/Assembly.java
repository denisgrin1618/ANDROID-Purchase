package com.account.sunstones.sunstones_purchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Assembly {

    public class Assembly_products{
        public int id;
        public Product product;
        public Double price;
        public int quantity;
        public int order_id;
        public String order_uid;
        public Assembly_products(){}
    }

    public class Assembly_products_for_assembly{
        public Product product;
        public Double price;
        public int quantity_order;
        public int quantity_assembly;
        public Boolean set_aside;
        public Boolean out_of_stock;
        public int order_id;
        public int order_products_id;
        public String coment;
        public Boolean changed;
        public Assembly_products_for_assembly(){}
    }


    public int id;
    public long date;
    public int number;
    public String uid;
    public Boolean sync;
    public Double sum = 0.0;
    public int products_count = 0;
    public ArrayList<Assembly.Assembly_products> products = new ArrayList<>();
    public ArrayList<Assembly.Assembly_products_for_assembly> products_for_assembly = new ArrayList<>();

    public Assembly(){ this.sync = false; }

    public void addProduct(Product product, Double price, int quantity, int order_id, String order_uid, int assembly_products_id){

        Assembly_products pp = new Assembly_products();
        pp.product      = product;
        pp.price        = price;
        pp.quantity     = quantity;
        pp.order_id     = order_id;
        pp.order_uid    = order_uid;
        pp.id           = assembly_products_id;

        products.add(pp);
    }

    public void addProductsForAssembly(Product product, Double price, int quantity_order, Boolean set_aside, Boolean out_of_stock, int order_id, int quantity_assembly, int order_products_id){

        Assembly_products_for_assembly pp = new Assembly_products_for_assembly();
        pp.product          = product;
        pp.price            = price;
        pp.quantity_order   = quantity_order;
        pp.quantity_assembly= quantity_assembly;
        pp.set_aside        = set_aside;
        pp.out_of_stock     = out_of_stock;
        pp.order_id         = order_id;
        pp.order_products_id= order_products_id;
        pp.changed          = false;
        products_for_assembly.add(pp);
    }

    public ArrayList<Integer> getListOrderIdProductsForAssembly() {

        ArrayList<Integer> list = new ArrayList();
        for(Assembly.Assembly_products_for_assembly pp : products_for_assembly){

            if(!list.contains(pp.order_id)){
                list.add(pp.order_id);
            }

        }

        return list;

    }

    public JSONObject get_json(){

        try {

            JSONArray json_products = new JSONArray();
            for (int i = 0; i < products.size(); i++)
            {
                Assembly.Assembly_products item = products.get(i);

                JSONObject json_product = new JSONObject();
                json_product.put("product_uid",         item.product.uid);
                json_product.put("order_uid",           item.order_uid);
                json_product.put("price",               item.price);
                json_product.put("quantity_assembly",   item.quantity);

                json_products.put(json_product);
            }

            JSONObject json = new JSONObject();
            json.put("date",        date);
            json.put("number",      number);
            json.put("uid",         uid);
            json.put("products",    json_products);
            return json;
        }

        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return  new JSONObject();
    }
}
