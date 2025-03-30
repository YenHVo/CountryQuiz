package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class QuizData {

    private SQLiteDatabase db;
    private CountryQuizDBHelper dbHelper;

    public QuizData(Context context) {
        dbHelper = new CountryQuizDBHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean isDBOpen() {
        return db != null && db.isOpen();
    }

    // Retrieve all countries from the database
    public List<String[]> getAllCountries() {
        List<String[]> countryList = new ArrayList<>();
        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_COUNTRIES,
                new String[]{CountryQuizDBHelper.COLUMN_COUNTRY_NAME, CountryQuizDBHelper.COLUMN_CONTINENT},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                countryList.add(new String[]{cursor.getString(0), cursor.getString(1)});
            } while (cursor.moveToNext());
        }
        cursor.close();
        return countryList;
    }

    // Retrieve all quiz results
    public List<String[]> getAllQuizResults() {
        List<String[]> quizResults = new ArrayList<>();
        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_QUIZZES,
                new String[]{CountryQuizDBHelper.COLUMN_DATE, CountryQuizDBHelper.COLUMN_SCORE},
                null, null, null, null, CountryQuizDBHelper.COLUMN_DATE + " DESC");
        if (cursor.moveToFirst()) {
            do {
                quizResults.add(new String[]{cursor.getString(0), cursor.getString(1)});
            } while (cursor.moveToNext());
        }
        cursor.close();
        return quizResults;
    }

    // Insert a new quiz result
    public void insertQuizResult(String date, int score) {
        ContentValues values = new ContentValues();
        values.put(CountryQuizDBHelper.COLUMN_DATE, date);
        values.put(CountryQuizDBHelper.COLUMN_SCORE, score);
        db.insert(CountryQuizDBHelper.TABLE_QUIZZES, null, values);
    }
}
