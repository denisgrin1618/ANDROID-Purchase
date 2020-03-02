package com.account.sunstones.sunstones_purchase;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AuthorizationActivity extends AppCompatActivity {

    public TextView text_view_error;
    public EditText edit_login;
    public EditText edit_password;

    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int REQUEST_CODE_PERMISSIONS                = 121;

    Query1C query;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);


        if(!allPermissionsGranted()){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }


        text_view_error = (TextView) findViewById(R.id.autorization_text_view_error);
        edit_login      = (EditText) findViewById(R.id.autorization_edit_login);
        edit_password   = (EditText) findViewById(R.id.autorization_edit_password);

        query = new Query1C(this);
        db = DataBase.getInstance(this);
        String login = db.getLogin();
        edit_login.setText(login);

        String password = db.getLogin();
        edit_password.setText(password);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("AUTHORIZATION");
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(!allPermissionsGranted()){
//                showError("Permission not granted");
            }
        }
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    public void onClickButtonlogin(View view){
        new Query1C(this).execute(Query1C.TASK_AUTORIZATION);
    }


    public void openOffers(){
        Intent intent = new Intent(this, OffersActivity.class);
        startActivity(intent);
    }

    public void showError(String erorr){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!")
                .setMessage(erorr)
                .setIcon(R.drawable.delete)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();


    }

}
