package com.cgwx.yyfwptz.lixiang.aqb.presenter;

/**
 * Created by yyfwptz on 2017/8/3.
 */

public interface LoginContract {
    interface Presenter {
        void test();
        void sendMessage(String tel);
    }

    interface View{
        void getVCodeSuccess(String tel);
    }
}
