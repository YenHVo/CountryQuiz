package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * ResultFragment displays the quiz results after the user finishes all questions.
 * It shows the user's score, the percentage of correct answers, and allows navigation
 * to retry the quiz or view the quiz history.
 */
public class ResultFragment extends Fragment {

    private static final String ARG_SCORE = "score";
    private static final String ARG_TOTAL = "total";
    private int score;
    private int totalQuestions;

    /**
     * Default empty constructor.
     */
    public ResultFragment() {
    }

    /**
     * Creates a new instance of ResultFragment with score and total questions.
     *
     * @param score The score the user achieved.
     * @param totalQuestions The total number of quiz questions.
     * @return A new instance of ResultFragment with arguments set.
     */
    public static ResultFragment newInstance(int score, int totalQuestions) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        args.putInt(ARG_TOTAL, totalQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views.
     * @param container The parent view that the fragment's UI should attach to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed.
     * @return The root view for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        // Retrieve score and total from arguments
        if (getArguments() != null) {
            score = getArguments().getInt(ARG_SCORE, 0);
            totalQuestions = getArguments().getInt(ARG_TOTAL, 1);
        }

        // Initialization
        TextView percentageText = view.findViewById(R.id.percentageText);
        TextView scoreText = view.findViewById(R.id.scoreText);
        Button retryButton = view.findViewById(R.id.retryButton);
        Button historyButton = view.findViewById(R.id.historyButton);

        // Calculate and display the quiz percentage score
        int percentage = (int) (((float) score / totalQuestions) * 100);
        percentageText.setText(percentage + "%");

        // Displays text on the result screen
        scoreText.setText(String.format("You got %d out of %d questions correct!", score, totalQuestions));

        // Restart the quiz when the "Try Again" button is clicked
        retryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // Navigate to the history screen when the "View History" button is clicked
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        });

        return view;
    }
}