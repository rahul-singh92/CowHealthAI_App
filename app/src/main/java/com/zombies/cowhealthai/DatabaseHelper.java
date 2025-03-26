package com.zombies.cowhealthai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CowHealthDB";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_COWS = "cows";

    // Column Names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BREED = "breed";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_MILK = "milk";
    private static final String COLUMN_IMAGE_URI = "image_uri";

    // Create Table Query
    private static final String CREATE_TABLE_COWS = "CREATE TABLE " + TABLE_COWS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_BREED + " TEXT, "
            + COLUMN_WEIGHT + " TEXT, "
            + COLUMN_AGE + " TEXT, "
            + COLUMN_MILK + " TEXT, "
            + COLUMN_IMAGE_URI + " TEXT)"; // Store image as URI

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COWS);
        onCreate(db);
    }

    // Insert Cow Data
    public boolean insertCow(String name, String breed, String weight, String age, String milk, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_BREED, breed);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_MILK, milk);
        values.put(COLUMN_IMAGE_URI, imageUri);

        long result = db.insert(TABLE_COWS, null, values);
        db.close();
        return result != -1; // Return true if insert was successful
    }

    // Fetch all cows
    public Cursor getAllCows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COWS, null);
    }

    public void deleteCow(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cows", "name = ?", new String[]{name});
        db.close();
    }

    public void updateCow(String oldName, String newName, String breed, String weight, String age, String milk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", newName);
        values.put("breed", breed);
        values.put("weight", weight);
        values.put("age", age);
        values.put("milk", milk);

        db.update("cows", values, "name = ?", new String[]{oldName});
        db.close();
    }
}
