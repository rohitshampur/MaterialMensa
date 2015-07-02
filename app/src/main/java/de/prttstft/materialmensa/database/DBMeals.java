package de.prttstft.materialmensa.database;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;

import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.pojo.Meal;

public class DBMeals {
    public static final int TODAY = 0;
    public static final int TOMORROW = 1;
    private MealHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBMeals(Context context) {
        mHelper = new MealHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertMeals(int table, ArrayList<Meal> listMeals, boolean clearPrevious) {
        if (clearPrevious) {
            deleteMeals(table);
        }


        //create a sql prepared statement
        String sql = "INSERT INTO " + (table == TODAY ? MealHelper.TABLE_TODAY : MealHelper.TABLE_TOMORROW) + " VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMeals.size(); i++) {
            Meal currentMeal = listMeals.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentMeal.getName());
            statement.bindString(3, currentMeal.getCategory());
            statement.bindString(4, currentMeal.getPriceStudents());
            statement.bindString(5, currentMeal.getPriceStaff());
            statement.bindString(6, currentMeal.getPriceGuests());
            statement.bindString(7, currentMeal.getAllergens());
            statement.bindString(8, currentMeal.getAllergensSpelledOut());
            if (currentMeal.getTara()) {
                statement.bi(9, 1);
            }

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + listMeals.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Meal> readMeals(int table) {
        ArrayList<Meal> listMeals = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {MealHelper.COLUMN_UID,
                MealHelper.COLUMN_NAME,
                MealHelper.COLUMN_CATEGORY,
                MealHelper.COLUMN_PRICE_STUDENTS,
                MealHelper.COLUMN_PRICE_STAFF,
                MealHelper.COLUMN_PRICE_GUESTS,
                MealHelper.COLUMN_PRICEOUTPUT,
                MealHelper.COLUMN_ALLERGENS,
                MealHelper.COLUMN_ALLERGENS_SPELLEDOUT,
                MealHelper.COLUMN_TARA,
                MealHelper.COLUMN_BADGEICON
        };
        Cursor cursor = mDatabase.query((table == TODAY ? MealHelper.TABLE_TODAY : MealHelper.TABLE_TOMORROW), columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                Meal meal = new Meal();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank movie object to contain our data

                meal.setName(cursor.getString(cursor.getColumnIndex(MealHelper.COLUMN_NAME)));
                meal.setCategory(cursor.getString(cursor.getColumnIndex(MealHelper.COLUMN_CATEGORY)));
                meal.setPriceStudents(cursor.getString(cursor.getColumnIndex(MealHelper.COLUMN_PRICE_STUDENTS)));
                meal.setPriceStaff(cursor.getString(cursor.getColumnIndex(MealHelper.COLUMN_PRICE_STAFF)));
                meal.setPriceGuests(cursor.getString(cursor.getColumnIndex(MealHelper.COLUMN_PRICE_GUESTS)));
                meal.setPriceOutput(cursor.getString(cursor.getColumnIndex(MealHelper.COLUMN_PRICEOUTPUT)));
                meal.setAllergens(cursor.getBlob(cursor.getColumnIndex(MealHelper.COLUMN_ALLERGENS)));
                meal.setAllergensSpelledOut(cursor.getString(cursor.getColumnIndex(MealHelper.COLUMN_ALLERGENS_SPELLEDOUT)));
                meal.setBadge(cursor.getString(cursor.getColumnIndex(MealHelper.COLUMN_BADGE)));
                meal.setOrderInfo(cursor.getInt(cursor.getColumnIndex(MealHelper.COLUMN_ORDER_INFO)));
                if (cursor.getInt(cursor.getColumnIndex(MealHelper.COLUMN_TARA)) == 1) {
                    meal.setTara(true);
                } else {
                    meal.setTara(false);
                }
                meal.setBadgeIcon(cursor.getInt(cursor.getColumnIndex(MealHelper.COLUMN_BADGEICON)));

                //add the movie to the list of movie objects which we plan to return
                listMeals.add(meal);
            }
            while (cursor.moveToNext());
        }
        return listMeals;
    }

    public void deleteMeals(int table) {
        mDatabase.delete((table == TODAY ? MealHelper.TABLE_TODAY : MealHelper.TABLE_TOMORROW), null, null);
    }

    private static class MealHelper extends SQLiteOpenHelper {

        public static final String TABLE_TODAY = "today";
        public static final String TABLE_TOMORROW = "tomorrow";

        public static final String COLUMN_UID = "_id";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PRICE_STUDENTS = "price_students";
        public static final String COLUMN_PRICE_STAFF = "price_staff";
        public static final String COLUMN_PRICE_GUESTS = "price_guests";
        public static final String COLUMN_PRICEOUTPUT = "priceOutput";
        public static final String COLUMN_ALLERGENS = "allergens";
        public static final String COLUMN_ALLERGENS_SPELLEDOUT = "allergens_spelledout";
        public static final String COLUMN_BADGE = "badge";
        public static final String COLUMN_ORDER_INFO = "order_info";
        public static final String COLUMN_TARA = "tara";
        public static final String COLUMN_BADGEICON = "badgeIcon";
        private static final String CREATE_TABLE_TODAY = "CREATE TABLE " + TABLE_TODAY + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_PRICE_STUDENTS + " TEXT," +
                COLUMN_PRICE_STAFF + " TEXT," +
                COLUMN_PRICE_GUESTS + " TEXT," +
                COLUMN_PRICEOUTPUT + " TEXT," +
                COLUMN_ALLERGENS + " TEXT," +
                COLUMN_ALLERGENS_SPELLEDOUT + " TEXT," +
                COLUMN_BADGE + " TEXT" +
                COLUMN_ORDER_INFO + " INTEGER" +
                COLUMN_TARA + " INTEGER" +
                COLUMN_BADGEICON + " INTEGER" +
                ");";
        private static final String CREATE_TABLE_TOMORROW = "CREATE TABLE " + TABLE_TOMORROW + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_CATEGORY + " INTEGER," +
                COLUMN_PRICE_STUDENTS + " INTEGER," +
                COLUMN_PRICE_STAFF + " TEXT," +
                COLUMN_PRICE_GUESTS + " TEXT," +
                COLUMN_PRICEOUTPUT + " TEXT," +
                COLUMN_ALLERGENS + " TEXT," +
                COLUMN_ALLERGENS_SPELLEDOUT + " TEXT," +
                COLUMN_TARA + " TEXT" +
                COLUMN_BADGEICON + " TEXT" +
                ");";
        private static final String DB_NAME = "meals_db";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public MealHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_TODAY);
                db.execSQL(CREATE_TABLE_TOMORROW);
                L.m("create table executed");
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                L.m("upgrade table executed");
                db.execSQL(" DROP TABLE " + TABLE_TODAY + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_TOMORROW + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }
    }
}