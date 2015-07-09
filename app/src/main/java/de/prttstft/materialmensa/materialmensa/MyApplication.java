package de.prttstft.materialmensa.materialmensa;

import android.app.Application;
import android.content.Context;

import de.prttstft.materialmensa.database.DBMeals;
import de.prttstft.materialmensa.database.DatabaseHandlerMeals;

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private static DatabaseHandlerMeals mDatabase;

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

    public synchronized static DatabaseHandlerMeals getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DatabaseHandlerMeals(getAppContext());
        }

        return mDatabase;
    }
}