package com.account.sunstones.sunstones_purchase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AssembliesActivity extends AppCompatActivity {

    DataBase db;
    ArrayList<Assembly> list_assemblies = new ArrayList<>();
    ScrollView assemblies_scroll;
    TableLayout table_assemblies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemblies);


        db = DataBase.getInstance(this);

        table_assemblies = (TableLayout) findViewById(R.id.table_assemblies);
        assemblies_scroll = (ScrollView) findViewById(R.id.assemblies_scroll);

        list_assemblies = db.getListAssemblies();
        RefillTableOffers();

        assemblies_scroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                assemblies_scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("ASSEMBLIES");
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

        for (int i = 0; i < list_assemblies.size(); i++) {
            addRowToTableOffers(list_assemblies.get(i));
        }

//        offers_scrol.fullScroll(ScrollView.FOCUS_DOWN);

    }

    private void addRowToTableOffers(Assembly assembly){

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


        //1.
        TextView t = new TextView(this);
        t.setText(Integer.toString(assembly.id));
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
        Date d = new Date(assembly.date * 1000);
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
        t.setText(Integer.toString(assembly.products_count) );
        t.setBackgroundColor(Color.WHITE);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);

        //3.
        t = new TextView(this);
        t.setText(Double.toString(assembly.sum) );
        t.setBackgroundColor(Color.WHITE);
        t.setTextAppearance(android.R.style.TextAppearance_Medium);
        t.setLayoutParams(params);
        t.setPadding(5, 35, 5, 0);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setMinLines(2);
        tr.addView(t);


        //4.
        String sync = (assembly.sync == true ? "✓" : "");
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
//                        "Delete assembly #" + offer_id + " ?")
//                        .setCancelable(false)
//                        .setPositiveButton("ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        db.deleteOffer(offer_id);
//                                        table_assemblies.removeView(row);
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

//        Button button_del = new Button(this);
////        button_del.setText("delete");
//        button_del.setLayoutParams(params);
//        button_del.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        button_del.setBackground(ContextCompat.getDrawable(this, R.drawable.button_undo));
//        button_del.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//
//                TableRow row = (TableRow)v.getParent();
//                TextView firstTextView = (TextView) row.getChildAt(0);
//                int offer_id = Integer.valueOf( firstTextView.getText().toString());
//
//                db.deleteOffer(offer_id);
//                table_offers.removeView(row);
//            }
//        });
//        tr.addView(button_del);

        table_assemblies.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


    }

    public void onClickRow(View view){

        TableRow t = (TableRow) view;
        TextView firstTextView = (TextView) t.getChildAt(0);
        String assembly_id = firstTextView.getText().toString();

        Intent intent = new Intent(this, FormAssemblyActivity.class);
        intent.putExtra("assembly_id", Integer.valueOf(assembly_id) );
        startActivity(intent);

    }

    public void onClickButtonAddAssembly(View view){

        Assembly new_assembly = db.addAssembly(null);
        list_assemblies.add(new_assembly);

        addRowToTableOffers(new_assembly);
        assemblies_scroll.fullScroll(ScrollView.FOCUS_DOWN);

        Intent intent = new Intent(this, FormAssemblyActivity.class);
        intent.putExtra("assembly_id", Integer.valueOf(new_assembly.id) );
        startActivity(intent);

    }
}
