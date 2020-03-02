package com.account.sunstones.sunstones_purchase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OffersActivity extends AppCompatActivity {

    DataBase db;
    TableLayout table_offers;
    ArrayList<Offer> list_offers = new ArrayList<>();
    ScrollView offers_scrol;
    Boolean delete_mod = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        db = DataBase.getInstance(this);
        table_offers = (TableLayout) findViewById(R.id.table_offers);
        offers_scrol = (ScrollView) findViewById(R.id.offers_scrol);

        list_offers = db.getListOffersWithEmptyPhotos();
        RefillTableOffers();

        offers_scrol.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                offers_scrol.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("OFFERS");
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

        Display display = getWindowManager().getDefaultDisplay();
        Point sizeWindow = new Point();
        display.getSize(sizeWindow);

        for (int i = 0; i < list_offers.size(); i++) {
            addRowToTableOffers(list_offers.get(i));
        }

//        offers_scrol.fullScroll(ScrollView.FOCUS_DOWN);

    }

    private void addRowToTableOffers(Offer offer){

        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.BLACK);
        tr.setPadding(0, 0, 1, 1);
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
        t.setText(Integer.toString(offer.id));
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
        Date d = new Date(offer.date * 1000);
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
        t.setText(Integer.toString(offer.total_quantity_products) );
        t.setBackgroundColor(Color.WHITE);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //4.
        String sync = (offer.sync == true ? "✓" : "");
        t = new TextView(this);
        t.setText(sync);
        t.setBackgroundColor(Color.WHITE);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        SpannableString spanString = new SpannableString("Delete");
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
//        spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);


//        t = new TextView(this);
//        t.setText(spanString);
//        t.setBackgroundColor(Color.WHITE);
//        t.setTextAppearance(android.R.style.TextAppearance_Medium);
//        t.setLayoutParams(params);
//        t.setPadding(5, 35, 5, 0);
//        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        t.setMinLines(2);
//        t.setTextColor(Color.RED);
//        t.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//
//                final TableRow row = (TableRow)v.getParent();
//                TextView firstTextView = (TextView) row.getChildAt(0);
//                final int offer_id = Integer.valueOf( firstTextView.getText().toString());
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setMessage(
//                        "Delete offer #" + offer_id + " ?")
//                        .setCancelable(false)
//                        .setPositiveButton("ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        db.deleteOffer(offer_id);
//                                        table_offers.removeView(row);
//                                    }
//                                })
//                        .setNegativeButton("cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                    }
//                                });
//                builder.show();
//
//
//
//            }
//        });
//        tr.addView(t);


        CheckBox box = new CheckBox(this);
        box.setBackgroundColor(Color.WHITE);
        box.setTextAppearance(android.R.style.TextAppearance_Medium);
        box.setLayoutParams(params);
        box.setPadding(5, 8, 0, 27);
        box.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        box.setVisibility(View.GONE);
        box.setMinLines(2);
        tr.addView(box); // Adding textView to tablerow.


        table_offers.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


    }

    public void onClickRow(View view){

        TableRow t = (TableRow) view;
        TextView firstTextView = (TextView) t.getChildAt(0);
        String offer_id = firstTextView.getText().toString();

//        Toast toast = Toast.makeText(this, offer_id, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();


        Intent intent = new Intent(this, FormOfferActivity.class);
        intent.putExtra("offer_id", Integer.valueOf(offer_id) );
        startActivity(intent);




    }

    public void onClickButtonAddOffer(View view){
        Offer new_offer = db.addOffer(null);

        list_offers.add(new_offer);
        addRowToTableOffers(new_offer);

        offers_scrol.fullScroll(ScrollView.FOCUS_DOWN);

        Intent intent = new Intent(this, FormOfferActivity.class);
        intent.putExtra("offer_id", Integer.valueOf(new_offer.id) );
        startActivity(intent);

    }

    public void onClickButtonDelete(View view){

        delete_mod = !delete_mod;


        if(!delete_mod){

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Delete")
                    .setMessage("Delete selected offers?")
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
            android.support.v7.app.AlertDialog alert = builder.create();
            alert.show();
        }else{



            updateVisibilityColomnDel();
        }

    }


    public void updateVisibilityColomnDel(){

        int visibility = (delete_mod==true ? View.VISIBLE : View.GONE);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(1, 0, 0, 0);

        for (int i = 0, j = table_offers.getChildCount(); i < j; i++) {

            View view1 = table_offers.getChildAt(i);
            if (view1 instanceof TableRow) {

                TableRow row = (TableRow) view1;

                if(i==0 ){
                    TextView del = (TextView)row.getChildAt(4);
                    del.setVisibility(visibility);
                }else{

                    CheckBox del = (CheckBox)row.getChildAt(4);
                    del.setVisibility(visibility);
                }

            }

        }


    }

    private void deleteSelectedRows(){

        ArrayList<TableRow> list_row_delete = new  ArrayList<TableRow>();

        for (int i = 1, j = table_offers.getChildCount(); i < j; i++) {

            View view1 = table_offers.getChildAt(i);
            if (view1 instanceof TableRow) {

                TableRow row = (TableRow) view1;

                if (row.getChildAt(4) instanceof CheckBox) {
                    CheckBox view_del   = (CheckBox) row.getChildAt(4);
                    TextView view_index = (TextView) row.getChildAt(0);
                    int offer_id        = (view_index.getText().toString().isEmpty() ? 0 : Integer.valueOf(view_index.getText().toString()));

                    if (view_del.isChecked()) {
                        db.deleteOffer(offer_id);
                        list_row_delete.add(row);
                    }
                }

            }

        }


        for(TableRow row : list_row_delete){
            table_offers.removeView(row);
        }

    }


}
