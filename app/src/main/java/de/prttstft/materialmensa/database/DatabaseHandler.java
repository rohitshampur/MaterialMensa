package de.prttstft.materialmensa.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.prttstft.materialmensa.pojo.Contact;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mealDatabase",
            TABLE_TODAY = "today",
            COLUMN_ID = "_id",
            COLUMN_NAME = "name",
            COLUMN_CATEGORY = "category",
            COLUMN_PRICE_STUDENTS = "price_students",
            COLUMN_PRICE_STAFF = "price_staff",
            COLUMN_PRICE_GUESTS = "price_guests",
            COLUMN_ALLERGENS = "allergens",
            COLUMN_ALLERGENS_FULL = "allergens_full";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TODAY + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_PRICE_STUDENTS + " TEXT," +
                COLUMN_PRICE_STAFF + " TEXT," +
                COLUMN_PRICE_GUESTS + " TEXT," +
                COLUMN_ALLERGENS + " TEXT," +
                COLUMN_ALLERGENS_FULL + " TEXT" + ")");
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
        values.put(COLUMN_CATEGORY, contact.getCategory());
        values.put(COLUMN_PRICE_STUDENTS, contact.getPrice_students());
        values.put(COLUMN_PRICE_STAFF, contact.getPrice_staff());
        values.put(COLUMN_PRICE_GUESTS, contact.getPrice_guests());
        values.put(COLUMN_ALLERGENS, convertListToString(contact.getAllergens().size(), contact.getAllergens()));
        values.put(COLUMN_ALLERGENS_FULL, convertListToString(contact.getAllergens_full().size(), contact.getAllergens_full()));

        db.insert(TABLE_TODAY, null, values);
        db.close();
    }


    public Contact getMeal(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODAY, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_CATEGORY, COLUMN_PRICE_STUDENTS, COLUMN_PRICE_STAFF, COLUMN_PRICE_GUESTS, COLUMN_ALLERGENS, COLUMN_ALLERGENS_FULL}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);


        if (cursor != null) {
            cursor.moveToFirst();
        }
        //Integer.parseInt(cursor.getString(2))

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), // _ID
                cursor.getString(1), // name
                cursor.getString(2), // category
                cursor.getString(3), // price_students
                cursor.getString(4), // price_staff
                cursor.getString(5), // price_guests
                convertStringToList(cursor.getString(6)), // allergens
                convertStringToList(cursor.getString(7))); // allergens_full

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
        values.put(COLUMN_CATEGORY, contact.getCategory());
        values.put(COLUMN_PRICE_STUDENTS, contact.getPrice_students());
        values.put(COLUMN_PRICE_STAFF, contact.getPrice_staff());
        values.put(COLUMN_PRICE_GUESTS, contact.getPrice_guests());
        values.put(COLUMN_ALLERGENS, convertListToString(contact.getAllergens().size(), contact.getAllergens()));
        values.put(COLUMN_ALLERGENS_FULL, convertListToString(contact.getAllergens_full().size(), contact.getAllergens_full()));

        return db.update(TABLE_TODAY, values, COLUMN_ID + "=?", new String[]{String.valueOf(contact.getId())});
    }

    public List<Contact> getAllMeals() {
        List<Contact> meals = new ArrayList<Contact>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TODAY, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), // _ID
                        cursor.getString(1), // name
                        cursor.getString(2), // category
                        cursor.getString(3), // price_students
                        cursor.getString(4), // price_staff
                        cursor.getString(5), // price_guests
                        convertStringToList(cursor.getString(6)), // allergens
                        convertStringToList(cursor.getString(7))); // allergens_full
                meals.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return meals;
    }

    /**
     * This Method takes a StringList and an StringList Size and converts it into an String with the help of Gson.
     */
    private String convertListToString(int size, List<String> inputList) {
        ArrayList<String> arrayList = new ArrayList<String>(size);
        arrayList.addAll(inputList);
        Gson gson = new Gson();

        return gson.toJson(arrayList);
    }

    /**
     * This Method takes a String and converts it into an StringList with the help of Gson.
     */
    private List<String> convertStringToList(String string) {
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        Gson gson = new Gson();
        ArrayList<String> list = gson.fromJson(string, type);
        List<String> listAllergens = new ArrayList<String>();
        listAllergens.addAll(list);

        return list;
    }
}