package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    private static final String ARG_SCORE = "score";
    private static final String ARG_TOTAL = "total";

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(int score, int totalQuestions) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        args.putInt(ARG_TOTAL, totalQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView percentageText = view.findViewById(R.id.percentageText);
        TextView scoreText = view.findViewById(R.id.scoreText);
        Button retryButton = view.findViewById(R.id.retryButton);
        Button historyButton = view.findViewById(R.id.historyButton);

        if (getArguments() != null) {
            int score = getArguments().getInt(ARG_SCORE, 0);
            int total = getArguments().getInt(ARG_TOTAL, 1); // Avoid division by zero

            int percentage = (int) (((float) score / total) * 100);
            percentageText.setText(percentage + "%");
            scoreText.setText(String.format("You got %d out of %d questions correct!", score, total));
        }

        retryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        });
    }
}