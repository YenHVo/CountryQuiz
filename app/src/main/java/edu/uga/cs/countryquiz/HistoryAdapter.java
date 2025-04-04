package edu.uga.cs.countryquiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adapter for displaying quiz history in a RecyclerView.
 * It binds quiz date and score information to the UI components.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<String[]> quizHistory;

    /**
     * Constructs a new HistoryAdapter with the given quiz history data.
     *
     * @param quizHistory A list of quiz results, where each result contains date and score.
     */
    public HistoryAdapter(List<String[]> quizHistory) {
        this.quizHistory = quizHistory;
    }

    /**
     * Creates and returns a ViewHolder for displaying a quiz history item.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of the view (not used here as all items are the same).
     * @return A ViewHolder for a quiz history item.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds quiz history data to the ViewHolder.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the data in the list.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String[] quizResult = quizHistory.get(position);
        holder.dateTextView.setText(quizResult[0]);
        holder.scoreTextView.setText("Score: " + quizResult[1]);
    }

    /**
     * Returns the number of quiz history items.
     *
     * @return The total number of quiz history entries.
     */
    @Override
    public int getItemCount() {
        return quizHistory.size();
    }

    /**
     * ViewHolder class that holds the views for displaying a single quiz history item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView scoreTextView;

        /**
         * Constructs a ViewHolder with the given itemView.
         *
         * @param itemView The view representing a single quiz history item.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.quizDate);
            scoreTextView = itemView.findViewById(R.id.quizScore);
        }
    }
}
