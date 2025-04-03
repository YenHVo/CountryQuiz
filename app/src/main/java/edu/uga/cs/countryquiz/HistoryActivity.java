package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;

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

        Button returnMainButton = findViewById(R.id.button_return_main);
        Button startNewQuizButton = findViewById(R.id.button_start_new_quiz);
        returnMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        startNewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // fetch quiz history asynchronously
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
