package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.os.AsyncTask;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.uga.cs.countryquiz.models.Country;
import edu.uga.cs.countryquiz.models.Question;
import edu.uga.cs.countryquiz.models.Quiz;

/**
 * QuizActivity handles the quiz functionality, including fetching country data,
 * generating quiz questions, managing the quiz view, and saving quiz results.
 */
public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;
    private QuizData quizData;
    private List<String[]> countryList;
    private ViewPager2 viewPager;
    private QuizPagerAdapter adapter;

    /**
     * Called when the activity is first created. Sets up the UI and starts the process
     * of fetching country data in the background.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager = findViewById(R.id.viewPager);
        viewPager.setUserInputEnabled(false);

        // start AsyncTask to load countries in background
        new FetchCountries().execute();
    }

    /**
     * Sets up the ViewPager with the quiz data and manages quiz progression.
     * @param quiz The quiz object containing the questions.
     */
    private void setupViewPager(Quiz quiz) {
        adapter = new QuizPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle(),
                quiz
        );

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(adapter);

        // Checks if the quiz is complete
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == quiz.getQuestions().size() - 1) {
                    checkQuizCompletion();
                }
            }
        });
    }

    /**
     * Enables or disables user swiping in the ViewPager.
     * @param enabled True to enable swiping, false to disable it.
     */
    public void setSwipeEnabled(boolean enabled) {
        viewPager.setUserInputEnabled(enabled);
    }

    /**
     * AsyncTask to fetch country data from the database in the background.
     */
    private class FetchCountries extends AsyncTask<Void, Void, List<String[]>> {
        @Override
        protected List<String[]> doInBackground(Void... voids) {

            quizData = new QuizData(QuizActivity.this);
            quizData.open();
            List<String[]> countries = quizData.getAllCountries(); // fetch countries
            quizData.close();
            return countries;
        }

        @Override
        protected void onPostExecute(List<String[]> countries) {
            super.onPostExecute(countries);
            if (countries.isEmpty()) {
                Log.e("QuizActivity", "No countries loaded from database!");
            } else {
                Log.d("QuizActivity", "Countries loaded: " + countries.size());
            }
            countryList = countries;

            // proceed to create the quiz after the data is loaded
            onQuizCreated();
        }
    }

    /**
     * Generates quiz questions using randomly selected countries from the database.
     */
    public void onQuizCreated() {
        List<Question> quizQuestions = new ArrayList<>();
        Random random = new Random();

        // ensure countryList is not empty
        if (countryList == null || countryList.isEmpty()) {
            Log.e("QuizActivity", "No countries found in database!");
            return;
        }

        // Generates 6 quiz questions
        for (int i = 0; i < 6; i++) {
            int correctIndex = random.nextInt(countryList.size());
            String[] correctData = countryList.get(correctIndex);
            Country correctCountry = new Country(correctData[0], correctData[1]);

            Set<Country> options = new HashSet<>();
            options.add(correctCountry);

            Set<Integer> usedIndices = new HashSet<>();
            usedIndices.add(correctIndex);

            Set<String> usedContinents = new HashSet<>();
            usedContinents.add(correctCountry.getContinent());

            // Selects 2 additional unique countries from different continents
            while (options.size() < 3) {
                int randIndex = random.nextInt(countryList.size());
                if (usedIndices.contains(randIndex)) {
                    continue;
                }

                String[] countryData = countryList.get(randIndex);
                Country option = new Country(countryData[0], countryData[1]);

                if (!usedContinents.contains(option.getContinent())) {
                    options.add(option);
                    usedIndices.add(randIndex);
                    usedContinents.add(option.getContinent());
                }
            }

            // Shuffles options in a random order
            List<Country> optionList = new ArrayList<>(options);
            Collections.shuffle(optionList);

            // Adds questions to the quiz
            Question question = new Question(correctCountry, optionList);
            quizQuestions.add(question);
        }

        quiz = new Quiz(quizQuestions);
        setupViewPager(quiz);
    }

    /**
     * Saves the quiz result asynchronously.
     */
    private void saveQuizResult() {
        new SaveQuizResultTask().execute(quiz.getScore());
    }

    /**
     * AsyncTask to save the quiz result to the database.
     */
    private class SaveQuizResultTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int finalScore = params[0];
            quizData.open(); // Open the database

            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            SQLiteDatabase writableDb = quizData.getWritableDatabaseInstance();

            // Prepare score and date to insert into database
            ContentValues values = new ContentValues();
            values.put(CountryQuizDBHelper.COLUMN_DATE, currentDate);
            values.put(CountryQuizDBHelper.COLUMN_SCORE, finalScore);

            // Inserts quiz results into the table
            writableDb.insert(CountryQuizDBHelper.TABLE_QUIZZES, null, values);

            quizData.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    /**
     * Checks if the quiz is complete and enables results display if finished.
     */
    public void checkQuizCompletion() {
        if (quiz.isComplete()) {
            // Enable swipe to results
            saveQuizResult();
            adapter.updateResults();
            setSwipeEnabled(true);
        }
    }
}
