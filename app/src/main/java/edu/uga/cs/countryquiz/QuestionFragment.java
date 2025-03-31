package edu.uga.cs.countryquiz;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.countryquiz.models.Question;
import edu.uga.cs.countryquiz.models.Quiz;
import edu.uga.cs.countryquiz.models.Country;

public class QuestionFragment extends Fragment {

    private static final String ARG_QUIZ = "quiz";
    private Quiz quiz;
    private int questionsAnswered = 0;

    private TextView questionHeader;
    private TextView questionText;
    private RadioGroup optionsRadioGroup;
    private RadioButton optionOne;
    private RadioButton optionTwo;
    private RadioButton optionThree;
    private RadioButton optionFour;
    private ViewPager2 viewPager;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(Quiz quiz) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUIZ, (Serializable) quiz);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            quiz = (Quiz) getArguments().getSerializable(ARG_QUIZ);
        }

        questionHeader = view.findViewById(R.id.questionHeader);
        questionText = view.findViewById(R.id.questionText);
        optionsRadioGroup = view.findViewById(R.id.options);

        viewPager = requireActivity().findViewById(R.id.viewPager);
        int currentPosition = viewPager.getCurrentItem();
        Question currentQuestion = quiz.getQuestion(currentPosition);
        List<Country> answerOptions = currentQuestion.getOptions();

        questionHeader.setText("Question " + quiz.getQuestionsAnswered() + "/" + quiz.getQuestions().size());
        questionText.setText("What continent is " + currentQuestion.getCountry().getName() + " in?");

        for (int i = 0; i < optionsRadioGroup.getChildCount(); i++) {
            ((RadioButton) optionsRadioGroup.getChildAt(i)).setText(answerOptions.get(i).getContinent());
        }

        if (currentQuestion.getSelectedAnswer() != -1) {
            ((RadioButton) optionsRadioGroup.getChildAt(currentQuestion.getSelectedAnswer())).setChecked(true);
        }

        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedIndex = group.indexOfChild(view.findViewById(checkedId));
            quiz.recordAnswer(currentPosition, selectedIndex);

            if (currentPosition < quiz.getQuestions().size() - 1) {
                viewPager.setCurrentItem(currentPosition +1, true);
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected (int position) {
                super.onPageSelected(position);
                if (position == quiz.getQuestions().size() - 1 && quiz.isComplete()) {
                    //showResults();
                    // todo: make a results fragment? or activity?
                }
            }
        });
    }
}