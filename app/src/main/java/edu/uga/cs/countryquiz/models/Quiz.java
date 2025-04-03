package edu.uga.cs.countryquiz.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {
    private final List<Question> questions;
    private int score;

    public Quiz(List<Question> questions) {
        if (questions.size() != 6) {
            throw new IllegalArgumentException("Must have six questions");
        }

        this.questions = new ArrayList<>(questions);
        this.score = 0;
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public Question getQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            Log.e("Quiz", "Invalid question index: " + index);
            return null;
        }
        return questions.get(index);
    }

    public int getScore() {
        return score;
    }

    public void recordAnswer(int currentQuestion, int answer) {
        if (currentQuestion < 0 || currentQuestion >= questions.size()) {
            Log.e("Quiz", "Invalid question index: " + currentQuestion);
            return;
        }
        Question currentQ = questions.get(currentQuestion);

        // Store previous state
        boolean previouslyCorrect = currentQ.isCorrect();
        boolean wasAnswered = currentQ.getAlreadyAnswered();

        // Update answer
        currentQ.setSelectedAnswer(answer);
        boolean nowCorrect = currentQ.isCorrect();

        if (!wasAnswered) {
            currentQ.setAlreadyAnswered(true);
            if (nowCorrect) {
                score++;
            }
        } else {
            // Handle answer changes
            if (previouslyCorrect && !nowCorrect) {
                score--;
            } else if (!previouslyCorrect && nowCorrect) {
                score++;
            }
        }
    }

    public boolean isComplete() {
        for (Question question : questions) {
            if (!question.isAnswered()) {
                return false;
            }
        }
        return true;
    }
}
