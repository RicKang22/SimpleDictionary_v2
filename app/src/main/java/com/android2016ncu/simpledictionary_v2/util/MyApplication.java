package com.android2016ncu.simpledictionary_v2.util;

import android.app.Application;
import android.content.Context;


// 全局获取Context

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext(){
        return context;
    }




}
