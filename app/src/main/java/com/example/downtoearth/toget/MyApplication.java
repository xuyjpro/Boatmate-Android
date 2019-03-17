package com.example.downtoearth.toget;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;

public class MyApplication extends Application {
    public static List<Message> forwardMsg = new ArrayList<>();
    public static String PICTURE_DIR = "sdcard/ToGet/pictures/";
    public static String FILE_DIR = "sdcard/ToGet/recvFiles/";

    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.init( this,true);
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DEFAULT);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
