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

/**
 * QuizData is responsible for managing access to the SQLite database,
 * including opening/closing connections and performing CRUD operations
 * related to countries and quiz results.
 */
public class QuizData {

    private SQLiteDatabase db;
    private CountryQuizDBHelper dbHelper;
    private Context context;

    /**
     * Constructor initializes the DB helper and populates the database if necessary.
     * @param context Context from the calling component (e.g., Activity)
     */
    public QuizData(Context context) {
        this.context = context;
        dbHelper = new CountryQuizDBHelper(context);
        open();
        insertCountriesFromCSV();
        close();
    }

    /**
     * Opens the database for writing if it is not already open.
     */
    public void open() {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
            Log.d("QuizData", "Database opened.");
        }
    }

    /**
     * Closes the database if it is open.
     */
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.d("QuizData", "Database closed.");
        }
    }

    /**
     * Provides access to the writable database instance.
     * @return Writable SQLiteDatabase instance
     */
    public SQLiteDatabase getWritableDatabaseInstance() {
        return dbHelper.getWritableDatabase();
    }

    /**
     * Checks if the database is currently open.
     * @return True if DB is open, false otherwise
     */
    public boolean isDBOpen() {
        return db != null && db.isOpen();
    }

    /**
     * Reads a CSV resource file and inserts country-continent data into the database
     * if the countries table is currently empty.
     */
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

    /**
     * Inserts a single country and its continent into the database.
     * @param name Country name
     * @param continent Continent name
     */
    private void insertCountry(String name, String continent) {
        ContentValues values = new ContentValues();
        values.put(CountryQuizDBHelper.COLUMN_COUNTRY_NAME, name);
        values.put(CountryQuizDBHelper.COLUMN_CONTINENT, continent);
        db.insert(CountryQuizDBHelper.TABLE_COUNTRIES, null, values);
    }

    /**
     * Checks whether the countries table is currently empty.
     * @return True if the table has no entries, false otherwise
     */
    public boolean isCountriesTableEmpty() {
        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_COUNTRIES,
                new String[]{CountryQuizDBHelper.COLUMN_COUNTRY_ID},
                null, null, null, null, null);
        boolean isEmpty = cursor.getCount() == 0;
        cursor.close();
        return isEmpty;
    }

    /**
     * Retrieves all country-continent pairs from the database.
     * @return A list of string arrays, each containing a country and its continent
     */
    public List<String[]> getAllCountries() {
        List<String[]> countries = new ArrayList<>();
        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_COUNTRIES,
                new String[]{CountryQuizDBHelper.COLUMN_COUNTRY_NAME, CountryQuizDBHelper.COLUMN_CONTINENT},
                null, null, null, null, null);

        // Reads all rows from the result cursor
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CountryQuizDBHelper.COLUMN_COUNTRY_NAME));
            String continent = cursor.getString(cursor.getColumnIndex(CountryQuizDBHelper.COLUMN_CONTINENT));
            countries.add(new String[]{name, continent});
        }
        cursor.close();
        return countries;
    }

    /**
     * Retrieves all quiz results from the database.
     * @return A list of string arrays containing quiz date and score
     */
    public List<String[]> getAllQuizResults() {
        List<String[]> results = new ArrayList<>();

        Cursor cursor = db.query(CountryQuizDBHelper.TABLE_QUIZZES,
                new String[]{CountryQuizDBHelper.COLUMN_DATE, CountryQuizDBHelper.COLUMN_SCORE},
                null, null, null, null, null);

        // Read all rows from the result cursor
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(CountryQuizDBHelper.COLUMN_DATE));
            int score = cursor.getInt(cursor.getColumnIndex(CountryQuizDBHelper.COLUMN_SCORE));
            results.add(new String[]{date, String.valueOf(score)});
        }
        cursor.close();
        return results;
    }
}


