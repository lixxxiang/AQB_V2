package com.cgwx.yyfwptz.lixiang.aqb.util;

import android.text.TextUtils;

/**
 * Created by yyfwptz on 2017/7/17.
 */

public class Constants {
    //    public static final String prefix = "http://10.10.90.11:8086/";
    public static final String prefix = "http://202.111.178.10:28084/";
    private static long lastClickTime;

    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
