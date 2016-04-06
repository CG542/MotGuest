package com.mot.MotGuest;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by bkmr38 on 3/28/2016.
 */
public class MsgHandler extends Handler {
    private Activity activity;
    public MsgHandler(Activity activity){
        this.activity=new WeakReference<Activity>(activity).get();
    }
    @Override
    public void handleMessage(Message msg) {
        int index =msg.arg1;
        PasswordMgr mgr = PasswordMgr.getInstance();
        String password = mgr.getAllWifiPas().get(index);
        showInfo("Try the Password: "+password);
        super.handleMessage(msg);
    }

    /**
     * 显示提示信息
     * @param info
     */
    public void showInfo(String info)
    {
        Toast.makeText(activity.getApplicationContext(),info, Toast.LENGTH_LONG).show();
    }

}