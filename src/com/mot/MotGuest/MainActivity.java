package com.mot.MotGuest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

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

        PasswordMgr.CreateInstance(getApplicationContext());
        Context content =getApplicationContext();
        MsgHandler msgHander = new MsgHandler(MainActivity.this);
        WifiMgr wifiMgr=new WifiMgr(content,msgHander);

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
                                PasswordMgr.getInstance().storePasswordFromWeb(NetworkAccess.getPasswordFromNet());
                                animationDrawable.stop();
                                Thread.sleep(2000);
                                System.exit(0);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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

