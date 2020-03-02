package com.account.sunstones.sunstones_purchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Order {

    public class Order_products{
        public Product product;
        public Double price;
        public int quantity;
        public int quantity_assembly;
        public Boolean set_aside;
        public Boolean out_of_stock;
        public Order_products(){}
    }

    public int id;
    public long date;
    public String uid;
    public int number;
    public Boolean sync = false;
    public ArrayList<Order_products> products = new ArrayList<>();
    public int total_quantity_products = 0;
    public Double sum = 0.0;

    public Order(){ }

    public void addProduct(Product product, Double price, int quantity, int quantity_assembly, Boolean set_aside, Boolean out_of_stock){

        Order_products pp = new Order_products();
        pp.price                = price;
        pp.quantity             = quantity;
        pp.quantity_assembly    = quantity_assembly;
        pp.product              = product;
        pp.set_aside            = set_aside;
        pp.out_of_stock         = out_of_stock;
        products.add(pp);

    }

    public int getTotalQuantityProducts(){

        int t = 0;
        for (Order_products p : products) {
            t += p.quantity;
        }

        return t;
    }

    public int getTotalQuantityOrder(){

        int t = 0;
        for (Order_products p : products) {
            t += p.quantity;
        }

        return t;
    }

    public int getTotalQuantityAssembly(){

        int t = 0;
        for (Order_products p : products) {
            t += p.quantity_assembly;
        }

        return t;
    }

    public int getTotalQuantitySetAside(){

        int t = 0;
        for (Order_products p : products) {
            if(p.set_aside){
              t++;
            }
        }

        return t;
    }

    public int getTotalQuantityOutOfStock(){

        int t = 0;
        for (Order_products p : products) {
            if(p.out_of_stock){
                t++;
            }
        }

        return t;
    }

    public int getIndexProduct(int vendor_code){

        for (int i = 0; i < products.size(); i++) {

            if(products.get(i).product.vendor_code == vendor_code){
                return i;
            }

        }

        return 0;
    }

    public JSONObject get_json(){

        try {

            JSONArray json_products = new JSONArray();
            for (int i = 0; i < products.size(); i++)
            {
                Order.Order_products item = products.get(i);

                    JSONObject json_product = new JSONObject();
                    json_product.put("product_uid",         item.product.uid);
                    json_product.put("product_vendor_code", item.product.vendor_code);
                    json_product.put("price",               item.price);
                    json_product.put("quantity_order",      item.quantity);
                    json_product.put("quantity_assembly",   item.quantity_assembly);
                    json_product.put("out_of_stock",        item.out_of_stock);
                    json_product.put("set_aside",           item.set_aside);

                    json_products.put(json_product);
            }

            JSONObject json = new JSONObject();
            json.put("date",        date);
            json.put("number",      number);
            json.put("order_uid",   uid);
            json.put("products",    json_products);
            return json;
        }

        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return  new JSONObject();
    }


}
