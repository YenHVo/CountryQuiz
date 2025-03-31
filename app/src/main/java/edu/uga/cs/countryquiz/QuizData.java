package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizData {

    private SQLiteDatabase db;
    private CountryQuizDBHelper dbHelper;
    private Context context;

    public QuizData(Context context) {
        this.context = context;
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

    // insert countries from CSV into the db
    public void insertCountriesFromCSV() {
        // Read and parse the CSV file
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.country_continent)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    String countryName = data[0].trim();
                    String continent = data[1].trim();
                    insertCountry(countryName, continent);  // Insert each country and continent into the DB
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e("QuizData", "Error reading CSV file", e);
        }
    }

    // insert a single country into the db
    private void insertCountry(String name, String continent) {
        ContentValues values = new ContentValues();
        values.put(CountryQuizDBHelper.COLUMN_COUNTRY_NAME, name);
        values.put(CountryQuizDBHelper.COLUMN_CONTINENT, continent);
        db.insert(CountryQuizDBHelper.TABLE_COUNTRIES, null, values);
    }

    // retrieve all countries from the db
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

    // retrieve all quiz results from the db
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

    // insert a new quiz result into the db
    public void insertQuizResult(String date, int score) {
        ContentValues values = new ContentValues();
        values.put(CountryQuizDBHelper.COLUMN_DATE, date);
        values.put(CountryQuizDBHelper.COLUMN_SCORE, score);
        db.insert(CountryQuizDBHelper.TABLE_QUIZZES, null, values);
    }

    // check if the countries table is empty
    public boolean isCountriesTableEmpty() {
        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_COUNTRIES,
                new String[]{CountryQuizDBHelper.COLUMN_COUNTRY_ID},
                null, null, null, null, null);
        boolean isEmpty = cursor.getCount() == 0;
        cursor.close();
        return isEmpty;
    }
}
