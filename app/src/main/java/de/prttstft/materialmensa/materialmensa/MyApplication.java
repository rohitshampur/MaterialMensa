package de.prttstft.materialmensa.materialmensa;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {
    public static final String API_KEY_KOTTEN_TOMATOES = "54wzfswsa4qmjg8hjwa64d4c";
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


}