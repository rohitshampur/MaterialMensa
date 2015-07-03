package de.prttstft.materialmensa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mealDatabase",
            TABLE_TODAY = "today",
            COLUMN_UID = "_id",
            COLUMN_NAME = "name",
            COLUMN_INTEGER = "integer";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TODAY + "(" + COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_INTEGER + " INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAY);

        onCreate(db);
    }
}
