package com.cgwx.yyfwptz.lixiang.aqb.presenter;

import android.util.Log;
import android.widget.Toast;

import com.cgwx.yyfwptz.lixiang.aqb.util.NetUtils;
import com.cgwx.yyfwptz.lixiang.aqb.model.api.ApiService;
import com.cgwx.yyfwptz.lixiang.aqb.view.activity.LoginActivity;
import com.cgwx.yyfwptz.lixiang.aqb.util.Constants;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.sendMessage;
import javax.inject.Inject;
import retrofit2.Retrofit;

/**
 * Created by yyfwptz on 2017/8/3.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private String userTel;
    private ApiService apiService;
    private Retrofit retrofit;


    @Inject
    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void test() {
        Log.e("DAGGET2_TAG", "test");
    }

    @Override
    public void sendMessage(String tel) {
        userTel = tel;
        if (Constants.isMobileNO(userTel)) {
            /**
             * 先跳转
             */
            view.getVCodeSuccess(userTel);
            retrofit = NetUtils.getRetrofit();
            apiService = retrofit.create(ApiService.class);
            retrofit2.Call<sendMessage> call = apiService.sendMessage(userTel);
            call.enqueue(new retrofit2.Callback<sendMessage>() {

                @Override
                public void onResponse(retrofit2.Call<sendMessage> call, retrofit2.Response<sendMessage> response) {
                    System.out.println("vcode return：" + response.body().getMeta());
                    if (response.body().getMeta().equals("success")){
//                        view.getVCodeSuccess(userTel);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<sendMessage> call, Throwable throwable) {
                    Toast.makeText(LoginActivity.la, "验证码获取失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.la, "手机号不正确", Toast.LENGTH_SHORT).show();
        }
    }
}
