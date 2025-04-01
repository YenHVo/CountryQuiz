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
    private QuizData quizData;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            int score = getArguments().getInt(ARG_SCORE);
            int total = getArguments().getInt(ARG_TOTAL);
            int percentage = (int) (((float) score / total) * 100);

            TextView percentageText = view.findViewById(R.id.percentageText);
            TextView scoreText = view.findViewById(R.id.scoreText);
            Button retryButton = view.findViewById(R.id.retryButton);
            Button historyButton = view.findViewById(R.id.historyButton);

            percentageText.setText(percentage + "%");
            scoreText.setText(String.format("You got %d out of %d questions correct!", score, total));

            retryButton.setOnClickListener(v -> {
                requireActivity().recreate();
            });

            historyButton.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            });
        }
    }

    // AsyncTask to fetch quiz history
    private class FetchQuizHistory extends AsyncTask<Void, Void, List<String[]>> {

        @Override
        protected List<String[]> doInBackground(Void... voids) {
            quizData = new QuizData(requireContext());
            quizData.open();
            List<String[]> results = quizData.getAllQuizResults();
            quizData.close();
            return results;
        }

        @Override
        protected void onPostExecute(List<String[]> results) {
            super.onPostExecute(results);
            for (String[] result : results) {
                String date = result[0];
                String score = result[1];

                System.out.println("Quiz on " + date + " scored: " + score);
            }
        }
    }
}
