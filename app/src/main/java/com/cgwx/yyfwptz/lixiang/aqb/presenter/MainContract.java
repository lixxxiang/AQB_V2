package com.cgwx.yyfwptz.lixiang.aqb.presenter;

/**
 * Created by yyfwptz on 2017/8/4.
 */

public interface MainContract {
    interface Presenter{
        void addAlarm(String cid, String ctel, Double lon, Double lat, String poi, String addr);
        void test();
        void isAlarmAccepted();
    }

    interface View{
        void addAlarmDone(String aid);
        void alarmAccepted();
        void noPolice();
    }
}
