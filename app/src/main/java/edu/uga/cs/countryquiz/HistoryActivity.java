package edu.uga.cs.countryquiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private QuizData quizData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        quizData = new QuizData(this);

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch quiz history asynchronously
        new FetchQuizHistoryTask().execute();
    }

    // AsyncTask to fetch quiz history from the database
    private class FetchQuizHistoryTask extends AsyncTask<Void, Void, List<String[]>> {

        @Override
        protected List<String[]> doInBackground(Void... voids) {
            quizData.open();
            List<String[]> quizHistory = quizData.getAllQuizResults();
            quizData.close();
            return quizHistory;
        }

        @Override
        protected void onPostExecute(List<String[]> result) {
            super.onPostExecute(result);
            if (result.isEmpty()) {
                Toast.makeText(HistoryActivity.this, "No quiz history found.", Toast.LENGTH_SHORT).show();
            } else {
                historyAdapter = new HistoryAdapter(result);
                historyRecyclerView.setAdapter(historyAdapter);
            }
        }
    }
}
