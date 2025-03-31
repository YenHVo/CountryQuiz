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
        quizData = new QuizData(this);
        quizData.open();
        countryList = quizData.getAllCountries();
        quizData.close();

        onQuizCreated();
    }

    // todo: can make the quiz here and pass the information to the fragment
    // todo: ensure that the country schema matches Country class in which it can use getContinent()
    // todo: optional but modify the Question and Quiz class so that its easier to add questions to the class without needing to create a new arraylist everytime?

    public void onQuizCreated() {
        List<Question> quizQuestions = new ArrayList<>();
        Random random = new Random();

        // ensure countryList is not empty
        if (countryList == null || countryList.isEmpty()) {
            Log.e("QuizActivity", "No countries found in database!");
            return; // prevent crash
        }

        for (int i = 0; i < 6; i++) {
            // pick a random country as the correct answer
            int correctIndex = random.nextInt(countryList.size());
            String[] correctData = countryList.get(correctIndex);
            Country correctCountry = new Country(correctData[0], correctData[1]);

            // select 3 incorrect answers from different continents
            Set<Country> options = new HashSet<>();
            options.add(correctCountry);

            while (options.size() < 3) {
                int randIndex = random.nextInt(countryList.size());
                String[] countryData = countryList.get(randIndex);
                Country option = new Country(countryData[0], countryData[1]);

                // Ensure no duplicate answers & different continents
                if (!option.getContinent().equals(correctCountry.getContinent()) && !options.contains(option)) {
                    options.add(option);
                }
            }

            // convert HashSet to List & Shuffle options
            List<Country> optionList = new ArrayList<>(options);
            Collections.shuffle(optionList);

            // create Question and add it to the quiz
            Question question = new Question(correctCountry, optionList);
            quizQuestions.add(question);
        }

        quiz = new Quiz(quizQuestions);
        // todo: pass the quiz to the fragment

        QuestionFragment questionFragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable("quiz", (Serializable) quiz);
        // todo: find a way to pass quiz into args. maybe: args.putSerializable("quiz", (Serializable) quiz);
        questionFragment.setArguments(args);

        // todo: portrait and landscape fixes
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, questionFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // todo: double check this code with his own, not 100%
    }
}