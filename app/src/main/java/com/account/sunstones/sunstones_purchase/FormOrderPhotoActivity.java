package com.account.sunstones.sunstones_purchase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class FormOrderPhotoActivity extends AppCompatActivity {

    DataBase data;
    Order order;
    int current_filter_mod;
    int order_product_index;
    ImageView form_order_photo_image;
    EditText form_order_photo_vendor_code;
    EditText form_order_photo_price;
    EditText form_order_photo_quantity_order;
    EditText form_order_photo_quantity_assembly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_photo);

        form_order_photo_image              = (ImageView) findViewById(R.id.form_order_photo_image);
        form_order_photo_vendor_code        = (EditText) findViewById(R.id.form_order_photo_vendor_code);
        form_order_photo_price              = (EditText) findViewById(R.id.form_order_photo_price);
        form_order_photo_quantity_order     = (EditText) findViewById(R.id.form_order_photo_quantity_order);
        form_order_photo_quantity_assembly  = (EditText) findViewById(R.id.form_order_photo_quantity_assembly);

        data                        = DataBase.getInstance(this);
        int product_vendor_code     = getIntent().getIntExtra("product_vendor_code", 0);
        int order_id                = getIntent().getIntExtra("order_id", 0);
        current_filter_mod          = getIntent().getIntExtra("current_filter_mod", 0);

        order                       = data.getOrder(order_id);
        order_product_index         = order.getIndexProduct(product_vendor_code);
        refillItems();
        getSupportActionBar().setTitle("ORDER #" + order.id);
    }

    private void refillItems(){

        if(order_product_index >= 0 && order_product_index < order.products.size()){

            Order.Order_products pp = order.products.get(order_product_index);
//            Bitmap bitmap = data.getPhotoModified(pp.product.photo_id);
            Bitmap bitmap = makePhotoWithVendorCode(pp.product);

            form_order_photo_image.setImageBitmap(bitmap);
            form_order_photo_vendor_code.setText(String.valueOf(pp.product.vendor_code));
            form_order_photo_price.setText(String.valueOf(pp.price));
            form_order_photo_quantity_order.setText(String.valueOf(pp.quantity));
            form_order_photo_quantity_assembly.setText(String.valueOf(pp.quantity_assembly));

        }
    }

    private Bitmap makePhotoWithVendorCode(Product product){

        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(50);


        Bitmap bitmap = Bitmap.createBitmap(data.getPhotoOriginal(product.photo_id));
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawCircle(product.x, product.y, 10, p);

        String text = Integer.toString(product.vendor_code);
        float width_text = p.measureText(text);
        float X = (bitmap.getWidth() < product.x + width_text ?   bitmap.getWidth() - width_text : product.x);
        float Y = product.y;
        canvas.drawText(text, X, Y, p);

        return mutableBitmap;
    }

    private void saveChengesToOrder(){

        order.sync = false;
        Order.Order_products pp = order.products.get(order_product_index);

        pp.price                = form_order_photo_price.getText().toString().isEmpty()             ? 0 : Double.valueOf(form_order_photo_price.getText().toString());
        pp.quantity             = form_order_photo_quantity_order.getText().toString().isEmpty()    ? 0 : Integer.valueOf(form_order_photo_quantity_order.getText().toString() );
        pp.quantity_assembly    = form_order_photo_quantity_assembly.getText().toString().isEmpty() ? 0 : Integer.valueOf(form_order_photo_quantity_assembly.getText().toString() );

        if(pp.quantity_assembly > 0){
            pp.out_of_stock = false;
            pp.set_aside = false;
        }


    }

    public void onClickButtonSave(View view){

        saveChengesToOrder();
        order.sync = false;
        data.addOrder(order);

        Intent intent = new Intent(this, OrdersActivity.class);
        intent.putExtra("order_id", order.id);
        startActivity(intent);
    }

    public void onClickButtonSetAside(View view){

        order.products.get(order_product_index).set_aside = true;
        order.products.get(order_product_index).out_of_stock = false;
        order.products.get(order_product_index).quantity = 0;
        onClickButtonRight(null);

    }

    public void onClickButtonOutOfStock(View view){

        order.products.get(order_product_index).set_aside           = false;
        order.products.get(order_product_index).out_of_stock        = true;
        order.products.get(order_product_index).quantity = 0;
        onClickButtonRight(null);

    }

    public void onClickButtonLeft(View view){

        saveChengesToOrder();


        for (int j = order_product_index-1; j >= 0; j--) {
            Order.Order_products op = order.products.get(j);

            if(current_filter_mod == FormOrderActivity.MOD_ALL){

            }

            if(current_filter_mod == FormOrderActivity.MOD_FULL_ASSEMBLY && op.quantity > op.quantity_assembly){
                continue;
            }

            if(current_filter_mod == FormOrderActivity.MOD_OUT_OF_STOCK && !op.out_of_stock){
                continue;
            }

            if(current_filter_mod == FormOrderActivity.MOD_SET_ASIDE && !op.set_aside){
                continue;
            }
            if(current_filter_mod == FormOrderActivity.MOD_NOT_FULL_ASSEMBLY && op.quantity <= op.quantity_assembly){
                continue;
            }

            order_product_index = j;
            refillItems();
            break;
        }

//        int Index = order_product_index - 1;
//        if(Index >= 0){
//            order_product_index -= 1;
//            refillItems();
//        }

    }

    public void onClickButtonRight(View view){

        saveChengesToOrder();

        for (int j = order_product_index+1; j < order.products.size(); j++) {
            Order.Order_products op = order.products.get(j);

            if(current_filter_mod == FormOrderActivity.MOD_ALL){

            }

            if(current_filter_mod == FormOrderActivity.MOD_FULL_ASSEMBLY && op.quantity > op.quantity_assembly){
                continue;
            }

            if(current_filter_mod == FormOrderActivity.MOD_OUT_OF_STOCK && !op.out_of_stock){
                continue;
            }

            if(current_filter_mod == FormOrderActivity.MOD_SET_ASIDE && !op.set_aside){
                continue;
            }
            if(current_filter_mod == FormOrderActivity.MOD_NOT_FULL_ASSEMBLY && op.quantity <= op.quantity_assembly){
                continue;
            }


            order_product_index = j;
            refillItems();
            break;
        }

//        int Index = order_product_index + 1;
//        if(Index < order.products.size()){
//            order_product_index += 1;
//            refillItems();
//        }



    }


}
