package com.account.sunstones.sunstones_purchase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBase extends SQLiteOpenHelper {

    private static DataBase sInstance;
    private static final int DATABASE_VERSION            = 53;
    private static final String DATABASE_NAME            = "sunstones_purchase";
    private static final String TABLE_PHOTOS             = "photos";
    private static final String TABLE_PHOTO_PENCIL       = "photo_pencil";
    private static final String TABLE_PHOTO_PRODUCT      = "photo_product";
    private static final String TABLE_PRODUCTS           = "products";
    private static final String TABLE_OFFERS             = "offers";
    private static final String TABLE_OFFER_PRODUCTS     = "offer_products";
    private static final String TABLE_OFFER_PHOTOS       = "offer_photos";
    private static final String TABLE_ORDERS             = "orders";
    private static final String TABLE_ORDER_PRODUCTS     = "order_products";
    private static final String TABLE_ASSEMBLIES         = "assemblies";
    private static final String TABLE_ASSEMBLY_PRODUCTS  = "assembly_products";
    private static final String TABLE_SESSION            = "session_values";
    private static final String TABLE_PRODUCT_TYPE       = "product_type";
    private static final String TABLE_PRODUCT_CATALOG    = "product_catalog";

    SQLiteDatabase db;

    public static DataBase getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DataBase(context);
        }
        return sInstance;

    }

    private DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createTable(db, TABLE_SESSION);
        createTable(db, TABLE_PRODUCT_CATALOG);
        createTable(db, TABLE_PRODUCT_TYPE);
        createTable(db, TABLE_PHOTOS);
        createTable(db, TABLE_PHOTO_PENCIL);
        createTable(db, TABLE_PHOTO_PRODUCT);
        createTable(db, TABLE_PRODUCTS);
        createTable(db, TABLE_OFFERS);
        createTable(db, TABLE_OFFER_PRODUCTS);
        createTable(db, TABLE_OFFER_PHOTOS);
        createTable(db, TABLE_ORDERS);
        createTable(db, TABLE_ORDER_PRODUCTS);
        createTable(db, TABLE_ASSEMBLIES);
        createTable(db, TABLE_ASSEMBLY_PRODUCTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {


//            db.deleteDatabase(DATABASE_NAME);


            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFER_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFER_PHOTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSEMBLY_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSEMBLIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO_PENCIL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO_PRODUCT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
            onCreate(db);
        }

    }

    private void createTable(SQLiteDatabase db, String table_name){


        switch(table_name) {

            case TABLE_PRODUCT_TYPE:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_TYPE + " ("
                        + "id integer primary key autoincrement,"
                        + "name_en text,"
                        + "name_chn text"
                        + ");");

                break;

            case TABLE_PRODUCT_CATALOG:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_CATALOG + " ("
                        + "id integer primary key autoincrement,"
                        + "name_en text,"
                        + "name_chn text"
                        + ");");

                break;

            case TABLE_PRODUCTS:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " ("
                        + "id integer primary key autoincrement,"
                        + "uid TEXT,"
                        + "vendor_code INTEGER NOT NULL,"
                        + "out_of_stock INTEGER,"
                        + "sync INTEGER,"
                        + "type_id INTEGER NOT NULL,"
                        + "catalog_id INTEGER NOT NULL,"
                        + "FOREIGN KEY (type_id) REFERENCES "+TABLE_PRODUCT_TYPE+"(id),"
                        + "FOREIGN KEY (catalog_id) REFERENCES "+TABLE_PRODUCT_CATALOG+"(id)"
                        + ");");
                break;

            case TABLE_PHOTOS:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PHOTOS + " ("
                        + "id integer primary key autoincrement,"
                        + "uid TEXT,"
                        + "original TEXT,"
                        + "modified TEXT,"
                        + "sync INTEGER"
                        + ");");
                break;


            case TABLE_PHOTO_PENCIL:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PHOTO_PENCIL + " ("
                        + "id integer primary key autoincrement,"
                        + "photo_id INTEGER  NOT NULL,"
                        + "number_line INTEGER  NOT NULL,"
                        + "x REAL,"
                        + "y REAL,"
                        + "FOREIGN KEY (photo_id) REFERENCES "+TABLE_PHOTOS+"(id)"
                        + ");");
                break;

            case TABLE_PHOTO_PRODUCT:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PHOTO_PRODUCT + " ("
                        + "id integer primary key autoincrement,"
                        + "photo_id INTEGER  NOT NULL,"
                        + "product_id INTEGER  NOT NULL,"
                        + "x REAL,"
                        + "y REAL,"
                        + "price REAL ,"
                        + "quantity INTEGER ,"
                        + "coment TEXT ,"
                        + "FOREIGN KEY (photo_id) REFERENCES "+TABLE_PHOTOS+"(id),"
                        + "FOREIGN KEY (product_id) REFERENCES "+TABLE_PRODUCTS+"(id)"
                        + ");");
                break;


            case TABLE_OFFERS:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_OFFERS + " ("
                        + "id integer primary key autoincrement,"
                        + "number INTEGER NOT NULL,"
                        + "uid TEXT,"
                        + "date LONG,"
                        + "sync INTEGER"
                        + ");");
                break;

            case TABLE_OFFER_PHOTOS:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_OFFER_PHOTOS + " ("
                        + "id integer primary key autoincrement,"
                        + "offer_id INTEGER,"
                        + "photo_id INTEGER,"
                        + "FOREIGN KEY (offer_id) REFERENCES "+TABLE_OFFERS+"(id),"
                        + "FOREIGN KEY (photo_id) REFERENCES "+TABLE_PHOTOS+"(id)"
                        + ");");
                break;

            case TABLE_OFFER_PRODUCTS:
//                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_OFFER_PRODUCTS + " ("
//                        + "id integer primary key autoincrement,"
//                        + "offer_id INTEGER NOT NULL,"
//                        + "product_id INTEGER NOT NULL,"
//                        + "price REAL ,"
//                        + "quantity INTEGER ,"
//                        + "FOREIGN KEY (offer_id) REFERENCES "+TABLE_OFFERS+"(id),"
//                        + "FOREIGN KEY (product_id) REFERENCES "+TABLE_PRODUCTS+"(id)"
//                        + ");");
                break;


            case TABLE_ORDERS:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + " ("
                        + "id integer primary key autoincrement,"
                        + "uid TEXT,"
                        + "date LONG,"
                        + "sync INTEGER"
                        + ");");
                break;


            case TABLE_ORDER_PRODUCTS:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_PRODUCTS + " ("
                        + "id integer primary key autoincrement,"
                        + "order_id INTEGER NOT NULL,"
                        + "product_id INTEGER NOT NULL,"
                        + "price REAL ,"
                        + "quantity INTEGER ,"
                        + "set_aside INTEGER, "
                        + "out_of_stock INTEGER, "
                        + "FOREIGN KEY (order_id) REFERENCES "+TABLE_ORDERS+"(id),"
                        + "FOREIGN KEY (product_id) REFERENCES "+TABLE_PRODUCTS+"(id)"
                        + ");");
                break;



            case TABLE_ASSEMBLIES:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ASSEMBLIES + " ("
                        + "id integer primary key autoincrement,"
                        + "uid TEXT,"
                        + "number INTEGER NOT NULL,"
                        + "date LONG,"
                        + "sync INTEGER"
                        + ");");
                break;


            case TABLE_ASSEMBLY_PRODUCTS:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ASSEMBLY_PRODUCTS + " ("
                        + "id integer primary key autoincrement,"
                        + "assembly_id INTEGER NOT NULL,"
                        + "order_products_id INTEGER NOT NULL,"
                        + "price REAL ,"
                        + "quantity INTEGER ,"
                        + "FOREIGN KEY (assembly_id) REFERENCES "+TABLE_ASSEMBLIES+"(id),"
                        + "FOREIGN KEY (order_products_id) REFERENCES "+TABLE_ORDER_PRODUCTS+"(id)"
                        + ");");

                break;

            case TABLE_SESSION:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SESSION + " ("
                        + "id integer primary key autoincrement,"
                        + "name text,"
                        + "value text"
                        + ");");

                break;





            default:
                // code block
        }


    }







    public int getLastProductVendorCode(){

//        SQLiteDatabase db = getWritableDatabase();

        int LastVendorCode = 0;
        String query = "SELECT Max(vendor_code) FROM " + TABLE_PRODUCTS;
        Cursor cur = db.rawQuery(query, null);

        if (cur.moveToFirst()){
            LastVendorCode = cur.getInt(0);
        }

        if(LastVendorCode == 0)
            LastVendorCode = 1000;

//        db.close();
        return LastVendorCode;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //ORDER

    public ArrayList<Order> getListOrdersWithEmptyProducts(){

        String query = "SELECT orders.id, orders.date, orders.sync, sum(order_products.quantity), sum(order_products.quantity * order_products.price)  FROM "
                + TABLE_ORDERS + " AS orders"
                + " LEFT JOIN "
                + TABLE_ORDER_PRODUCTS + " AS order_products ON order_products.order_id = orders.id"
                + " GROUP BY orders.id, orders.date, orders.sync";

        Cursor c = db.rawQuery(query, null);
        ArrayList<Order> list = new ArrayList<>();

        while (c.moveToNext()) {
            Order order = new Order();
            order.id                        = c.getInt(0);
            order.number                    = 0;
            order.date                      = c.getInt(1);
            order.sync                      = (c.getInt(2)==1);
            order.total_quantity_products   = c.getInt(3);
            order.sum                       = c.getDouble(4);

            list.add(order);
        }


        return list;

    }

    public void addOrder(Order order){

        if(order == null) {
           return;
        }

        ContentValues cv = new ContentValues();
        cv.put("date",      order.date);
        cv.put("id",        order.id);
        cv.put("uid",       order.uid);
        cv.put("sync",      (order.sync == true ? 1 : 0));

        Cursor userCursor = db.rawQuery("select * from " + TABLE_ORDERS + " where  uid =?", new String[]{String.valueOf(order.uid)});
        if (!order.uid.isEmpty() && userCursor.moveToFirst()) {
            db.update(TABLE_ORDERS, cv, "uid=?", new String[]{String.valueOf(order.uid)});
            order.id = (order.id ==0 ? getIdOrderByUID(order.uid) : order.id);
        } else {
            order.id  = (int)db.insert(TABLE_ORDERS, null, cv);
        }


        for (int j = 0; j < order.products.size(); j++)
        {
            Order.Order_products item = order.products.get(j);

            ContentValues cc = new ContentValues();
            cc.put("order_id",          order.id);
            cc.put("product_id",        item.product.id);
            cc.put("price",             item.price);
            cc.put("quantity",          item.quantity);
//            cc.put("quantity_assembly", item.quantity_assembly);
            cc.put("set_aside",         (item.set_aside == true ? 1 : 0));
            cc.put("out_of_stock",      (item.out_of_stock == true ? 1 : 0));


                Cursor c = db.rawQuery("select * from " + TABLE_ORDER_PRODUCTS + " where  product_id =?  AND order_id=?", new String[]{String.valueOf(item.product.id), String.valueOf(order.id)});
                if (c.moveToFirst()) {
                    db.update(TABLE_ORDER_PRODUCTS, cc, "product_id=" + String.valueOf(item.product.id) + " AND order_id=" + String.valueOf(order.id), null);
                } else {
                    db.insert(TABLE_ORDER_PRODUCTS, null, cc);
                }

        }

    }

    private int getIdOrderByUID(String uid){

        String query = "SELECT orders.id  FROM "
                + TABLE_ORDERS +" AS orders  "
                + " WHERE orders.uid =?"
                + "";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(uid)});


        if (c.moveToFirst()){
            return c.getInt(0);
        }

        return 0;

    }

    public Product getProduct(String uid){

        String query = "SELECT products.id, products.uid, photo_product.photo_id, products.vendor_code, photo_product.x, photo_product.y, products.sync, photo_product.coment FROM "
                + TABLE_PRODUCTS +" AS products  "
                + " LEFT JOIN "
                +  TABLE_PHOTO_PRODUCT + " AS photo_product ON products.id = photo_product.product_id"
                + " WHERE products.uid =?"
                + "";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(uid)});
        ArrayList<Photo> list = new ArrayList<>();

        int counter = 1;

        while (c.moveToNext()) {
            int product_id = c.getInt(0);
            if (product_id > 0) {

                Product p = new Product();
                p.id            = c.getInt(0);
                p.uid           = c.getString(1);
                p.photo_id      = c.getInt(2);
                p.vendor_code   = c.getInt(3);
                p.x             = c.getFloat(4);
                p.y             = c.getFloat(5);
                p.sync          = (c.getInt(6) == 1);
                p.coment        = c.getString(7);

                return p;
            }
        }

        return null;
    }

    public Order getOrder(int order_id){

        String query = "SELECT orders.id, orders.uid, orders.date, orders.sync  FROM "
                + TABLE_ORDERS +" AS orders  "
                + " WHERE orders.id =?"
                + "";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(order_id)});


        if (c.moveToFirst()){
            Order order = new Order();
            order.id        = c.getInt(0);
            order.uid       = c.getString(1);
            order.date      = c.getLong(2);;
            order.sync      = (c.getInt(3)==1);
//            offer.number    = c.getInt(3);

            getOrder_products(order);

            return order;

        }

        return null;
    }

    private void getOrder_products(Order order){

        order.products.clear();

        String query = "SELECT order_products.product_id, products.uid, products.vendor_code, products.sync, photo_product.photo_id ,photo_product.x , photo_product.y ,order_products.price, order_products.quantity, 0, order_products.set_aside, order_products.out_of_stock, photo_product.coment, assembly_products.quantity  FROM "
                + TABLE_ORDER_PRODUCTS +" AS order_products  "
                + " LEFT JOIN "
                +  TABLE_PRODUCTS+ " AS products ON products.id = order_products.product_id"
                + " LEFT JOIN "
                +  TABLE_PHOTO_PRODUCT+ " AS photo_product ON photo_product.product_id = order_products.product_id"
                + " LEFT JOIN "
                +  TABLE_ASSEMBLY_PRODUCTS+ " AS assembly_products ON assembly_products.order_products_id = order_products.id"
                + " WHERE order_products.order_id =?"
                + "";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(order.id)});

        while (c.moveToNext()) {
            int product_id = c.getInt(0);
            if (product_id > 0) {

                Product p = new Product();
                p.id            = c.getInt(0);
                p.uid           = c.getString(1);
                p.vendor_code   = c.getInt(2);
                p.sync          = (c.getInt(3) == 1);
                p.photo_id      = c.getInt(4);
                p.x             = c.getFloat(5);
                p.y             = c.getFloat(6);
                p.coment        = c.getString(12);

                Double price            = c.getDouble(7);
                int quantity            = c.getInt(8);
                int quantity_assembly   = c.getInt(13);
                Boolean set_aside       = (c.getInt(10)==1);
                Boolean out_of_stock    = (c.getInt(11)==1);

                order.addProduct(p, price, quantity, quantity_assembly, set_aside, out_of_stock);

            }
        }


    }


    ///////////////////////////////////////////////////////////////////////////////////
    //OFFER

    public Bitmap ByteArrayToBitmap(byte[] byteArray) {
        if(byteArray == null){
            return null;
        }

        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    public int getLastNumberOffer(){


//        SQLiteDatabase db = getWritableDatabase();

        String qu = "SELECT number FROM " + TABLE_OFFERS + " ORDER BY number DESC" ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            return cur.getInt(0);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

//        db.close();
        return 1000;
    }

    public ArrayList<Offer> getListOffersWithEmptyPhotos(){

//        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("SELECT id, number, date, sync  FROM " + TABLE_OFFERS, null);
        ArrayList<Offer> list = new ArrayList<>();

        while (c.moveToNext()) {
            int offer_id = c.getInt(0);
            Offer offer = getOfferWithEmptyPhotos(offer_id);
            list.add(offer);
        }

//        db.close();
        return list;

    }

    public Offer getOfferWithEmptyPhotos(int id){

//        SQLiteDatabase db = getWritableDatabase();

        Offer offer = new Offer();
        offer.id    = id;

        String query = "SELECT " +
                "offers.id, "                   +
                "offers.date, "                 +
                "offers.sync, "                 +
                "offers.number "                +
                "FROM " +TABLE_OFFERS +" AS offers "
               + " WHERE offers.id =?";

        Cursor c2 = db.rawQuery(query, new String[]{String.valueOf(offer.id)});
        if (c2.moveToFirst()){
            offer.id        = c2.getInt(0);
            offer.date      = c2.getLong(1);;
            offer.sync      = (c2.getInt(2) == 1);
            offer.number    = c2.getInt(3);
        }





        String query3 = "SELECT COUNT(photo_products.product_id)  "
                + " FROM " +TABLE_OFFER_PHOTOS +" AS offer_pfoto "
                + " LEFT JOIN "
                + TABLE_PHOTO_PRODUCT + " AS photo_products ON photo_products.photo_id = offer_pfoto.photo_id"
                + " WHERE offer_pfoto.offer_id =?";

        Cursor c3 = db.rawQuery(query3, new String[]{String.valueOf(offer.id)});
        if (c3.moveToFirst()){
            offer.total_quantity_products = c3.getInt(0);
        }

//        db.close();


        return  offer;

    }

    public void deleteOffer(int offer_id){
        db.delete(TABLE_OFFER_PHOTOS,  "offer_id=" + offer_id, null);
        db.delete(TABLE_OFFERS,  "id=" + offer_id, null);
    }



    public Offer getOffer(int id){

//        SQLiteDatabase db = getWritableDatabase();

        Offer offer = new Offer();
        offer.id    = id;

        String query = "SELECT " +
                "offers.id, "                   +
                "offers.date, "                 +
                "offers.sync, "                 +
                "offers.number, "                +
                "offers.uid "                +
                "FROM " +TABLE_OFFERS +" AS offers "
                + " WHERE offers.id =?";

        Cursor c2 = db.rawQuery(query, new String[]{String.valueOf(offer.id)});
        if (c2.moveToFirst()){
            offer.id        = c2.getInt(0);
            offer.date      = c2.getLong(1);;
            offer.sync      = (c2.getInt(2)==1);
            offer.number    = c2.getInt(3);
            offer.uid       = c2.getString(4);
        }

//        db.close();

        ////////////////////////////////////
        offer.Photos =  getOffer_Photos(offer.id);

        return  offer;

    }

    private ArrayList<Photo> getOffer_Photos(int offer_id){

//        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT photos.id, photos.original, photos.modified, offer_photo.offer_id, photos.uid, photos.sync FROM "
                + TABLE_OFFER_PHOTOS +" AS offer_photo "
                + " LEFT JOIN "
                + TABLE_PHOTOS + " AS photos ON photos.id = offer_photo.photo_id"
                + " WHERE offer_photo.offer_id =?"
                + "GROUP BY photos.id, photos.original, photos.modified, offer_photo.offer_id, photos.uid";


        Cursor c = db.rawQuery(query, new String[]{String.valueOf(offer_id)});

        ArrayList<Photo> list = new ArrayList<>();

        int counter = 1;

        while (c.moveToNext()) {

            int photo_id = c.getInt(0);
            if(photo_id > 0){

                Photo item = new Photo();
                item.photo_id        = c.getInt(0);
                item.offer_id        = offer_id;
//                item.photo_original  = ByteArrayToBitmap(c.getBlob(1));
//                item.photo_modified  = ByteArrayToBitmap(c.getBlob(2));
                item.photo_original         = getBitmapFromPath(c.getString(1));
                item.photo_modified         = getBitmapFromPath(c.getString(2));
                item.path_photo_original    = c.getString(1);
                item.path_photo_modified    = c.getString(2);

                item.photo_uid       = c.getString(4);
                item.photo_sync      = (c.getInt(5) == 1);
                item.title           = " "; //+ String.valueOf(c.getInt(3));;

                item.products.clear();
                getOffer_Products(item);
                getOffer_PencilPaths(item);
                list.add(item);
            }

        }

//        db.close();
        return list;

    }

    private void getOffer_Products(Photo item){

//        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT products.id, products.vendor_code, photo_product.x, photo_product.y, photo_product.price, photo_product.quantity, products.sync, products.uid , photo_product.coment FROM "
                + TABLE_PHOTO_PRODUCT +" AS photo_product "
                + " LEFT JOIN "
                + TABLE_PRODUCTS + " AS products ON products.id = photo_product.product_id"
                + " WHERE photo_product.photo_id =?"
                + "";


        Cursor c = db.rawQuery(query, new String[]{String.valueOf(item.photo_id)});
        ArrayList<Photo> list = new ArrayList<>();

        int counter = 1;

        while (c.moveToNext()) {

            int product_id = c.getInt(0);
            if(product_id > 0){

                Product p = new Product();
                p.id            = product_id;
                p.photo_id      = item.photo_id;
                p.photo_uid     = item.photo_uid;
                p.vendor_code   = c.getInt(1);
                p.x             = c.getFloat(2);
                p.y             = c.getFloat(3);


                Double price = c.getDouble(4);
                int quantity = c.getInt(5);

                p.sync          = (c.getInt(6)==1);
                p.uid           = c.getString(7);
                p.coment        = c.getString(8);

                item.addProduct(p, price, quantity);

            }

        }

//        db.close();

    }

    private void getOffer_PencilPaths(Photo item){

//        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT photo_pencil.number_line, photo_pencil.x, photo_pencil.y  FROM "
                + TABLE_PHOTO_PENCIL +" AS photo_pencil "
                + " WHERE photo_pencil.photo_id =?"
                + "";


        Cursor c = db.rawQuery(query, new String[]{String.valueOf(item.photo_id)});

        FingerPath fp           = new FingerPath();
        Path path               = new Path();
        float lastPencilX       = 0;
        float lastPencilY       = 0;
        int last_namber_line    = 0;
        int step                = 0;
        while (c.moveToNext()) {

            int number_line = c.getInt(0);
            float x         = c.getFloat(1);
            float y         = c.getFloat(2);


            if(step == 0){
                path = new Path();
                path.reset();
                path.moveTo(x, y);
                fp = new FingerPath();
                fp.setPath(path);

                item.addFingerPath(fp);
            }

            //end
            if(step != 0 && last_namber_line != number_line){

                path.lineTo(lastPencilX, lastPencilY);



                path = new Path();
                path.reset();
                path.moveTo(x, y);
                fp = new FingerPath();
                fp.setPath(path);
                item.addFingerPath(fp);

            }

            if(last_namber_line == number_line){

                float X1 = lastPencilX;
                float Y1 = lastPencilY;
                float X2 = (lastPencilX + x) / 2;
                float Y2 = (lastPencilY + y) / 2;
                path.quadTo(X1, Y1, X2, Y2);

            }

            fp.addPathPoint(x, y);


            last_namber_line = number_line;
            lastPencilX = x;
            lastPencilY = y;
            step++;
        }


//        db.close();
    }


    public void saveOffer(Offer offer){
        addOffer(offer);
        saveOffer_Photos(offer);
        saveOffer_OfferPhotos(offer);
        saveOffer_Products(offer);
        saveOffer_PhotoProduct(offer);
        saveOffer_PhotoPencil(offer);
    }

    public Offer addOffer(Offer offer){

//        SQLiteDatabase db = getWritableDatabase();

        if(offer == null) {
            offer = new Offer();
            offer.sync      = false;
            offer.date      = System.currentTimeMillis() / 1000L;
            offer.number    = getLastNumberOffer() + 1;
        }

        ContentValues cv = new ContentValues();
        cv.put("date",      offer.date);
        cv.put("number",    offer.number);
        cv.put("uid",       offer.uid);
        cv.put("sync",      (offer.sync==true ? 1 : 0));

        Cursor userCursor = db.rawQuery("select * from " + TABLE_OFFERS + " where  id =?", new String[]{String.valueOf(offer.id)});
        if (offer.id > 0 && userCursor.moveToFirst()) {
            db.update(TABLE_OFFERS, cv, "id=" + String.valueOf(offer.id), null);
        } else {
            offer.id  = (int)db.insert(TABLE_OFFERS, null, cv);
        }

//        db.close();
        return offer;
    }

    private void saveOffer_Products(Offer offer){

        for (int i = 0; i < offer.Photos.size(); i++)
        {
            Photo photo_item =  offer.Photos.get(i);

            for (int j = 0; j < photo_item.products.size(); j++) {
                Product product = photo_item.products.get(j).product;
                addProduct(product);
            }
        }

    }

    private void saveOffer_Photos(Offer offer){

        for (int i = 0; i < offer.Photos.size(); i++)
        {
            Photo item = offer.Photos.get(i);
            item.offer_id  = offer.id;
            addPhoto(item);
        }
    }

    private void saveOffer_PhotoProduct(Offer offer){

        for (int j = 0; j < offer.Photos.size(); j++)
        {
            Photo item = offer.Photos.get(j);

            for (int i = 0; i < item.products.size(); i++) {
                Product product = item.products.get(i).product;
                int product_id = product.id;
                int photo_id   = item.photo_id;

                ContentValues cv = new ContentValues();
                cv.put("photo_id",      photo_id);
                cv.put("product_id",    product_id);
                cv.put("x",             product.x);
                cv.put("y",             product.y);
                cv.put("price",         item.products.get(i).price);
                cv.put("quantity",      item.products.get(i).quantity);
                cv.put("coment",        item.products.get(i).product.coment);

                Cursor userCursor = db.rawQuery("select * from " + TABLE_PHOTO_PRODUCT + " where  product_id =?  AND photo_id=?", new String[]{String.valueOf(product_id), String.valueOf(photo_id)});
                if (userCursor.moveToFirst()) {
                    db.update(TABLE_PHOTO_PRODUCT, cv, "product_id=" + String.valueOf(product_id) + " AND photo_id=" + String.valueOf(photo_id), null);
                } else {
                    db.insert(TABLE_PHOTO_PRODUCT, null, cv);
                }
            }
        }

    }

    private void saveOffer_OfferPhotos(Offer offer){

//        SQLiteDatabase db = getWritableDatabase();

//        db.deletedb.delete(TABLE_OFFER_PHOTOS, "offer_id=?", new String[]{ String.valueOf(offer.id)}) ;

        for (int i = 0; i < offer.Photos.size(); i++)
        {

            int offer_id = offer.id;
            int photo_id = offer.Photos.get(i).photo_id;

            ContentValues cv = new ContentValues();
            cv.put("offer_id", offer_id);
            cv.put("photo_id", photo_id);

            long iii = 0;

            Cursor userCursor = db.rawQuery("select * from " + TABLE_OFFER_PHOTOS + " where  offer_id =? AND photo_id=?", new String[]{String.valueOf(offer_id), String.valueOf(photo_id)});
            if (userCursor.moveToFirst()) {
//                db.update(TABLE_OFFER_PHOTOS, cv, "id=" + String.valueOf(photo_id), null);
            } else {
                iii = db.insert(TABLE_OFFER_PHOTOS, null, cv);
            }

        }



//        db.close();
    }

    private void saveOffer_PhotoPencil(Offer offer){

//        SQLiteDatabase db = getWritableDatabase();

        for (int i = 0; i < offer.Photos.size(); i++)
        {

            Photo item = offer.Photos.get(i);
            int photo_id = item.photo_id;



            db.delete(TABLE_PHOTO_PENCIL, "photo_id=?", new String[]{ String.valueOf(photo_id)}) ;


            for (int j = 0; j < item.paths.size(); j++) {
                FingerPath fp = item.paths.get(j);

                for (int u = 0; u < fp.path_points.size(); u++) {

                    FingerPath.Point p = fp.path_points.get(u);

                    ContentValues cv = new ContentValues();
                    cv.put("photo_id", photo_id);
                    cv.put("number_line", j+1);
                    cv.put("x", p.x);
                    cv.put("y", p.y);

                    long iii = 0;
                    iii = db.insert(TABLE_PHOTO_PENCIL, null, cv);

//                    Log.d("TEST", "TABLE_PHOTO_PENCIL id=" + String.valueOf(iii) + " number_line="+String.valueOf(j) + " photo_id="+String.valueOf(photo_id)+ " x="+String.valueOf(p.x)+ " y="+String.valueOf(p.y));

                }
            }


        }

//        db.close();
    }


    ///////////////////////////////////////////////////////////////////////////////////
    //PHOTO

    public void addProduct(Product product){

        int id_product = product.id;

        ContentValues cv = new ContentValues();
        cv.put("vendor_code",   product.vendor_code);
        cv.put("uid",           product.uid);
        cv.put("type_id",       (product.type == null ? 0 : product.type.id));
        cv.put("catalog_id",    (product.catalog == null ? 0: product.catalog.id));
        cv.put("sync",          (product.sync==true ? 1 : 0) );
        cv.put("out_of_stock",  (product.out_of_stock==true ? 1 : 0) );

        Cursor userCursor = db.rawQuery("select * from " + TABLE_PRODUCTS + " where  id =?", new String[]{String.valueOf(id_product)});
        if (id_product > 0 && userCursor.moveToFirst()) {
            db.update(TABLE_PRODUCTS, cv, "id=" + String.valueOf(id_product), null);
        } else {
            id_product = (int) db.insert(TABLE_PRODUCTS, null, cv);
        }

        product.id = id_product;
    }

    public void addPhotoProduct(Product product){

        ContentValues cv = new ContentValues();
        cv = new ContentValues();
        cv.put("photo_id",   product.photo_id);
        cv.put("product_id", product.id);
        cv.put("x",          product.x);
        cv.put("y",          product.y);
        cv.put("price",      product.price);
        cv.put("quantity",   0);
        cv.put("coment",     product.coment);

        Cursor c = db.rawQuery("select * from " + TABLE_PHOTO_PRODUCT + " where  product_id =?  AND photo_id=?", new String[]{String.valueOf(product.id), String.valueOf(product.photo_id)});
        if (c.moveToFirst()) {
            db.update(TABLE_PHOTO_PRODUCT, cv, "product_id=" + String.valueOf(product.id) + " AND photo_id=" + String.valueOf(product.photo_id), null);
        } else {
            db.insert(TABLE_PHOTO_PRODUCT, null, cv);
        }
    }

    public void addPhoto(Photo photo){



        int photo_id                = photo.photo_id;
        Bitmap photo_original       = photo.photo_original;
        Bitmap photo_modified       = photo.photo_modified;
        String photo_uid            = photo.photo_uid;
        int photo_sync              = (photo.photo_sync == true ? 1 : 0);

//            if(photo_uid.isEmpty()){
//                photo_uid = UUID.randomUUID().toString();
//            }

        ContentValues cv = new ContentValues();
        cv.put("uid", photo_uid);
        cv.put("sync", photo_sync);
        cv.put("original", photo.path_photo_original);
        cv.put("modified", photo.path_photo_modified);

//        if(photo_original != null){
//            ByteArrayOutputStream bos   = new ByteArrayOutputStream();
//            photo_original.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            byte[] img_original = bos.toByteArray();
//            cv.put("original", img_original);
//        }
//
//        if(photo_modified != null){
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            photo_modified.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            byte[] img_modified = bos.toByteArray();
//            cv.put("modified", img_modified);
//        }

//        try {


            Cursor userCursor = db.rawQuery("select * from " + TABLE_PHOTOS + " where  id =?", new String[]{String.valueOf(photo_id)});
            if (photo_id > 0 && userCursor.moveToFirst()) {
                db.update(TABLE_PHOTOS, cv, "id=" + String.valueOf(photo_id), null);
            } else {
                photo_id = (int) db.insert(TABLE_PHOTOS, null, cv);
            }

//        }catch (SQLiteBlobTooBigException expected) {
//            Log.v("TEST", "addPhoto photo_id:"+photo_id);
//        }

        photo.photo_id  = photo_id;
        photo.offer_id  = photo.offer_id;
        photo.photo_uid = photo_uid;






//        for (int i = 0; i < photo.products.size(); i++) {
//            Product product = photo.products.get(i).product;
//            int product_id = product.id;
//
//            cv = new ContentValues();
//            cv.put("photo_id",  photo_id);
//            cv.put("product_id", product_id);
//            cv.put("x",         product.x);
//            cv.put("y",         product.y);
//            cv.put("price",     photo.products.get(i).price);
//            cv.put("quantity",  photo.products.get(i).quantity);
//
//            Cursor c = db.rawQuery("select * from " + TABLE_PHOTO_PRODUCT + " where  product_id =?  AND photo_id=?", new String[]{String.valueOf(product_id), String.valueOf(photo_id)});
//            if (c.moveToFirst()) {
//                db.update(TABLE_PHOTO_PRODUCT, cv, "product_id=" + String.valueOf(product_id) + " AND photo_id=" + String.valueOf(photo_id), null);
//            } else {
//                db.insert(TABLE_PHOTO_PRODUCT, null, cv);
//            }
//        }
    }

    public Photo getPhoto(String photo_uid){

        String query = "SELECT photos.id, photos.original, photos.modified, photos.uid, photos.sync FROM "
                + TABLE_PHOTOS + " AS photos "
                + " WHERE photos.uid =?";


        Cursor c = db.rawQuery(query, new String[]{String.valueOf(photo_uid)});
        ArrayList<Photo> list = new ArrayList<>();

        int counter = 1;

        if (c.moveToFirst()){

            int photo_id = c.getInt(0);
            if(photo_id > 0){

                Photo item = new Photo();
                item.photo_id        = c.getInt(0);
//                item.photo_original  = ByteArrayToBitmap(c.getBlob(1));
//                item.photo_modified  = ByteArrayToBitmap(c.getBlob(2));
                item.photo_original         = getBitmapFromPath(c.getString(1));
                item.photo_modified         = getBitmapFromPath(c.getString(2));
                item.path_photo_original    = c.getString(1);
                item.path_photo_modified    = c.getString(2);
                item.photo_uid              = c.getString(3);
                item.photo_sync             = (c.getInt(4) == 1);
                item.title                  = " "; //+ String.valueOf(c.getInt(3));;

                item.products.clear();
                getOffer_Products(item);
                getOffer_PencilPaths(item);

                return item;
            }

        }

//        db.close();
        return null;
    }

    public void deletePhoto(Photo photo){

        db.delete(TABLE_OFFER_PHOTOS,   "photo_id=" + photo.photo_id, null);
        db.delete(TABLE_PHOTO_PENCIL,   "photo_id=" + photo.photo_id, null);
        db.delete(TABLE_PHOTO_PRODUCT,  "photo_id=" + photo.photo_id, null);
        db.delete(TABLE_PHOTOS,         "id=" + photo.photo_id, null);

    }

    public Bitmap getPhotoOriginal(int photo_id){

//        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT photo.original FROM " +TABLE_PHOTOS +" AS photo  WHERE photo.id =?";
        Cursor cur = db.rawQuery(query, new String[]{String.valueOf(photo_id)});

        if (cur.moveToFirst()){
//            return ByteArrayToBitmap(cur.getBlob(0));
            return getBitmapFromPath(cur.getString(0));
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

//        db.close();
        return null;
    }

    public Bitmap getPhotoModified(int photo_id){

//        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT photo.modified FROM " +TABLE_PHOTOS +" AS photo  WHERE photo.id =?";
        Cursor cur = db.rawQuery(query, new String[]{String.valueOf(photo_id)});

        if (cur.moveToFirst()){
            return getBitmapFromPath(cur.getString(0));
//            return ByteArrayToBitmap(cur.getBlob(0));
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

//        db.close();
        return null;
    }

    public Bitmap getBitmapFromPath(String full_path){

        if(full_path == null || full_path.isEmpty()){
            return null;
        }

        Bitmap bitmap = null;
        File imgFile = new  File(full_path);
        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }

        return bitmap;
    }




    public ArrayList<Photo> getPhotosNotUploaded(){

        ArrayList<Photo> list = new ArrayList();

        String query = "SELECT id, uid, original, modified  FROM " + TABLE_PHOTOS + " WHERE sync IS NULL or sync=0 ";
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {

            Photo photo = new Photo();
            photo.photo_id        = c.getInt(0);
            photo.photo_uid       = c.getString(1);
//            photo.photo_original  = ByteArrayToBitmap(c.getBlob(2));
//            photo.photo_modified  = ByteArrayToBitmap(c.getBlob(3));
            photo.photo_original         = getBitmapFromPath(c.getString(2));
            photo.photo_modified         = getBitmapFromPath(c.getString(3));
            photo.path_photo_original    = c.getString(2);
            photo.path_photo_modified    = c.getString(3);
            photo.title           = " ";
            photo.photo_sync      = false;

            list.add(photo);
        }

//        db.close();
        return list;

    }

    public ArrayList<Product> getProductsNotUploaded(){

        ArrayList<Product> list = new ArrayList();

        String query = "SELECT products.id, products.uid, products.vendor_code, photos.uid, photo_product.x, photo_product.y, photo_product.coment, products.out_of_stock  FROM "
                + TABLE_PRODUCTS + " AS products "
                + " LEFT JOIN " + TABLE_PHOTO_PRODUCT + " AS photo_product ON products.id = photo_product.product_id"
                + " LEFT JOIN " + TABLE_PHOTOS + " AS photos ON photos.id = photo_product.photo_id"
                + " WHERE products.sync IS NULL or products.sync=0 ";

        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {

            Product product = new Product();
            product.id              = c.getInt(0);
            product.uid             = c.getString(1);
            product.vendor_code     = c.getInt(2);
            product.photo_uid       = c.getString(3);
            product.x               = c.getFloat(4);
            product.y               = c.getFloat(5);
            product.sync            = false;
            product.coment          = c.getString(6);
            product.out_of_stock    = (c.getInt(7)==1);

            list.add(product);
        }

        return list;

    }

    public ArrayList<Offer> getOffersNotUploaded(){

        ArrayList<Offer> list = new ArrayList();
        String query          = "SELECT id FROM " + TABLE_OFFERS + " WHERE sync IS NULL or sync=0 ";
        Cursor c              = db.rawQuery(query, null);

        while (c.moveToNext()) {
            Offer offer = getOffer(c.getInt(0));
            list.add(offer);
        }

        return list;
    }

    public ArrayList<Assembly> getAssembliesNotUploaded(){

        ArrayList<Assembly> list = new ArrayList();
        String query          = "SELECT id FROM " + TABLE_ASSEMBLIES + " WHERE sync IS NULL or sync=0 ";
        Cursor c              = db.rawQuery(query, null);

        while (c.moveToNext()) {
            Assembly assembly = getAssembly(c.getInt(0), false);
            list.add(assembly);
        }

        return list;
    }

    public ArrayList<Order> getOrdersNotUploaded(){

        ArrayList<Order> list = new ArrayList();
        String query          = "SELECT id FROM " + TABLE_ORDERS + " WHERE sync IS NULL or sync=0 ";
        Cursor c              = db.rawQuery(query, null);

        while (c.moveToNext()) {
            Order order = getOrder(c.getInt(0));
            list.add(order);
        }

        return list;
    }




    private File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp        = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName    = "SUNSTONES_" + timeStamp + "_";
        File storageDir         = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image              = File.createTempFile("SUNSTONES", ".jpg", storageDir);

        return image;
    }

    public File saveBitmapToFile(Context context, Bitmap bitmap) {

        File newFile = null;
        try {
            newFile = createImageFile(context);
            FileOutputStream out = new FileOutputStream(newFile.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }catch(IOException e){

        }

        return newFile;
    }






    public void saveAssembly(Assembly assembly){

        assembly.sync = false;
        addAssembly(assembly);

        for (int j = 0; j < assembly.products_for_assembly.size(); j++)
        {
            Assembly.Assembly_products_for_assembly pp = assembly.products_for_assembly.get(j);

            if(pp.changed && !pp.out_of_stock && !pp.set_aside && pp.quantity_assembly > 0) {

                ContentValues cv = new ContentValues();
                cv.put("assembly_id", assembly.id);
                cv.put("order_products_id", pp.order_products_id);
                cv.put("price", pp.price);
                cv.put("quantity", pp.quantity_assembly);

                Cursor userCursor = db.rawQuery("select * from " + TABLE_ASSEMBLY_PRODUCTS + " where  assembly_id =?  AND order_products_id=?", new String[]{String.valueOf(assembly.id), String.valueOf(pp.order_products_id)});
                if (userCursor.moveToFirst()) {
                    db.update(TABLE_ASSEMBLY_PRODUCTS, cv, "assembly_id=" + String.valueOf(assembly.id) + " AND order_products_id=" + String.valueOf(pp.order_products_id), null);
                } else {
                    db.insert(TABLE_ASSEMBLY_PRODUCTS, null, cv);
                }


            }

            if(pp.changed ) {
                ContentValues cc = new ContentValues();
                cc.put("order_id",          pp.order_id);
                cc.put("product_id",        pp.product.id);
//                cc.put("price",             pp.price);
//                cc.put("quantity",          pp.quantity_order);
                cc.put("set_aside",         (pp.set_aside == true ? 1 : 0));
                cc.put("out_of_stock",      (pp.out_of_stock == true ? 1 : 0));

                Cursor c = db.rawQuery("select * from " + TABLE_ORDER_PRODUCTS + " where  product_id =?  AND order_id=?", new String[]{String.valueOf(pp.product.id), String.valueOf( pp.order_id)});
                if (c.moveToFirst()) {
                    db.update(TABLE_ORDER_PRODUCTS, cc, "product_id=" + String.valueOf(pp.product.id) + " AND order_id=" + String.valueOf( pp.order_id), null);
                } else {
//                    db.insert(TABLE_ORDER_PRODUCTS, null, cc);
                }


            }

            if(pp.out_of_stock){

                Product product = pp.product;
                product.out_of_stock = true;
                product.sync = false;
                addProduct(product);
            }

        }

    }

    public Assembly addAssembly(Assembly assembly){


        if(assembly == null) {
            assembly = new Assembly();
            assembly.sync      = false;
            assembly.date      = System.currentTimeMillis() / 1000L;
            assembly.number    = getLastNumberAssembly() + 1;
        }

        ContentValues cv = new ContentValues();
        cv.put("date",      assembly.date);
        cv.put("number",    assembly.number);
        cv.put("uid",       assembly.uid);
        cv.put("sync",      (assembly.sync==true ? 1 : 0));

        Cursor userCursor = db.rawQuery("select * from " + TABLE_ASSEMBLIES + " where  id =?", new String[]{String.valueOf(assembly.id)});
        if (assembly.id > 0 && userCursor.moveToFirst()) {
            db.update(TABLE_ASSEMBLIES, cv, "id=" + String.valueOf(assembly.id), null);
        } else {
            assembly.id  = (int)db.insert(TABLE_ASSEMBLIES, null, cv);
        }

//        db.close();
        return assembly;
    }

    public int getLastNumberAssembly(){

        String qu = "SELECT number FROM " + TABLE_ASSEMBLIES + " ORDER BY number DESC" ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            return cur.getInt(0);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return 1000;
    }

    public ArrayList<Assembly> getListAssemblies(){


        String query = "SELECT assemblies.id, assemblies.number, assemblies.date, assemblies.sync, sum(assembly_products.quantity), sum(assembly_products.quantity * assembly_products.price)   FROM "
                + TABLE_ASSEMBLIES + " AS assemblies"
                + " LEFT JOIN "
                + TABLE_ASSEMBLY_PRODUCTS + " AS assembly_products ON assembly_products.assembly_id = assemblies.id"
//                + " LEFT JOIN "
//                + TABLE_ORDER_PRODUCTS + " AS order_products ON order_products.id = assembly_products.order_products_id"


                + " GROUP BY assemblies.id, assemblies.number, assemblies.date, assemblies.sync";


        Cursor c = db.rawQuery(query, null);
        ArrayList<Assembly> list = new ArrayList<>();

        while (c.moveToNext()) {

            Assembly assembly = new Assembly();
            assembly.id             =  c.getInt(0);
            assembly.number         =  c.getInt(1);
            assembly.date           =  c.getInt(2);
            assembly.sync           =  c.getInt(3)==1;
            assembly.products_count =  c.getInt(4);
            assembly.sum            =  c.getDouble(5);

            list.add(assembly);
        }

//        db.close();
        return list;

    }

    public Assembly getAssembly(int id, Boolean get_products_for_assembly){

        Assembly assembly = new Assembly();
        assembly.id    = id;

        String query = "SELECT " +
                "assemblies.id, "                   +
                "assemblies.date, "                 +
                "assemblies.sync, "                 +
                "assemblies.number, "                +
                "assemblies.uid "                +
                "FROM " +TABLE_ASSEMBLIES +" AS assemblies "
                + " WHERE assemblies.id =?";

        Cursor c2 = db.rawQuery(query, new String[]{String.valueOf(assembly.id)});
        if (c2.moveToFirst()){
            assembly.id        = c2.getInt(0);
            assembly.date      = c2.getLong(1);;
            assembly.sync      = (c2.getInt(2)==1);
            assembly.number    = c2.getInt(3);
            assembly.uid       = c2.getString(4);


        }

        getAssembly_products(assembly);

        if(get_products_for_assembly){
            getAssembly_products_for_assembly(assembly);
        }


        return  assembly;

    }

    private void getAssembly_products(Assembly assembly){

        String query =

                "SELECT products.id, products.uid, products.vendor_code, products.sync,"
                        + "photo_product.photo_id, photo_product.x, photo_product.y, photo_product.coment,"
                        + "order_products.price, assembly_products.quantity, order_products.order_id, orders.uid, assembly_products.id  "
                + " FROM " + TABLE_ASSEMBLY_PRODUCTS + " AS assembly_products "
                + " LEFT JOIN " + TABLE_ORDER_PRODUCTS + " AS order_products ON order_products.id = assembly_products.order_products_id "
                + " LEFT JOIN " + TABLE_PRODUCTS + " AS products ON products.id = order_products.product_id "
                + " LEFT JOIN " + TABLE_PHOTO_PRODUCT + " AS photo_product ON photo_product.product_id = products.id "
                + " LEFT JOIN " + TABLE_ORDERS + " AS orders ON orders.id = order_products.order_id "
                + " WHERE assembly_products.assembly_id =?";


        Cursor c = db.rawQuery(query, new String[]{String.valueOf(assembly.id)});

        while (c.moveToNext()) {

            Product product = new Product();
            product.id              = c.getInt(0);
            product.uid             = c.getString(1);
            product.vendor_code     = c.getInt(2);
            product.sync            = (c.getInt(3)==1);
            product.photo_id        = c.getInt(4);
            product.x               = c.getFloat(5);
            product.y               = c.getFloat(6);
            product.coment          = c.getString(7);
            product.price           = c.getDouble(8);

            Double price            = c.getDouble(8);
            int quantity            = c.getInt(9);
            int order_id            = c.getInt(10);
            String order_uid        = c.getString(11);
            int assembly_products_id= c.getInt(12);

            assembly.addProduct(product, price, quantity, order_id, order_uid, assembly_products_id);
        }


    }

    private void getAssembly_products_for_assembly(Assembly assembly){

        String query =

                "SELECT products.id, products.uid, products.vendor_code, products.sync,"
                        + "photo_product.photo_id, photo_product.x, photo_product.y, photo_product.coment,"
                        + "order_products.price, order_products.quantity , order_products.set_aside , order_products.out_of_stock, order_products.order_id, assembly_products.quantity, order_products.id "
                        + ", product_catalog.id , product_catalog.name_en, product_catalog.name_chn, product_type.id , product_type.name_en, product_type.name_chn "
                        + " FROM " + TABLE_ORDER_PRODUCTS + " AS order_products "
                        + " LEFT JOIN " + TABLE_ASSEMBLY_PRODUCTS + " AS assembly_products ON assembly_products.order_products_id = order_products.id "
                        + " LEFT JOIN " + TABLE_PRODUCTS + " AS products ON products.id = order_products.product_id "
                        + " LEFT JOIN " + TABLE_PHOTO_PRODUCT + " AS photo_product ON photo_product.product_id = products.id "
                        + " LEFT JOIN " + TABLE_PRODUCT_CATALOG + " AS product_catalog ON product_catalog.id = products.catalog_id "
                        + " LEFT JOIN " + TABLE_PRODUCT_TYPE + " AS product_type ON product_type.id = products.type_id "
                        + " WHERE order_products.quantity > assembly_products.quantity OR assembly_products.quantity IS NULL";


        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {

            Product product = new Product();
            product.id              = c.getInt(0);
            product.uid             = c.getString(1);
            product.vendor_code     = c.getInt(2);
            product.sync            = (c.getInt(3)==1);
            product.photo_id        = c.getInt(4);
            product.x               = c.getFloat(5);
            product.y               = c.getFloat(6);
            product.coment          = c.getString(7);
            product.price           = c.getDouble(8);

            Double price            = c.getDouble(8);
            int quantity_order      = c.getInt(9);
            Boolean set_aside       = (c.getInt(10)==1);
            Boolean out_of_stock    = (c.getInt(11)==1);
            int order_id            = c.getInt(12);
            int quantity_assembly   = c.getInt(13);
            int order_products_id   = c.getInt(14);

            ProductCatalog catalog = new ProductCatalog();
            catalog.id              = c.getInt(15);
            catalog.name_en         = c.getString(16);
            catalog.name_chn        = c.getString(17);
            product.catalog         = catalog;

            ProductType type = new ProductType();
            type.id              = c.getInt(18);
            type.name_en         = c.getString(19);
            type.name_chn        = c.getString(20);
            product.type         = type;

            assembly.addProductsForAssembly(product, price, quantity_order, set_aside, out_of_stock, order_id, quantity_assembly, order_products_id);
        }


    }

    public ArrayList<Product> getListProductsForAssembly(){

        ArrayList<Product> list = new ArrayList<>();

        String query =
//                "SELECT assembly_products.id, order_products.product_id, products.uid  "
//                + " FROM " + TABLE_ORDER_PRODUCTS + " AS order_products "
//                + " LEFT JOIN "
//                + TABLE_ASSEMBLY_PRODUCTS +" AS assembly_products ON order_products.id = assembly_products.order_products_id "
//                + " LEFT JOIN "
//                + TABLE_PRODUCTS + " AS products ON products.id = order_products.product_id "
//                + " WHERE order_products.quantity > assembly_products.quantity";

            "SELECT order_products.id, order_products.product_id, products.uid  "
                + " FROM " + TABLE_ORDER_PRODUCTS + " AS order_products "
                + " LEFT JOIN "
                + TABLE_PRODUCTS + " AS products ON products.id = order_products.product_id ";


        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {

            String product_uid = c.getString(2);
            Product product = getProduct(product_uid);
//            product.id     =  c.getInt(0);
//            product.number =  c.getInt(1);
//            product.date   =  c.getInt(2);
//            product.sync   =  c.getInt(3)==1;

            list.add(product);
        }

        return list;
    }

    public void deleteAssemblyProduct(int id){
        db.delete(TABLE_ASSEMBLY_PRODUCTS,  "id=" + id, null);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //Session

    public void setCredential(String credential){
        setSessionVariable("credential", credential);
    }
    public String getCredential(){
        return getSessionVariable("credential");
    }

    public void setLogin(String login){
        setSessionVariable("login", login);
    }
    public String getLogin(){
        return getSessionVariable("login");
    }

    public void setPassword(String password){
        setSessionVariable("password", password);
    }
    public String getPassword(){
        return getSessionVariable("password");
    }

    public void setAuthorizationStatus(String authorization_status){
        setSessionVariable("authorization_status", authorization_status);
    }
    public String getAuthorizationStatus(){
        return getSessionVariable("authorization_status");
    }

    public String getSessionVariable(String name){

        String value  = "";
//        SQLiteDatabase db = getWritableDatabase();

        Cursor userCursor = db.rawQuery("select value from " + TABLE_SESSION + " where  name = ?", new String[]{name});
        if (userCursor.moveToFirst()) {
            value  = userCursor.getString(0);
        }

        return value;

    }
    public void setSessionVariable(String name, String value){


        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("value", value);

        Cursor userCursor = db.rawQuery("select * from " + TABLE_SESSION + " where  name = ?", new String[]{name});
        if (userCursor.moveToFirst()) {
            db.update(TABLE_SESSION, cv, "name= ?", new String[]{name});
        } else {
            db.insert(TABLE_SESSION, null, cv);
        }

    }

    public void setPhontSize(int phont_size){
        setSessionVariable("phont_size", String.valueOf(phont_size));
    }
    public int getPhontSize(){

        String phontSize = getSessionVariable("phont_size");
        if(phontSize == null || phontSize.isEmpty() || phontSize.equals(""))
            return 50;
        else
            return Integer.valueOf(phontSize);
    }






    public void addProductType(ProductType pt){

        if(pt.id == 0){
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put("id",        pt.id);
        cv.put("name_en",   pt.name_en);
        cv.put("name_chn",  pt.name_chn);

        Cursor userCursor = db.rawQuery("select * from " + TABLE_PRODUCT_TYPE + " where  id =?", new String[]{String.valueOf(pt.id)});
        if (userCursor.moveToFirst()) {
            db.update(TABLE_PRODUCT_TYPE, cv, "id=" + String.valueOf(pt.id), null);
        } else {
            db.insert(TABLE_PRODUCT_TYPE, null, cv);
        }

    }

    public ProductType getProductType(int id){

        String query = "SELECT types.id, types.name_en, types.name_chn  FROM "
                + TABLE_PRODUCT_TYPE +" AS types  "
                + " WHERE orders.id =?"
                + "";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (c.moveToFirst()){
            ProductType type = new ProductType();
            type.id        = c.getInt(0);
            type.name_en   = c.getString(1);
            type.name_chn  = c.getString(2);;

            return type;
        }

        return null;
    }

    public void addProductCatalog(ProductCatalog pc){

        if(pc.id == 0){
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put("id",        pc.id);
        cv.put("name_en",   pc.name_en);
        cv.put("name_chn",  pc.name_chn);

        Cursor userCursor = db.rawQuery("select * from " + TABLE_PRODUCT_CATALOG + " where  id =?", new String[]{String.valueOf(pc.id)});
        if (userCursor.moveToFirst()) {
            db.update(TABLE_PRODUCT_CATALOG, cv, "id=" + String.valueOf(pc.id), null);
        } else {
            db.insert(TABLE_PRODUCT_CATALOG, null, cv);
        }

    }

    public ProductCatalog getProductCatalog(int id){

        String query = "SELECT types.id, types.name_en, types.name_chn  FROM "
                + TABLE_PRODUCT_CATALOG +" AS types  "
                + " WHERE orders.id =?"
                + "";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (c.moveToFirst()){
            ProductCatalog catalog = new ProductCatalog();
            catalog.id        = c.getInt(0);
            catalog.name_en   = c.getString(1);
            catalog.name_chn  = c.getString(2);;

            return catalog;
        }

        return null;
    }

    public ArrayList<ProductType> getProductsType(){

        ArrayList<ProductType> list = new ArrayList();
        String query          = "SELECT id, name_en, name_chn FROM " + TABLE_PRODUCT_TYPE;
        Cursor c              = db.rawQuery(query, null);

        while (c.moveToNext()) {

            ProductType pt = new ProductType();
            pt.id       = c.getInt(0);
            pt.name_en  = c.getString(1);
            pt.name_chn = c.getString(2);

            list.add(pt);
        }

        return list;

    }

    public ArrayList<ProductCatalog> getProductsCatalogs(){

        ArrayList<ProductCatalog> list = new ArrayList();
        String query          = "SELECT id, name_en, name_chn FROM " + TABLE_PRODUCT_CATALOG;
        Cursor c              = db.rawQuery(query, null);

        while (c.moveToNext()) {

            ProductCatalog pt = new ProductCatalog();
            pt.id       = c.getInt(0);
            pt.name_en  = c.getString(1);
            pt.name_chn = c.getString(2);

            list.add(pt);
        }

        return list;

    }




    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db.close();
    }
}
