package com.cgwx.yyfwptz.lixiang.aqb.presenter;

import android.util.Log;
import com.cgwx.yyfwptz.lixiang.aqb.model.api.ApiService;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.addAlarm;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.isAlarmAccepted;
import com.cgwx.yyfwptz.lixiang.aqb.util.NetUtils;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by yyfwptz on 2017/8/4.
 */

public class MainPresenter implements MainContract.Presenter{
    private MainContract.View view;
    private Retrofit retrofit;
    private ApiService apiService;
    private String CivilianId;
    private String CivilianTel;
    private Double longitude;
    private Double latitude;
    private String address;
    private String CivilianPoi;
    private String alarmId;

    @Inject
    public MainPresenter(MainContract.View view){
        this.view = view;
    }

    @Override
    public void addAlarm(String cid, String ctel, Double lon, Double lat, String poi, String addr) {
        view.addAlarmDone(alarmId);
        Log.e("Main pre","ok");
        CivilianId = cid;
        CivilianTel = ctel;
        longitude = lon;
        latitude = lat;
        address = addr;
        CivilianPoi = poi;
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<addAlarm> call = apiService.addAlarm(CivilianId, CivilianTel, longitude, latitude, CivilianPoi, address);
        call.enqueue(new retrofit2.Callback<addAlarm>() {

            @Override
            public void onResponse(retrofit2.Call<addAlarm> call, retrofit2.Response<addAlarm> response) {
                System.out.println("vcode return：" + response.body().getMeta());
                if (response.body().getMeta().equals("success")) {
                    alarmId = response.body().getAlarmId();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<addAlarm> call, Throwable throwable) {
            }
        });

    }

    @Override
    public void test() {
        Log.e("Main pre","ok");
    }

    @Override
    public void isAlarmAccepted() {
        retrofit = NetUtils.getRetrofit();
        apiService = retrofit.create(ApiService.class);
        retrofit2.Call<isAlarmAccepted> call = apiService.isAlarmAccepted(alarmId);
        call.enqueue(new retrofit2.Callback<isAlarmAccepted>() {

            @Override
            public void onResponse(retrofit2.Call<isAlarmAccepted> call, retrofit2.Response<isAlarmAccepted> response) {
                switch (response.body().getMeta()) {
                    case "success":
                        view.alarmAccepted();
                        break;
                    case "failed":
                        Log.e("TAG", "SEARCHING");
                        break;
                    case "no police":
                        Log.e("TAG", "NO POLICE NOW");
                        view.noPolice();
                        break;
                }
            }

            @Override
            public void onFailure(retrofit2.Call<isAlarmAccepted> call, Throwable throwable) {
            }
        });

    }
}
