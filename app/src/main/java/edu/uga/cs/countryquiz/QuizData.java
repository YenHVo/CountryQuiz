package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class QuizData {

    private SQLiteDatabase db;
    private CountryQuizDBHelper dbHelper;
    private Context context;

    public QuizData(Context context) {
        this.context = context;
        dbHelper = new CountryQuizDBHelper(context);
        open();
        insertCountriesFromCSV();
        close();
    }


    public void open() {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
            Log.d("QuizData", "Database opened.");
        }
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.d("QuizData", "Database closed.");
        }
    }


    public boolean isDBOpen() {
        return db != null && db.isOpen();
    }

    public void insertCountriesFromCSV() {
        if (!isCountriesTableEmpty()) {
            return; // avoid duplicate entries
        }
        Log.d("QuizData", "Inserting countries from CSV...");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.country_continent)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    insertCountry(data[0].trim(), data[1].trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e("QuizData", "Error reading CSV file", e);
        }
    }

    private void insertCountry(String name, String continent) {
        ContentValues values = new ContentValues();
        values.put(CountryQuizDBHelper.COLUMN_COUNTRY_NAME, name);
        values.put(CountryQuizDBHelper.COLUMN_CONTINENT, continent);
        db.insert(CountryQuizDBHelper.TABLE_COUNTRIES, null, values);
    }

    public boolean isCountriesTableEmpty() {
        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_COUNTRIES,
                new String[]{CountryQuizDBHelper.COLUMN_COUNTRY_ID},
                null, null, null, null, null);
        boolean isEmpty = cursor.getCount() == 0;
        cursor.close();
        return isEmpty;
    }


    public List<String[]> getAllCountries() {
        List<String[]> countries = new ArrayList<>();
        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_COUNTRIES,
                new String[]{CountryQuizDBHelper.COLUMN_COUNTRY_NAME, CountryQuizDBHelper.COLUMN_CONTINENT},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CountryQuizDBHelper.COLUMN_COUNTRY_NAME));
            String continent = cursor.getString(cursor.getColumnIndex(CountryQuizDBHelper.COLUMN_CONTINENT));
            countries.add(new String[]{name, continent});
        }
        cursor.close();
        return countries;
    }
    public List<String[]> getAllQuizResults() {
        List<String[]> results = new ArrayList<>();


        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_QUIZZES,
                new String[]{CountryQuizDBHelper.COLUMN_DATE, CountryQuizDBHelper.COLUMN_SCORE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(CountryQuizDBHelper.COLUMN_DATE));
            int score = cursor.getInt(cursor.getColumnIndex(CountryQuizDBHelper.COLUMN_SCORE));
            results.add(new String[]{date, String.valueOf(score)});
        }
        cursor.close();
        return results;
    }
}


