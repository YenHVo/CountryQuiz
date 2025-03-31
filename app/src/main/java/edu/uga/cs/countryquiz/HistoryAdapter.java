package edu.uga.cs.countryquiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<String[]> quizResults;

    public HistoryAdapter(List<String[]> quizResults) {
        this.quizResults = quizResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] result = quizResults.get(position);
        holder.dateTextView.setText("Date: " + result[0]);
        holder.scoreTextView.setText("Score: " + result[1]);
    }

    @Override
    public int getItemCount() {
        return quizResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, scoreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}
