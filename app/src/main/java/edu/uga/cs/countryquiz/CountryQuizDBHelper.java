package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CountryQuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "CountryQuizDBHelper";
    private static final String DATABASE_NAME = "countryquiz.db";
    private static final int DATABASE_VERSION = 1;

    // Table for storing countries
    public static final String TABLE_COUNTRIES = "countries";
    public static final String COLUMN_COUNTRY_ID = "id";
    public static final String COLUMN_COUNTRY_NAME = "name";
    public static final String COLUMN_CONTINENT = "continent";



    // Table for storing quiz results
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String COLUMN_QUIZ_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SCORE = "score";



    private static final String TABLE_COUNTRIES_CREATE =
            "CREATE TABLE " + TABLE_COUNTRIES + " (" +
                    COLUMN_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                    COLUMN_CONTINENT + " TEXT NOT NULL");";

    private static final String TABLE_QUIZZES_CREATE =
            "CREATE TABLE " + TABLE_QUIZZES + " (" +
                    COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_SCORE + " INTEGER NOT NULL");";

    public CountryQuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_COUNTRIES_CREATE);
        db.execSQL(TABLE_QUIZZES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        onCreate(db);
    }


    // Get all countries
    public List<String[]> getAllCountries() {
        List<String[]> countryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COUNTRIES, null);
        if (cursor.moveToFirst()) {
            do {
                countryList.add(new String[]{cursor.getString(1), cursor.getString(2)});
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return countryList;
    }

    // Insert a quiz result
    public void insertQuizResult(String date, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_SCORE, score);
        db.insert(TABLE_QUIZZES, null, values);
        db.close();
    }

    // Get all quiz results
    public List<String[]> getAllQuizResults() {
        List<String[]> quizResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUIZZES, null);
        if (cursor.moveToFirst()) {
            do {
                quizResults.add(new String[]{cursor.getString(1), cursor.getString(2)});
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return quizResults;
    }
}

