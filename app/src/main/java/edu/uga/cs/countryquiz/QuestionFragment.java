package edu.uga.cs.countryquiz;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.List;

import edu.uga.cs.countryquiz.models.Question;
import edu.uga.cs.countryquiz.models.Quiz;
import edu.uga.cs.countryquiz.models.Country;

/**
 * A Fragment that represents a single question in the quiz.
 * It displays the question, answer options, and handles user selection.
 */
public class QuestionFragment extends Fragment {

    private static final String ARG_QUIZ = "quiz";
    private static final String ARG_POSITION = "position";
    private Quiz quiz;
    private int position;

    private TextView questionHeader;
    private TextView questionText;
    private RadioGroup optionsRadioGroup;

    /**
     * Default constructor.
     */
    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Creating a new instance of QuestionFragment.
     *
     * @param quiz The Quiz object containing the questions.
     * @param position The index of the question in the quiz.
     * @return A new instance of QuestionFragment.
     */
    public static QuestionFragment newInstance(Quiz quiz, int position) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUIZ, quiz);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is created. Retrieves arguments and initializes variables.
     *
     * @param savedInstanceState The previously saved state of the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quiz = (Quiz) getArguments().getSerializable(ARG_QUIZ);
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    /**
     * Inflates the layout for the fragment.
     *
     * @param inflater The LayoutInflater used to inflate views.
     * @param container The parent view the fragment's UI should be attached to.
     * @param savedInstanceState The previously saved state of the fragment (if any).
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    /**
     * Called after the fragment's view has been created.
     * Sets up UI components and handles user interactions.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState The previously saved state of the fragment (if any).
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialization
        questionHeader = view.findViewById(R.id.questionHeader);
        questionText = view.findViewById(R.id.questionText);
        optionsRadioGroup = view.findViewById(R.id.options);

        // Gets the question and answer options
        Question currentQuestion = quiz.getQuestion(position);
        List<Country> answerOptions = currentQuestion.getOptions();

        // Displays the text
        questionHeader.setText("Question " + (position + 1) + "/" + quiz.getQuestions().size());
        questionText.setText("What continent is " + currentQuestion.getCountry().getName() + " in?");

        // Populates the answer choices
        for (int i = 0; i < optionsRadioGroup.getChildCount() && i < answerOptions.size(); i++) {
            ((RadioButton) optionsRadioGroup.getChildAt(i)).setText((i + 1) + ". " + answerOptions.get(i).getContinent());
        }

        // Restores previously selected answer
        if (currentQuestion.getSelectedAnswer() != -1) {
            ((RadioButton) optionsRadioGroup.getChildAt(currentQuestion.getSelectedAnswer()))
                    .setChecked(true);
        }

        // Handles answer selection
        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedIndex = group.indexOfChild(view.findViewById(checkedId));
            quiz.recordAnswer(position, selectedIndex);
            Log.d("QuestionFragment", "Score: " + quiz.getScore());

            // Enables swipe navigation
            if (getActivity() instanceof QuizActivity) {
                QuizActivity activity = (QuizActivity) getActivity();
                activity.setSwipeEnabled(true);
                activity.checkQuizCompletion();
            }
        });
    }

    /**
     * Called when the fragment is resumed.
     * Ensures swipe navigation is enabled if the question has been answered.
     */
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
