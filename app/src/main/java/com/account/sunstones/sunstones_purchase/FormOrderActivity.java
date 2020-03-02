package com.account.sunstones.sunstones_purchase;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FormOrderActivity extends AppCompatActivity {

    public Order order;
    DataBase data;
    TableLayout table_order_asseblies;
//    TextView text_view_order_quantity;
//    TextView text_view_assembly_quantity;
    TableRow table_order_asseblies_row_head;

    public static final int MOD_FULL_ASSEMBLY   = 20;
    public static final int MOD_NOT_FULL_ASSEMBLY   = 27;
    public static final int MOD_SET_ASIDE       = 21;
    public static final int MOD_OUT_OF_STOCK    = 22;
    public static final int MOD_ALL             = 25;

    String[] filters = {"no filter", "show - full assembly", "show - not full assembly", "show - set aside", "show - out of stock"};

    public int current_filter_mod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order);

        current_filter_mod = MOD_ALL;
        data = DataBase.getInstance(this);

        int order_id = getIntent().getIntExtra("order_id", 0);
        order = data.getOrder(order_id);

        table_order_asseblies           = (TableLayout) findViewById(R.id.table_order_asseblies);
//        text_view_order_quantity        = (TextView) findViewById(R.id.text_view_order_quantity);
//        text_view_assembly_quantity     = (TextView) findViewById(R.id.text_view_assembly_quantity);
        table_order_asseblies_row_head  = (TableRow) findViewById(R.id.table_order_asseblies_row_head);

        RefillTableOrder();

//        text_view_order_quantity.setText("ORDER:" + String.valueOf(order.getTotalQuantityOrder()) );
//        text_view_assembly_quantity.setText("ASSEMBLY:" + String.valueOf(order.getTotalQuantityAssembly()));



        Spinner spinner = (Spinner) findViewById(R.id.filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);

                switch(item) {
                    case "show - full assembly":
                        current_filter_mod = MOD_FULL_ASSEMBLY;
                        break;
                    case "show - not full assembly":
                        current_filter_mod = MOD_NOT_FULL_ASSEMBLY;
                        break;
                    case "show - set aside":
                        current_filter_mod = MOD_SET_ASIDE;
                        break;
                    case "show - out of stock":
                        current_filter_mod = MOD_OUT_OF_STOCK;
                        break;
                    case "no filter":
                        current_filter_mod = MOD_ALL;
                        break;
                    default:

                        break;
                }

                RefillTableOrder();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("ORDER #" + String.valueOf(order.id));
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

        table_order_asseblies.removeAllViews();
        table_order_asseblies.addView(table_order_asseblies_row_head, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


        for (int i = 0; i < order.products.size(); i++) {

            Order.Order_products op = order.products.get(i);

            if(current_filter_mod == MOD_ALL){

            }
            if(current_filter_mod == MOD_FULL_ASSEMBLY && op.quantity > op.quantity_assembly){
                continue;
            }
            if(current_filter_mod == MOD_OUT_OF_STOCK && !op.out_of_stock){
                continue;
            }
            if(current_filter_mod == MOD_SET_ASIDE && !op.set_aside){
                continue;
            }
            if(current_filter_mod == MOD_NOT_FULL_ASSEMBLY && op.quantity <= op.quantity_assembly){
                continue;
            }

            addRowToTableOrder(i);
        }

        if(current_filter_mod == MOD_ALL){
            addRowFoterToTableOrder();
        }

    }

    private void addRowToTableOrder(int index){

        Order.Order_products op = order.products.get(index);

        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.BLACK);
        tr.setPadding(2, 0, 2, 2);
        tr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickRow(v);
            }
        });
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));


        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1,1,1,1);



        int background_color = Color.WHITE;
        if(op.quantity_assembly >= op.quantity){
            background_color =ContextCompat.getColor(this, android.R.color.holo_green_dark);
        }else if(op.set_aside){
            background_color = ContextCompat.getColor(this, android.R.color.holo_orange_light);
        }else if(op.out_of_stock){
            background_color = ContextCompat.getColor(this, android.R.color.holo_red_light);
        }



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


        //4.
        t = new TextView(this);
        t.setText(Integer.toString(op.quantity_assembly) );
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        String set_aside = (op.set_aside == true ? "✓" : "");
        t = new TextView(this);
        t.setText(set_aside);
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
//        t.setGravity(Gravity.CENTER);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);

        String out_of_stock = (op.out_of_stock == true ? "✓" : "");
        t = new TextView(this);
        t.setText(out_of_stock);
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
//        t.setGravity(Gravity.CENTER);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        table_order_asseblies.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    private void addRowFoterToTableOrder(){


        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.BLACK);
        tr.setPadding(2, 0, 2, 2);
        tr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickRow(v);
            }
        });
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));


        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1,3,1,1);



        int background_color = Color.WHITE;




        //1.
        TextView t = new TextView(this);
        t.setText("");
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
        t.setText(String.valueOf(order.products.size()));
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //3.


        t = new TextView(this);
        t.setText(Integer.toString(order.getTotalQuantityOrder()) );
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //4.
        t = new TextView(this);
        t.setText(Integer.toString(order.getTotalQuantityAssembly()) );
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        t = new TextView(this);
        t.setText(Integer.toString(order.getTotalQuantitySetAside()));
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
//        t.setGravity(Gravity.CENTER);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);

        t = new TextView(this);
        t.setText(Integer.toString(order.getTotalQuantityOutOfStock()));
        t.setBackgroundColor(background_color);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
//        t.setGravity(Gravity.CENTER);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        table_order_asseblies.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
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

    public void onClickFilterSetAside(View view) {
        current_filter_mod = MOD_SET_ASIDE;
        RefillTableOrder();
    }

    public void onClickFilterOutOfStock(View view) {
        current_filter_mod = MOD_OUT_OF_STOCK;
        RefillTableOrder();
    }

    public void onClickFilterFullAssembly(View view) {
        current_filter_mod = MOD_FULL_ASSEMBLY;
        RefillTableOrder();
    }

    public void onClickFilterAll(View view) {
        current_filter_mod = MOD_ALL;
        RefillTableOrder();
    }

    public void onClickFilterNotAssembled(View view){

        current_filter_mod = MOD_NOT_FULL_ASSEMBLY;
        RefillTableOrder();

    }

}
