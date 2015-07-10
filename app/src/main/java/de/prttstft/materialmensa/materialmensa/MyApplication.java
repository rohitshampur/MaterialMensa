package de.prttstft.materialmensa.materialmensa;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import de.prttstft.materialmensa.database.DatabaseMeals;

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private static DatabaseMeals mDatabase;
    private static Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (bus == null) {
            bus = new Bus();
        }
    }

    public static Bus getBus(){
        return bus;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public synchronized static DatabaseMeals getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DatabaseMeals(getAppContext());
        }
        return mDatabase;
    }
}