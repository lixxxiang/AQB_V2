package com.cgwx.yyfwptz.lixiang.aqb.presenter;

/**
 * Created by yyfwptz on 2017/8/4.
 */

public interface VcodeContract {
    interface Presenter{
        void test();
        void resendMessage(String tel);
        void checkMessage(String tel, String vcode);
    }

    interface View{
        void recount();
        void checkVcodeSuccess(Long id);
    }
}
