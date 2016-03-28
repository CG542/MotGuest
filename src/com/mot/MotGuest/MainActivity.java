package com.mot.MotGuest;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private AnimationDrawable animationDrawable;
    private ImageView animationIV;
    private Thread t;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);




    }

    @Override
    protected void onResume(){
        super.onResume();

        animationIV = (ImageView) findViewById(R.id.animationIV);

        animationIV.setImageResource(R.drawable.animation1);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();


        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Context content =getApplicationContext();
        MsgHandler msgHander = new MsgHandler(MainActivity.this);
        WifiMgr wifiMgr=new WifiMgr(wifi,connectivityManager,content,msgHander);

        try {
            if(!wifiMgr.motSSIDExist())
            {
                Toast toast=Toast.makeText(getApplicationContext(), "Can't find M-Guest", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                animationDrawable.start();
                t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(wifiMgr.tryPassword()) {
                                animationDrawable.stop();

                                Thread.sleep(2000);
                                System.exit(0);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Thread.State s = t.getState();
        if(t!=null && t.getState()!= Thread.State.RUNNABLE)
            t.start();
    }

}

