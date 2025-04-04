package edu.uga.cs.countryquiz.models;

import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a quiz consisting of a fixed set of six multiple-choice questions.
 * This class tracks the questions and the user's score.
 * Implements Serializable for object serialization.
 */
public class Quiz implements Serializable {

    private final List<Question> questions;
    private int score;

    /**
     * Constructs a new Quiz with a given list of six questions.
     *
     * @param questions the list of questions for the quiz (must contain exactly six questions)
     * @throws IllegalArgumentException if the number of questions is not six
     */
    public Quiz(List<Question> questions) {
        if (questions.size() != 6) {
            throw new IllegalArgumentException("Must have six questions");
        }

        this.questions = new ArrayList<>(questions);
        this.score = 0;
    }

    /**
     * Retrieves the list of questions in the quiz.
     *
     * @return a new list containing all quiz questions
     */
    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    /**
     * Retrieves a specific question by its index.
     *
     * @param index the index of the question (0 to 5)
     * @return the Question object at the given index, or null if the index is invalid
     */
    public Question getQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            Log.e("Quiz", "Invalid question index: " + index);
            return null;
        }
        return questions.get(index);
    }

    /**
     * Gets the current score of the user.
     *
     * @return the user's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Records an answer for a specific question in the quiz and updates the score.
     *
     * @param currentQuestion the index of the question being answered
     * @param answer the index of the selected answer choice in the options list
     */
    public void recordAnswer(int currentQuestion, int answer) {
        if (currentQuestion < 0 || currentQuestion >= questions.size()) {
            Log.e("Quiz", "Invalid question index: " + currentQuestion);
            return;
        }

        Question currentQ = questions.get(currentQuestion);

        // Store previous state
        boolean wasAnswered = currentQ.isAnswered();
        boolean previouslyCorrect = currentQ.isCorrect();

        // Update answer
        currentQ.setSelectedAnswer(answer);
        boolean nowCorrect = currentQ.isCorrect();

        if (!wasAnswered) {
            // First-time answering
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

    /**
     * Checks if the quiz has been fully completed.
     * A quiz is considered complete when all questions have been answered.
     *
     * @return true if all questions have been answered, false otherwise
     */
    public boolean isComplete() {
        for (Question question : questions) {
            if (!question.isAnswered()) {
                return false;
            }
        }
        return true;
    }
}
