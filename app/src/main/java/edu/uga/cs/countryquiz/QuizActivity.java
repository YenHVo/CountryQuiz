package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.os.AsyncTask;
import android.view.View;

import edu.uga.cs.countryquiz.models.Country;
import edu.uga.cs.countryquiz.models.Question;
import edu.uga.cs.countryquiz.models.Quiz;

public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;
    private QuizData quizData;
    private int score = 0;
    private List<String[]> countryList;

    private ViewPager2 viewPager;
    private QuizPagerAdapter adapter;

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

    private void setupViewPager(Quiz quiz) {
        adapter = new QuizPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle(),
                quiz
        );
        viewPager.setOrientation(
                ViewPager2.ORIENTATION_HORIZONTAL );
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == quiz.getQuestions().size()) {
                    saveQuizResult();
                }
            }
        });
    }

    public void setSwipeEnabled (boolean enabled) {
        viewPager.setUserInputEnabled(enabled);
    }

    // AsyncTask to fetch countries from database in background
    private class FetchCountries extends AsyncTask <Void, Void, List<String[]>> {
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

    // method to create the quiz after countries are loaded
    public void onQuizCreated() {
        List<Question> quizQuestions = new ArrayList<>();
        Random random = new Random();

        // ensure countryList is not empty
        if (countryList == null || countryList.isEmpty()) {
            Log.e("QuizActivity", "No countries found in database!");
            return;
        }

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

            List<Country> optionList = new ArrayList<>(options);
            Collections.shuffle(optionList);

            Question question = new Question(correctCountry, optionList);
            quizQuestions.add(question);
        }

        quiz = new Quiz(quizQuestions);
        setupViewPager(quiz);
    }
    public void updateScore(boolean isCorrect) {
        if (isCorrect) {
            score++;
            Log.d("QuizActivity", "Current Score: " + score);
        }
    }
    private void saveQuizResult() {
        new SaveQuizResultTask().execute(score);
    }

    private class SaveQuizResultTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int finalScore = params[0];
            quizData.open(); // Open the database


            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


            SQLiteDatabase writableDb = quizData.getWritableDatabaseInstance();

            ContentValues values = new ContentValues();
            values.put(CountryQuizDBHelper.COLUMN_DATE, currentDate);
            values.put(CountryQuizDBHelper.COLUMN_SCORE, finalScore);
            writableDb.insert(CountryQuizDBHelper.TABLE_QUIZZES, null, values);

            quizData.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void checkQuizCompletion() {
        if (quiz.isComplete() && viewPager.getCurrentItem() == quiz.getQuestions().size() - 1) {
            // Enable swipe to results
            adapter.showResults();
            setSwipeEnabled(true);
        }
    }

}
