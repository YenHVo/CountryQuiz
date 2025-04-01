package edu.uga.cs.countryquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import edu.uga.cs.countryquiz.models.Question;
import edu.uga.cs.countryquiz.models.Quiz;
import edu.uga.cs.countryquiz.models.Country;

public class QuestionFragment extends Fragment {

    private static final String ARG_QUIZ = "quiz";
    private static final String ARG_POSITION = "position";
    private Quiz quiz;
    private int position;

    private TextView questionHeader;
    private TextView questionText;
    private RadioGroup optionsRadioGroup;


    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(Quiz quiz, int position) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUIZ, quiz);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quiz = (Quiz) getArguments().getSerializable(ARG_QUIZ);
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionHeader = view.findViewById(R.id.questionHeader);
        questionText = view.findViewById(R.id.questionText);
        optionsRadioGroup = view.findViewById(R.id.options);

        Question currentQuestion = quiz.getQuestion(position);
        List<Country> answerOptions = currentQuestion.getOptions();

        questionHeader.setText("Question " + (position + 1) + "/" + quiz.getQuestions().size());
        questionText.setText("What continent is " + currentQuestion.getCountry().getName() + " in?");

        for (int i = 0; i < optionsRadioGroup.getChildCount() && i < answerOptions.size(); i++) {
            ((RadioButton) optionsRadioGroup.getChildAt(i)).setText((i + 1) + ". " + answerOptions.get(i).getContinent());
        }


        if (currentQuestion.getSelectedAnswer() != -1) {
            ((RadioButton) optionsRadioGroup.getChildAt(currentQuestion.getSelectedAnswer()))
                    .setChecked(true);
        }

        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedIndex = group.indexOfChild(view.findViewById(checkedId));
            quiz.recordAnswer(position, selectedIndex);

            if (getActivity() instanceof QuizActivity) {
                QuizActivity activity = (QuizActivity) getActivity();


                boolean isCorrect = answerOptions.get(selectedIndex).getContinent()
                        .equals(currentQuestion.getCountry().getContinent());


                activity.updateScore(isCorrect);
                activity.setSwipeEnabled(true);


                if (position == quiz.getQuestions().size() - 1) {
                    activity.checkQuizCompletion();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof QuizActivity) {
            ((QuizActivity) getActivity()).setSwipeEnabled(
                    quiz.getQuestion(position).isAnswered()
            );
        }
    }
}
