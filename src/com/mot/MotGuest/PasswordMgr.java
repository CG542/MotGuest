package com.mot.MotGuest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by bkmr38 on 3/23/2016.
 */
public class PasswordMgr {
    public Integer getPossbleIndex() {
        SimpleDateFormat fomat = new SimpleDateFormat("MM");
        Integer month = Integer.valueOf(fomat.format(new Date()));
        fomat = new SimpleDateFormat("dd");
        Integer day = Integer.valueOf(fomat.format(new Date()));

        Integer result = month*2;
        if(day>15){
            result++;
        }
        result--;
        return result;
    }

    public HashMap<Integer, String> getAllWifiPas() {
        HashMap<Integer, String> result = new HashMap<Integer, String>();
        SimpleDateFormat fomat=new SimpleDateFormat("yyyy");
        String year =fomat.format(new Date());

        result.put(1, "InternetAccess"+year);
        result.put(2, "GuestMotoaccess"+year);
        result.put(3, "SolutionsInternet"+year);
        result.put(4, "ConnectmeMoto"+year);
        result.put(5, "ThisisSolutions"+year);
        result.put(6, "SolutionsGuest"+year);
        result.put(7, "GuestInternet"+year);
        result.put(8, "Access2Internet"+year);
        result.put(9, "MyGuestInternet"+year);
        result.put(10, "Guest2web"+year);
        result.put(11, "Web4Solutions"+year);
        result.put(12, "Internet4Guests"+year);
        result.put(13, "Welcome2Solutions"+year);
        result.put(14, "Access4Guests"+year);
        result.put(15, "InternetConnection"+year);
        result.put(16, "Connect2Solutions"+year);
        result.put(17, "MyMoto2web"+year);
        result.put(18, "WelcomeGuests"+year);
        result.put(19, "Web4Guests"+year);
        result.put(20, "MyMotoAccess"+year);
        result.put(21, "Internet4Solutions"+year);
        result.put(22, "Go2Internet"+year);
        result.put(23, "MywebAccess"+year);
        result.put(24, "ThisisGuestAccess"+year);
        result.put(25, "GuestWeb4U"+year);
        result.put(26, "Internet4MSI"+year);

        return result;
    }
}
