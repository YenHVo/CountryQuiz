package edu.uga.cs.countryquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CountryQuizDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "countryquiz.db";
    private static final int DATABASE_VERSION = 1;

    // table for storing countries
    public static final String TABLE_COUNTRIES = "countries";
    public static final String COLUMN_COUNTRY_ID = "id";
    public static final String COLUMN_COUNTRY_NAME = "name";
    public static final String COLUMN_CONTINENT = "continent";

    // table for storing quiz results
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String COLUMN_QUIZ_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SCORE = "score";

    private static final String TABLE_COUNTRIES_CREATE =
            "CREATE TABLE " + TABLE_COUNTRIES + " (" +
                    COLUMN_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                    COLUMN_CONTINENT + " TEXT NOT NULL);";

    private static final String TABLE_QUIZZES_CREATE =
            "CREATE TABLE " + TABLE_QUIZZES + " (" +
                    COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_SCORE + " INTEGER NOT NULL);";

    private Context context;

    public CountryQuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;  // Save the context for later use
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_COUNTRIES_CREATE);
        db.execSQL(TABLE_QUIZZES_CREATE);

        // populate the countries table from the CSV
        QuizData quizData = new QuizData(context);
        quizData.open();

        // insert data from the CSV only if the countries table is empty
        if (quizData.isCountriesTableEmpty()) {
            quizData.insertCountriesFromCSV();
        }

        quizData.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        onCreate(db);
    }
}
