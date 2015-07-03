package de.prttstft.materialmensa.materialmensa;

import android.app.Application;
import android.content.Context;

import de.prttstft.materialmensa.database.DBMeals;

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private static DBMeals dbMeals;

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

    public synchronized static DBMeals getWritableDatabase() {
        if (dbMeals == null) {
            dbMeals = new DBMeals(getAppContext());
        }

        return dbMeals;
    }
}