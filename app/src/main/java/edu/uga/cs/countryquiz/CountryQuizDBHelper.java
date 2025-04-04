package edu.uga.cs.countryquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * CountryQuizDBHelper is a helper class that manages the creation and upgrade
 * of the local SQLite database used in the Country Quiz app.
 *
 * It defines two tables:
 * - countries: stores country names and their respective continents
 * - quizzes: stores historical quiz scores and timestamps
 */
public class CountryQuizDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "countryquiz.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_COUNTRIES = "countries";
    public static final String COLUMN_COUNTRY_ID = "id";
    public static final String COLUMN_COUNTRY_NAME = "name";
    public static final String COLUMN_CONTINENT = "continent";

    public static final String TABLE_QUIZZES = "quizzes";
    public static final String COLUMN_QUIZ_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SCORE = "score";

    // SQL Statement to create the 'countries' table
    private static final String TABLE_COUNTRIES_CREATE =
            "CREATE TABLE " + TABLE_COUNTRIES + " (" +
                    COLUMN_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                    COLUMN_CONTINENT + " TEXT NOT NULL);";

    // SQL Statement to create the 'quizzes' table
    private static final String TABLE_QUIZZES_CREATE =
            "CREATE TABLE " + TABLE_QUIZZES + " (" +
                    COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_SCORE + " INTEGER NOT NULL);";

    /**
     * Constructor for the CountryQuizDBHelper.
     *
     * @param context The application context.
     */
    public CountryQuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * This method creates the necessary tables for the app.
     *
     * @param db The SQLite database instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_COUNTRIES_CREATE);
        db.execSQL(TABLE_QUIZZES_CREATE);
    }

    /**
     * Called when the database needs to be upgraded (e.g., version change).
     * Drops the existing tables and recreates them.
     *
     * @param db The SQLite database instance.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("CountryQuizDBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        onCreate(db); // Recreate database
    }

}
