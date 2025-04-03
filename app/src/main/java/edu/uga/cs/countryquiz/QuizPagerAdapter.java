package edu.uga.cs.countryquiz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.uga.cs.countryquiz.models.Quiz;

public class QuizPagerAdapter extends FragmentStateAdapter {
    private final Quiz quiz;
    public QuizPagerAdapter (
            FragmentManager fragmentManager,
            Lifecycle lifecycle,
            Quiz quiz) {
        super( fragmentManager, lifecycle );
        this.quiz = quiz;
    }

    @Override
    public Fragment createFragment(int position) {
        if (position < quiz.getQuestions().size()) {
            return QuestionFragment.newInstance(quiz, position);
        } else {
            return ResultFragment.newInstance(quiz.getScore(), quiz.getQuestions().size());
        }
    }

    @Override
    public int getItemCount() {
        return quiz.getQuestions().size() + 1;
    }

    public void updateResults() {
        notifyItemChanged(quiz.getQuestions().size());
    }
}