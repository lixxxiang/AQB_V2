package com.cgwx.yyfwptz.lixiang.aqb.di.modules;

import com.cgwx.yyfwptz.lixiang.aqb.presenter.MainContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yyfwptz on 2017/8/4.
 */
@Module
public class MainModule {
    private final MainContract.View mView;

    public MainModule(MainContract.View mView){
        this.mView = mView;
    }

    @Provides
    public MainContract.View provideMainView(){
        return mView;
    }
}
