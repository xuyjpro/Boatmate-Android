package com.example.downtoearth.toget.utils;

import android.content.Context;

public class AppUpdateService {

    Context context;
    private static final AppUpdateService INSTANCE = new AppUpdateService();

    private static class LazyHolder {
        public static AppUpdateService getThis(Context context) {
            INSTANCE.context = context;
            return INSTANCE;
        }
    }

    public static AppUpdateService getInstance(Context context) {
        return LazyHolder.getThis(context);
    }
}