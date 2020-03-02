package com.account.sunstones.sunstones_purchase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SynchronizationActivity extends AppCompatActivity {

    Button button_start_synchronization;
    public TextView synchronization_text_rezalt;
    public ProgressBar progress_bar_current;
    public ProgressBar progress_bar_total;
    public TextView text_view_current;
    public TextView text_view_total;
    Query1C query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);

        button_start_synchronization = (Button)findViewById(R.id.button_start_synchronization);
        synchronization_text_rezalt  = (TextView)findViewById(R.id.synchronization_text_rezalt);
        progress_bar_current         = (ProgressBar)findViewById(R.id.progress_bar_current);
        progress_bar_total           = (ProgressBar)findViewById(R.id.progress_bar_total);
        text_view_current            = (TextView)findViewById(R.id.text_view_current);
        text_view_total              = (TextView)findViewById(R.id.text_view_total);
        query                        = new Query1C(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("SYNCHRONIZATION");
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


    public void onClickButtonStartSynchronization(View view){
        new Query1C(this).execute(Query1C.TASK_SYNCHRONIZATION);
    }


}
