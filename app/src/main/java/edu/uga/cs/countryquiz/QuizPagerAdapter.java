package edu.uga.cs.countryquiz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.uga.cs.countryquiz.models.Quiz;

/**
 * Adapter class for managing the quiz pages in a ViewPager2.
 * It provides fragments for each quiz question and a final result page.
 */
public class QuizPagerAdapter extends FragmentStateAdapter {
    private final Quiz quiz;

    /**
     * Constructor for QuizPagerAdapter.
     *
     * @param fragmentManager FragmentManager for handling fragments
     * @param lifecycle Lifecycle of the hosting activity or fragment
     * @param quiz The Quiz instance to be displayed
     */
    public QuizPagerAdapter (
            FragmentManager fragmentManager,
            Lifecycle lifecycle,
            Quiz quiz) {
        super( fragmentManager, lifecycle );
        this.quiz = quiz;
    }

    /**
     * Creates a fragment based on the position in the ViewPager2.
     *
     * @param position The position of the page to be displayed
     * @return A Fragment representing either a quiz question or the result page
     */
    @Override
    public Fragment createFragment(int position) {
        // Returns the question fragment for the quiz question at the current position
        if (position < quiz.getQuestions().size()) {
            return QuestionFragment.newInstance(quiz, position);
        } else {
            // Returns the result fragment once all questions are completed
            return ResultFragment.newInstance(quiz.getScore(), quiz.getQuestions().size());
        }
    }

    /**
     * Returns the total number of pages (questions + 1 for the result page).
     *
     * @return The total count of fragments in the adapter
     */
    @Override
    public int getItemCount() {
        return quiz.getQuestions().size() + 1;
    }

    /**
     * Notifies the adapter to update the result page.
     * Should be called when quiz results are ready to be displayed.
     */
    public void updateResults() {
        notifyItemChanged(quiz.getQuestions().size());
    }
}