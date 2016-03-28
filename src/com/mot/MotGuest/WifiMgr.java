package com.mot.MotGuest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.Toast;

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
    String motSSID="M-Guest";
    public WifiMgr(WifiManager mgr, ConnectivityManager connMgr,Context context)
    {
        this.wifiMgr =mgr;
        this.connMgr =connMgr;
        this.context=context;
    }

    public boolean motSSIDExist() throws InterruptedException {

        List<String> allSSIDs=getAllAvailableSSID();
        boolean motoSSIDExist=allSSIDs.contains(motSSID);

        if(!motoSSIDExist){
            return false;
        }
        return true;
    }

    public boolean tryPassword() throws InterruptedException {
        PasswordMgr passwordMgr=new PasswordMgr();

        Integer index = passwordMgr.getPossbleIndex();
        HashMap<Integer, String> alllPass=passwordMgr.getAllWifiPas();

        for(int i=index;i<=alllPass.size();i++){
            String password = alllPass.get(i);
            if(setWifiPass(motSSID,password))
            {
                return true;
            }
        }

        for(int i=index;i>0;i--){
            String password = alllPass.get(i);
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

        Thread.sleep(500);

        if(wifiMgr.isWifiEnabled()) {
            List<ScanResult> result = wifiMgr.getScanResults();

            for (ScanResult r : result) {
                ssids.add(r.SSID);
            }
        }
        return ssids;
    }

    private Boolean setWifiPass(String ssid, String password) throws InterruptedException {
//        Toast toast=Toast.makeText(context, "Try "+password, Toast.LENGTH_SHORT);
//        toast.show();

        WifiConfiguration config = new WifiConfiguration();

        config.SSID =  "\"" + ssid + "\"";
        config.preSharedKey = "\"" + password + "\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiMgr.addNetwork(config);


        List<WifiConfiguration> list = wifiMgr.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifiMgr.disconnect();
                wifiMgr.enableNetwork(i.networkId, true);
                wifiMgr.reconnect();
                break;
            }
        }


        boolean result = false;
        for(int i=0;i<10;i++){
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo.DetailedState state =wifiNetworkInfo.getDetailedState();
            if(state!= NetworkInfo.DetailedState.CONNECTED)
            {
                Thread.sleep(1000);
            }
            result= wifiNetworkInfo.isConnected();
            if(result)
                break;
        }

        return result;

    }
}
