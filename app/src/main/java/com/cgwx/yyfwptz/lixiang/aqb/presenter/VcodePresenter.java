package com.cgwx.yyfwptz.lixiang.aqb.presenter;

import android.util.Log;
import android.widget.Toast;

import com.cgwx.yyfwptz.lixiang.aqb.model.api.ApiService;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.checkMessage;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.sendMessage;
import com.cgwx.yyfwptz.lixiang.aqb.util.NetUtils;
import com.cgwx.yyfwptz.lixiang.aqb.view.activity.LoginActivity;
import com.cgwx.yyfwptz.lixiang.aqb.view.activity.VCodeActivity;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by yyfwptz on 2017/8/4.
 */

public class VcodePresenter implements VcodeContract.Presenter {
    private VcodeContract.View view;
    private String userTel;
    private String userVcode;
    private Long userId;
    private ApiService apiService;
    private Retrofit retrofit;

    @Inject
    public VcodePresenter(VcodeContract.View view) {
        this.view = view;
    }


    @Override
    public void test() {
        Log.e("TAG", "vcode test");
    }

    @Override
    public void resendMessage(String tel) {
        userTel = tel;
        /**
         * 提前显示
         */
        view.recount();
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<sendMessage> call = apiService.sendMessage(userTel);
        call.enqueue(new retrofit2.Callback<sendMessage>() {

            @Override
            public void onResponse(retrofit2.Call<sendMessage> call, retrofit2.Response<sendMessage> response) {
                System.out.println("vcode return：" + response.body().getMeta());
                if (response.body().getMeta().equals("success")) {
                }
            }

            @Override
            public void onFailure(retrofit2.Call<sendMessage> call, Throwable throwable) {
                Toast.makeText(LoginActivity.la, "验证码获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void checkMessage(String tel, String vcode) {
        userTel = tel;
        userVcode = vcode;
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<checkMessage> call = apiService.checkMessage(userTel, userVcode);
        call.enqueue(new retrofit2.Callback<checkMessage>() {

            @Override
            public void onResponse(retrofit2.Call<checkMessage> call, retrofit2.Response<checkMessage> response) {
                System.out.println("checkvcode return：" + response.body().getMeta());
                if (response.body().getMeta().equals("success")) {
                    userId = response.body().getCivilianId();
                    Log.e("checkvcode return：", "dfs");
                    view.checkVcodeSuccess(userId);
                } else {
                    Toast.makeText(VCodeActivity.va, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<checkMessage> call, Throwable throwable) {
            }
        });
    }
}
