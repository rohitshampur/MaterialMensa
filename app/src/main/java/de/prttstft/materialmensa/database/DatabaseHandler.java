package de.prttstft.materialmensa.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.prttstft.materialmensa.pojo.Contact;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mealDatabase",
            TABLE_TODAY = "today",
            COLUMN_ID = "_id",
            COLUMN_NAME = "name",
            COLUMN_INTEGER = "integer";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //super(context, "/mnt/sdcard/hri_database.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TODAY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_INTEGER + " INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAY);

        onCreate(db);
    }

    public void createMeal(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_INTEGER, contact.getInteger());

        db.insert(TABLE_TODAY, null, values);
        db.close();
    }

    public Contact getMeal(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODAY, new String[]{COLUMN_ID, COLUMN_NAME, String.valueOf(COLUMN_INTEGER)}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);


        if (cursor != null) {
            cursor.moveToFirst();
        }

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));

        db.close();
        cursor.close();

        return contact;
    }

    public void deleteMeal(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_TODAY, COLUMN_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public int getMealsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TODAY, null);

        int count = cursor.getCount();

        db.close();
        cursor.close();

        return count;
    }

    public int updateMeal(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_INTEGER, contact.getInteger());

        return db.update(TABLE_TODAY, values, COLUMN_ID + "=?", new String[]{String.valueOf(contact.getId())});
    }

    public List<Contact> getAllMeals() {
        List<Contact> meals = new ArrayList<Contact>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TODAY, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
                meals.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return meals;
    }
}