package de.prttstft.materialmensa.database;

import android.content.Context;

public class DBMeals {
    private Context context;
    private static final String DB_NAME = "meals_db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_TODAY = "meals_today";
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
    /*private static final String CREATE_TABLE_TODAY = "CREATE TABLE " + TABLE_TODAY + " (" +
            COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT"*/
}

/*
    private String name;
    private String category;
    private String price_students;
    private String price_staff;
    private String price_guests;
    private String priceOutput;
    List<String> allergens = new ArrayList<String>();
    List<String> allergens_spelledout = new ArrayList<String>();
    private String badge;
    private int order_info;
    private boolean tara;
    Integer badgeIcon;
 */