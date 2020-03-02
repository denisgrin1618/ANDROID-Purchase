package com.account.sunstones.sunstones_purchase;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrdersActivity extends AppCompatActivity {


    DataBase db;
    TableLayout table_orders;
    ArrayList<Order> list_orders = new ArrayList<>();
    ScrollView orders_scrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        db = DataBase.getInstance(this);
        table_orders = (TableLayout) findViewById(R.id.table_orders);
        orders_scrol = (ScrollView) findViewById(R.id.orders_scrol);

        list_orders = db.getListOrdersWithEmptyProducts();
        RefillTableOffers();
//        orders_scrol.fullScroll(ScrollView.FOCUS_DOWN);
        orders_scrol.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                orders_scrol.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("ORDERS");
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



    private void RefillTableOffers(){

//        Display display = getWindowManager().getDefaultDisplay();
//        Point sizeWindow = new Point();
//        display.getSize(sizeWindow);

        for (int i = 0; i < list_orders.size(); i++) {
            addRowToTableOffers(list_orders.get(i));
        }

    }

    private void addRowToTableOffers(Order order){

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
        params.setMargins(1,0,1,1);


        //1.
        TextView t = new TextView(this);
        t.setText(Integer.toString(order.id));
//            valueTV.setTextColor(Color.GREEN);
        t.setBackgroundColor(Color.WHITE);
//        t.setId(View.generateViewId());
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
//            t.setWidth(sizeWindow.x / 5);
        tr.addView(t); // Adding textView to tablerow.



        //2.
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date d = new Date(order.date * 1000);
        String date_string = df.format(d);

        t = new TextView(this);
        t.setText(date_string);
        t.setBackgroundColor(Color.WHITE);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //3.
        t = new TextView(this);
        t.setText(Integer.toString(order.total_quantity_products) );
        t.setBackgroundColor(Color.WHITE);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //3.
        t = new TextView(this);
        t.setText(Double.toString(order.sum) );
        t.setBackgroundColor(Color.WHITE);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //4.
        String sync = (order.sync == true ? "✓" : "");
        t = new TextView(this);
        t.setText(sync);
        t.setBackgroundColor(Color.WHITE);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);

        table_orders.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    public void onClickRow(View view){

        TableRow t = (TableRow) view;
        TextView firstTextView = (TextView) t.getChildAt(0);
        String order_id = firstTextView.getText().toString();

//        Toast toast = Toast.makeText(this, offer_id, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();


        Intent intent = new Intent(this, FormOrderActivity.class);
        intent.putExtra("order_id", Integer.valueOf(order_id) );
        startActivity(intent);

    }

}
