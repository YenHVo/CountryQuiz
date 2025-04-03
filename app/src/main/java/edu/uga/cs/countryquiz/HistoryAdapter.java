package edu.uga.cs.countryquiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<String[]> quizHistory;
    public HistoryAdapter(List<String[]> quizHistory) {
        this.quizHistory = quizHistory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String[] quizResult = quizHistory.get(position);
        holder.dateTextView.setText(quizResult[0]);
        holder.scoreTextView.setText("Score: " + quizResult[1]);
    }

    @Override
    public int getItemCount() {
        return quizHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView scoreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.quizDate);
            scoreTextView = itemView.findViewById(R.id.quizScore);
        }
    }
}
