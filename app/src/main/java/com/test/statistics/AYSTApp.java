package com.test.statistics;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * User: Geek_Ping
 * Name：AYSTApp
 * Date: 2016-05-11
 * Info:全局配置
 */
public class AYSTApp extends MultiDexApplication {



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {

    }




}
