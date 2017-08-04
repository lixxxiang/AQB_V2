package com.cgwx.yyfwptz.lixiang.aqb.di.modules;


import com.cgwx.yyfwptz.lixiang.aqb.presenter.VcodeContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yyfwptz on 2017/8/4.
 */
@Module
public class VcodeModule {
    private final VcodeContract.View vView;

    public VcodeModule(VcodeContract.View vView){
       this.vView = vView;
    }
    @Provides
    public VcodeContract.View provideVcodeView(){
        return vView;
    }
}
