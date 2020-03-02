package com.account.sunstones.sunstones_purchase;

import android.app.Activity;
import android.widget.TextView;

public class Query1C_Params {

    public final Activity act;
    public String task;
    TextView text_view_rezalt;
    public String login;
    public String password;

    public Query1C_Params(Activity act){
        this.act                = act;
    }
    public Query1C_Params(String task, Activity act, TextView text_view_rezalt){
        this.act                = act;
        this.task               = task;
        this.text_view_rezalt   = text_view_rezalt;
    }


    public void setText_view_rezalt(String rezalt){

//        final String f = rezalt;
//        act.runOnUiThread(new Runnable() {
//            public void run() {
//                ((AuthorizationActivity)act).text_view_error.setText(f);
//            }
//        });

    }
}
