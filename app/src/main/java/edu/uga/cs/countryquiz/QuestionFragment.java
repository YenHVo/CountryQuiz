package edu.uga.cs.countryquiz;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import edu.uga.cs.countryquiz.models.Question;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COUNTRY_NAME = "country_name";
    private static final String ARG_CORRECT_CONTINENT = "correct_continent";
    private static final String ARG_OPTIONS = "options";
    private static final String ARG_QUESTION_INDEX = "question_index";
    private static final String ARG_TOTAL_QUESTIONS = "total_questions";

    private String countryName;
    private String correctContinent;
    private ArrayList<String> options;
    private int questionIndex;
    private int totalQuestions;
    private int selectedAnswer = -1;

    private TextView questionHeader;
    private TextView questionText;
    private RadioGroup optionsGroup;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(String countryName, String correctContinent,
                                               ArrayList<String> options, int questionIndex, int totalQuestions) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COUNTRY_NAME, countryName);
        args.putString(ARG_CORRECT_CONTINENT, correctContinent);
        args.putStringArrayList(ARG_OPTIONS, options);
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        args.putInt(ARG_TOTAL_QUESTIONS, totalQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            countryName = getArguments().getString(ARG_COUNTRY_NAME);
            correctContinent = getArguments().getString(ARG_CORRECT_CONTINENT);
            options = getArguments().getStringArrayList(ARG_OPTIONS);
            questionIndex = getArguments().getInt(ARG_QUESTION_INDEX);
            totalQuestions = getArguments().getInt(ARG_TOTAL_QUESTIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        questionHeader = view.findViewById(R.id.questionHeader);
        questionText = view.findViewById(R.id.questionText);
        optionsGroup = view.findViewById(R.id.options);

        questionHeader.setText(String.format("Question %d/%d", questionIndex + 1, totalQuestions));
        questionText.setText(String.format("Which continent is %s located in?"));




        return view;
    }
}