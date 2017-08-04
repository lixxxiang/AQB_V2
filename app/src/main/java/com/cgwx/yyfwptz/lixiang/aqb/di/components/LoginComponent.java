package com.cgwx.yyfwptz.lixiang.aqb.di.components;



import com.cgwx.yyfwptz.lixiang.aqb.di.modules.LoginModule;
import com.cgwx.yyfwptz.lixiang.aqb.view.activity.LoginActivity;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/3.
 */
@Component(modules = LoginModule.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}
