package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.uga.cs.countryquiz.AsyncTask;
import edu.uga.cs.countryquiz.QuestionFragment;
import edu.uga.cs.countryquiz.R;
import edu.uga.cs.countryquiz.models.Country;
import edu.uga.cs.countryquiz.models.Question;
import edu.uga.cs.countryquiz.models.Quiz;

public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;
    private QuizData quizData;
    private List<String[]> countryList;

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

        // start AsyncTask to load countries in background
        new FetchCountries().execute();
    }

    // AsyncTask to fetch countries from database in background
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
            countryList = countries;

            // Proceed to create the quiz after the data is loaded
            onQuizCreated();
        }
    }

    // Method to create the quiz after countries are loaded
    public void onQuizCreated() {
        List<Question> quizQuestions = new ArrayList<>();
        Random random = new Random();

        // ensure countryList is not empty
        if (countryList == null || countryList.isEmpty()) {
            Log.e("QuizActivity", "No countries found in database!");
            return; // Prevent crash
        }

        for (int i = 0; i < 6; i++) {
            // pick a random country as the correct answer
            int correctIndex = random.nextInt(countryList.size());
            String[] correctData = countryList.get(correctIndex);
            Country correctCountry = new Country(correctData[0], correctData[1]);

            Set<Country> options = new HashSet<>();
            options.add(correctCountry);

            while (options.size() < 3) {
                int randIndex = random.nextInt(countryList.size());
                String[] countryData = countryList.get(randIndex);
                Country option = new Country(countryData[0], countryData[1]);

                // ensure no duplicate answers & different continents
                if (!option.getContinent().equals(correctCountry.getContinent()) && !options.contains(option)) {
                    options.add(option);
                }
            }

            // convert HashSet to List & Shuffle options
            List<Country> optionList = new ArrayList<>(options);
            Collections.shuffle(optionList);

            // create question and add it to the quiz
            Question question = new Question(correctCountry, optionList);
            quizQuestions.add(question);
        }

        quiz = new Quiz(quizQuestions);
        // pass the quiz to the fragment
        QuestionFragment questionFragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable("quiz", (Serializable) quiz);
        questionFragment.setArguments(args);

        // handle fragment transaction to show quiz
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, questionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
