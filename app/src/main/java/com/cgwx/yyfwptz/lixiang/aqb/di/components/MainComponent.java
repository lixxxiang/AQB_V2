package com.cgwx.yyfwptz.lixiang.aqb.di.components;

import com.cgwx.yyfwptz.lixiang.aqb.di.modules.MainModule;
import com.cgwx.yyfwptz.lixiang.aqb.view.activity.MainActivity;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/4.
 */
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
