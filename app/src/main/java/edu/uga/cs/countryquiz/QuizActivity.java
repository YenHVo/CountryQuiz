package edu.uga.cs.countryquiz;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.countryquiz.models.Country;
import edu.uga.cs.countryquiz.models.Question;
import edu.uga.cs.countryquiz.models.Quiz;

public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;

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

        onQuizCreated();
    }

    // todo: can make the quiz here and pass the information to the fragment
    // todo: ensure that the country schema matches Country class in which it can use getContinent()
    // todo: optional but modify the Question and Quiz class so that its easier to add questions to the class without needing to create a new arraylist everytime?

    public void onQuizCreated() {
        List<Question> quizQuestions = new ArrayList<Question>();
        for (int i = 0; i < 6; i++) {
            List<Country> options = new ArrayList<Country>();
            Country selectedCountry = new Country();
            // Selecting a country from the database
            // todo: find a way to pull a random country from the database

            // Selecting other random continents from the database
            // todo: add the other continents into the list

            // Randomizing the options
            // todo: finding a way to randomize the options

            Question question = new Question(selectedCountry, options);
            quizQuestions.add(question);
        }

        Quiz quiz = new Quiz(quizQuestions);
        // todo: pass the quiz to the fragment

        QuestionFragment questionFragment = new QuestionFragment();
        Bundle args = new Bundle();
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