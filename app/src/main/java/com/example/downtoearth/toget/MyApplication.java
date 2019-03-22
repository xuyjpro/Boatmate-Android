package com.example.downtoearth.toget;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.init( getApplicationContext(),true);
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DEFAULT);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
