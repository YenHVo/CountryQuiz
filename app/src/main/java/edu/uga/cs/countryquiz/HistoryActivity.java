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

/**
 * HistoryActivity displays the quiz history stored in the local database.
 * Users can return to the main menu or start a new quiz from this screen.
 */
public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private QuizData quizData;

    /**
     * Called when the activity is created. Initializes UI components and fetches quiz history.
     *
     * @param savedInstanceState The previously saved instance state (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        quizData = new QuizData(this);

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialization
        Button returnMainButton = findViewById(R.id.button_return_main);
        Button startNewQuizButton = findViewById(R.id.button_start_new_quiz);

        // Allows the user to return to the main screen
        returnMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Allows the user to start a new quiz
        startNewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Fetch quiz history asynchronously
        new FetchQuizHistoryTask().execute();
    }

    /**
     * AsyncTask to fetch quiz history from the database in the background.
     */
    private class FetchQuizHistoryTask extends AsyncTask<Void, Void, List<String[]>> {

        /**
         * Runs in the background to fetch quiz history from the database.
         *
         * @param voids No parameters are needed.
         * @return A list of quiz results, where each entry is a String array containing quiz details.
         */
        @Override
        protected List<String[]> doInBackground(Void... voids) {
            quizData.open();
            List<String[]> quizHistory = quizData.getAllQuizResults();
            quizData.close();
            return quizHistory;
        }

        /**
         * Runs on the UI thread after background execution is complete.
         * Updates the RecyclerView with fetched quiz history.
         *
         * @param result The list of quiz results retrieved from the database.
         */
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
