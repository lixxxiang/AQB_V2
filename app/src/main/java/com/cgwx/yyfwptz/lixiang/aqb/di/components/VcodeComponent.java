package com.cgwx.yyfwptz.lixiang.aqb.di.components;

import com.cgwx.yyfwptz.lixiang.aqb.di.modules.VcodeModule;
import com.cgwx.yyfwptz.lixiang.aqb.view.activity.VCodeActivity;

import dagger.Component;

/**
 * Created by yyfwptz on 2017/8/4.
 */
@Component(modules = VcodeModule.class)
public interface VcodeComponent {
    void inject(VCodeActivity activity);
}
