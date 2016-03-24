package com.mot.MotGuest;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }
    public void BtnOnClick(View view){
        PasswordMgr passwordMgr=new PasswordMgr();
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiMgr wifiMgr=new WifiMgr(wifi);
        String motSSID="M-Guest";

        TextView textView =(TextView) findViewById(R.id.textView);
        textView.setText("Scaning");

        List<String> allSSIDs=wifiMgr.getAllAvailableSSID();
        boolean motoSSIDExist=allSSIDs.contains(motSSID);

        if(!motoSSIDExist){
            textView.setText("Can't find "+motSSID);
            return;
        }

        Integer index = passwordMgr.getPossbleIndex();
        HashMap<Integer, String> alllPass=passwordMgr.getAllWifiPas();

        for(int i=index;i<=alllPass.size();i++){
            String password = alllPass.get(i);
            if(wifiMgr.tryPassword(motSSID,password))
            {
                textView.setText("Password is "+password);
                return;
            }
        }

        for(int i=index;i>0;i--){
            String password = alllPass.get(i);
            if(wifiMgr.tryPassword(motSSID,password))
            {
                textView.setText("Password is "+password);
                return;
            }
        }

        textView.setText("Fail");
    }




}

