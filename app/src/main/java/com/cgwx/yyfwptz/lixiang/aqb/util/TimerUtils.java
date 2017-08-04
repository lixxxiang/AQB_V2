package com.cgwx.yyfwptz.lixiang.aqb.util;

import java.util.Timer;

/**
 * Created by yyfwptz on 2017/8/4.
 */

public class TimerUtils {
    private static Timer timer = null;
    private TimerUtils(){};
    public static Timer getTimer(){
        if (timer == null){
            timer = new Timer();
        }
        return timer;
    }
}
