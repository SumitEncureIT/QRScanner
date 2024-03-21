package com.irpinnovative.qrscanner.roomdatabase;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class BaseApplication extends MultiDexApplication {
    private static Context appsContext;
    private static BaseApplication instances;

    public static BaseApplication getInstances() {
        return instances;
    }

    public static Context getAppsContext() {
        return appsContext;
    }


    public void onCreate() {
        appsContext = this;
        instances = this;

        super.onCreate();
        DatabaseUtil.init(getApplicationContext());
    }

}