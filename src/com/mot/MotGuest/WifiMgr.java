package com.mot.MotGuest;

import android.content.Context;
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
    public WifiMgr(WifiManager mgr){
        this.wifiMgr =mgr;
    }

    public List<String> getAllAvailableSSID(){
        if (wifiMgr.isWifiEnabled() == false) {
            wifiMgr.setWifiEnabled(true);
        }
        List<ScanResult> result = wifiMgr.getScanResults();
        List<String> ssids=new ArrayList<>();
        for(ScanResult r : result){
            ssids.add(r.SSID);
        }
        return ssids;
    }

    public boolean tryPassword(String ssid, String psw) {

        return setWifiPass(ssid,psw);
    }

    private Boolean setWifiPass(String ssid, String password) {

        WifiConfiguration config = new WifiConfiguration();

        config.SSID =  "\"" + ssid + "\"";
        config.preSharedKey = "\"" + password + "\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiMgr.addNetwork(config);

        boolean result=false;
        List<WifiConfiguration> list = wifiMgr.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifiMgr.disconnect();
                result=wifiMgr.enableNetwork(i.networkId, true);
                wifiMgr.reconnect();
                break;
            }
        }
        return result;

    }
}
