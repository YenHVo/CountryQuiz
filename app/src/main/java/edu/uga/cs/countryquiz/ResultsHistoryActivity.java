package edu.uga.cs.countryquiz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ResultsHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ProgressBar progressBar;
    private Button startQuizButton;
    private List<String[]> quizResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_history);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        startQuizButton = findViewById(R.id.startQuizButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(quizResults);
        recyclerView.setAdapter(adapter);

        startQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsHistoryActivity.this, QuizActivity.class);
            startActivity(intent);
        });

        new FetchQuizResults(this).execute();
    }

    private static class FetchQuizResults extends AsyncTask<Void, Void, List<String[]>> {
        private final WeakReference<ResultsHistoryActivity> activityRef;

        FetchQuizResults(ResultsHistoryActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected List<String[]> doInBackground(Void... voids) {
            ResultsHistoryActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) return new ArrayList<>();

            QuizData quizData = new QuizData(activity);
            quizData.open();
            List<String[]> results = quizData.getAllQuizResults();
            quizData.close();
            return results;
        }

        @Override
        protected void onPostExecute(List<String[]> results) {
            ResultsHistoryActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) return;

            activity.progressBar.setVisibility(View.GONE);
            activity.quizResults.clear();
            activity.quizResults.addAll(results);
            activity.adapter.notifyDataSetChanged();
        }
    }
}

