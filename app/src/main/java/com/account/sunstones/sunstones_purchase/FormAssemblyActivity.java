package com.account.sunstones.sunstones_purchase;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FormAssemblyActivity extends AppCompatActivity {

    DataBase db;
    int assembly_id=0;
    Assembly assembly;
    TableLayout table_assemblies;
    TableRow table_asseblies_row_head;
    Boolean delete_mod = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_assembly);

        db = DataBase.getInstance(this);
        assembly_id = getIntent().getIntExtra("assembly_id", 0);
        assembly = db.getAssembly(assembly_id, true);

        table_assemblies  = (TableLayout) findViewById(R.id.table_assemblies);
        table_asseblies_row_head = (TableRow) findViewById(R.id.table_asseblies_row_head);
        RefillTableOrder();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("ASSEMBLY #" + String.valueOf(assembly.id));
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


    private void RefillTableOrder(){

        table_assemblies.removeAllViews();
        table_assemblies.addView(table_asseblies_row_head, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < assembly.products.size(); i++) {
            Assembly.Assembly_products op = assembly.products.get(i);
            addRowToTableOrder(i);
        }
        addRowFoterToTableOrder();

    }

    private void addRowToTableOrder(int index){

        Assembly.Assembly_products op = assembly.products.get(index);

        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.BLACK);
        tr.setPadding(2, 0, 2, 2);
        tr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickRow(v);
            }
        });
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));


//        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(2,0,2,2);



        int background_color = Color.WHITE;
//        if(op.quantity_assembly >= op.quantity){
//            background_color = ContextCompat.getColor(this, android.R.color.holo_green_dark);
//        }else if(op.set_aside){
//            background_color = ContextCompat.getColor(this, android.R.color.holo_orange_light);
//        }else if(op.out_of_stock){
//            background_color = ContextCompat.getColor(this, android.R.color.holo_red_light);
//        }



        //1.
        TextView t = new TextView(this);
        t.setText(Integer.toString(index+1));
//            valueTV.setTextColor(Color.GREEN);
        t.setBackgroundColor(background_color);
//        t.setId(View.generateViewId());
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setMinLines(2);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            t.setWidth(sizeWindow.x / 5);

        tr.addView(t); // Adding textView to tablerow.



        //2.
        t = new TextView(this);
        t.setText(String.valueOf(op.product.vendor_code));
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //3.
        t = new TextView(this);
        t.setText(Integer.toString(op.quantity) );
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);




        Double sum= op.quantity * op.price;
        t = new TextView(this);
        t.setText(Double.toString(sum));
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
//        t.setGravity(Gravity.CENTER);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);



        CheckBox box = new CheckBox(this);
        box.setBackgroundColor(Color.WHITE);
        box.setTextAppearance(android.R.style.TextAppearance_Medium);
        box.setLayoutParams(params);
        box.setPadding(5, 8, 0, 27);
        box.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        box.setVisibility(View.GONE);
        box.setMinLines(2);
        tr.addView(box); // Adding textView to tablerow.



        table_assemblies.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));



    }

    private void addRowFoterToTableOrder(){


        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.BLACK);
        tr.setPadding(2, 2, 2, 2);
        tr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickRow(v);
            }
        });
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));


//        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(2,2,0,2);



        int background_color = Color.WHITE;
//        if(op.quantity_assembly >= op.quantity){
//            background_color = ContextCompat.getColor(this, android.R.color.holo_green_dark);
//        }else if(op.set_aside){
//            background_color = ContextCompat.getColor(this, android.R.color.holo_orange_light);
//        }else if(op.out_of_stock){
//            background_color = ContextCompat.getColor(this, android.R.color.holo_red_light);
//        }



        //1.
        TextView t = new TextView(this);
        t.setText("TOTAL");
//            valueTV.setTextColor(Color.GREEN);
        t.setBackgroundColor(background_color);
//        t.setId(View.generateViewId());
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setMinLines(2);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            t.setWidth(sizeWindow.x / 5);

        tr.addView(t); // Adding textView to tablerow.



        //2.

        t = new TextView(this);
        t.setText(String.valueOf(assembly.products.size()));
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //3.
        int quantity = 0;
        for(Assembly.Assembly_products p : assembly.products){
            quantity += p.quantity;
        }

        t = new TextView(this);
        t.setText(Integer.toString(quantity) );
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);




        Double sum = 0.0;
        for(Assembly.Assembly_products p : assembly.products){
            sum += (p.quantity * p.price);
        }

        t = new TextView(this);
        t.setText(Double.toString(sum));
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
//        t.setGravity(Gravity.CENTER);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);

        t = new TextView(this);
        t.setText("");
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
//        t.setGravity(Gravity.CENTER);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        t.setVisibility(View.GONE);
        tr.addView(t);


        table_assemblies.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    public void onClickRow(View view){

//        TableRow t = (TableRow) view;
//        TextView firstTextView = (TextView) t.getChildAt(1);
//        String product_vendor_code = firstTextView.getText().toString();
//
//        Intent intent = new Intent(this, FormOrderPhotoActivity.class);
//        intent.putExtra("product_vendor_code", Integer.valueOf(product_vendor_code) );
//        intent.putExtra("order_id", order.id );
//        intent.putExtra("current_filter_mod", current_filter_mod );
//
//        startActivity(intent);
    }

    public void onClickButtonAdd(View view) {
        finish();

        Intent intent = new Intent(this, FormAssemblyPhotoActivity.class);
        intent.putExtra("assembly_id", assembly.id);
        startActivity(intent);

    }

    public void onClickButtonDelete(View view) {

        delete_mod = !delete_mod;


        if(!delete_mod){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete")
                    .setMessage("Delete selected rows?")
                    .setIcon(R.drawable.delete)
                    .setCancelable(false)
                    .setPositiveButton("Оk",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    deleteSelectedRows();
                                    updateVisibilityColomnDel();
                                }
                            })
                    .setNegativeButton("Cansel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    updateVisibilityColomnDel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{



            updateVisibilityColomnDel();
        }

    }

    public void onClickButtonSave(View view) {
        assembly.sync = false;
        db.saveAssembly(assembly);

        Intent intent = new Intent(this, AssembliesActivity.class);
        startActivity(intent);
    }

    public void onClickButtonPrint(View v){

//        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "table_assemblies.jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String mPath    = storageDir + "/" + "assembly_"+ assembly.id+".jpg";

        Bitmap bitmap;
        View v1 = findViewById(R.id.table_assemblies);
        v1.setDrawingCacheEnabled(true);
        // bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        bitmap = loadBitmapFromView(v1, v1.getWidth(), v1.getHeight());
        v1.setDrawingCacheEnabled(false);
        OutputStream fout = null;
        File imageFile = new File(mPath);
        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();


            Toast toast = Toast.makeText(getApplicationContext(), "Image saved " + mPath, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }



    private void deleteSelectedRows(){

        assembly.sync = false;
        db.addAssembly(assembly);

        for (int i = 1, j = table_assemblies.getChildCount(); i < j; i++) {

            View view1 = table_assemblies.getChildAt(i);
            if (view1 instanceof TableRow) {

                TableRow row = (TableRow) view1;

                if (row.getChildAt(4) instanceof CheckBox) {
                    CheckBox view_del = (CheckBox) row.getChildAt(4);
                    TextView view_index = (TextView) row.getChildAt(0);
                    int index = (view_index.getText().toString().isEmpty() ? 0 : Integer.valueOf(view_index.getText().toString()));

                    Assembly.Assembly_products op = assembly.products.get(index - 1);
                    if (view_del.isChecked()) {
                        table_assemblies.removeView(row);
                        db.deleteAssemblyProduct(op.id);
                    }
                }

            }

        }

    }

    public void updateVisibilityColomnDel(){

        int visibility = (delete_mod==true ? View.VISIBLE : View.GONE);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(1, 0, 0, 0);

        for (int i = 0, j = table_assemblies.getChildCount(); i < j; i++) {

            View view1 = table_assemblies.getChildAt(i);
            if (view1 instanceof TableRow) {

                TableRow row = (TableRow) view1;

                if(i==0 || i==table_assemblies.getChildCount()-1){
                    TextView del = (TextView)row.getChildAt(4);
                    del.setVisibility(visibility);
                }else{

                    CheckBox del = (CheckBox)row.getChildAt(4);
                    del.setVisibility(visibility);
                }

            }

        }


    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, width, height);
        //Get the view’s background
        Drawable bgDrawable =v.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(c); //has background drawable, then draw it on the canvas
        else
            c.drawColor(Color.WHITE); //does not have background drawable, then draw white background on the canvas

        v.draw(c);
        return b;
    }

}
