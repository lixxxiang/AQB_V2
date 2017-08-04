package com.cgwx.yyfwptz.lixiang.aqb.model.api;

import com.cgwx.yyfwptz.lixiang.aqb.model.entity.addAlarm;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.checkMessage;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.isAlarmAccepted;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.sendMessage;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yyfwptz on 2017/8/3.
 */

public interface ApiService {
    @POST("mobile/common/sendMessage")
    @FormUrlEncoded
    Call<sendMessage> sendMessage(@Field("telephone") String targetSentence);

    @POST("mobile/civilian/checkMessage")
    @FormUrlEncoded
    Call<checkMessage> checkMessage(@Field("telephone") String targetSentence, @Field("code") String targetSentence2);

    @POST("mobile/civilian/addAlarm")
    @FormUrlEncoded
    Call<addAlarm> addAlarm(@Field("civilianId") String targetSentence,
                            @Field("civilianTel") String targetSentence2,
                            @Field("longitude") Double targetSentence3,
                            @Field("latitude") Double targetSentence4,
                            @Field("poi") String targetSentence5,
                            @Field("address") String targetSentence6);

    @POST("mobile/civilian/isAlarmAccepted")
    @FormUrlEncoded
    Call<isAlarmAccepted> isAlarmAccepted(@Field("alarmId") String targetSentence);

}
