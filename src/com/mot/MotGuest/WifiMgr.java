package com.mot.MotGuest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bkmr38 on 3/23/2016.
 */
public class WifiMgr {

    WifiManager wifiMgr;
    ConnectivityManager connMgr;
    Context context;
    MsgHandler msgHandler;
    String motSSID="M-Guest";
    public WifiMgr(Context context,MsgHandler msgHandler)
    {
        this.wifiMgr =(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        this.connMgr =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);;
        this.context=context;
        this.msgHandler=msgHandler;
    }

    public boolean motSSIDExist() throws InterruptedException {

        List<String> allSSIDs=getAllAvailableSSID();
        boolean motoSSIDExist=allSSIDs.contains(motSSID);

        if(!motoSSIDExist){
            return false;
        }
        return true;
    }

    public boolean tryPassword() throws InterruptedException, UnsupportedEncodingException {
        PasswordMgr passwordMgr=PasswordMgr.getInstance();

        String passwordFromWeb=passwordMgr.getPasswordFromWeb();
        if(setWifiPass(motSSID,passwordFromWeb))
        {
            return true;
        }

        Integer index = passwordMgr.getPossbleIndex();
        HashMap<Integer, String> allPass=passwordMgr.getAllWifiPas();

        for(int i=index;i<=allPass.size();i++){
            Message msg = msgHandler.obtainMessage();
            msg.arg1 = i;
            msgHandler.sendMessage(msg);
            String password = allPass.get(i);
            if(setWifiPass(motSSID,password))
            {
                return true;
            }
        }

        for(int i=index;i>0;i--){
            Message msg = msgHandler.obtainMessage();
            msg.arg1 = i;
            msgHandler.sendMessage(msg);
            String password = allPass.get(i);
            if(setWifiPass(motSSID,password))
            {
                return true;
            }
        }

        return false;
    }

    private List<String> getAllAvailableSSID() throws InterruptedException {
        List<String> ssids=new ArrayList<>();

        if (!wifiMgr.isWifiEnabled()) {
            wifiMgr.setWifiEnabled(true);
        }
        for(int i=0;i<30;i++) {
           if(!wifiMgr.isWifiEnabled()){
                Thread.sleep(500);
            }
            else{
               break;
           }

        }

        if(wifiMgr.isWifiEnabled()) {
            List<ScanResult> result = wifiMgr.getScanResults();

            for (ScanResult r : result) {
                ssids.add(r.SSID);
            }
        }
        return ssids;
    }

    private Boolean setWifiPass(String ssid, String password) throws InterruptedException {

        if(password.isEmpty()){
            return false;
        }
        WifiConfiguration config = new WifiConfiguration();

        config.SSID = "\"" + ssid + "\"";
        config.preSharedKey = "\"" + password + "\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiMgr.addNetwork(config);


        List<WifiConfiguration> list = wifiMgr.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifiMgr.disconnect();
                wifiMgr.enableNetwork(i.networkId, true);
                wifiMgr.reconnect();
                break;
            }
        }

        return WifiIsConnected();
    }

    private Boolean WifiIsConnected() throws InterruptedException {
        boolean result = false;
        for(int i=0;i<20;i++){
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo.DetailedState state =wifiNetworkInfo.getDetailedState();
            if(state!= NetworkInfo.DetailedState.CONNECTED)
            {
                Thread.sleep(500);
            }
            result= wifiNetworkInfo.isConnected();
            if(result)
                break;
        }

        return result;
    }
}
