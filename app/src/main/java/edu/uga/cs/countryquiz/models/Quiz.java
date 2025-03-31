package edu.uga.cs.countryquiz.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {
    private final List<Question> questions;
    //private Date date;
    private int score;
    private int questionsAnswered;

    public Quiz(List<Question> questions) {
        if (questions.size() != 6) {
            throw new IllegalArgumentException("Must have six questions");
        }
        this.questions = new ArrayList<>(questions);
        this.score = 0;
        this.questionsAnswered = 1;
        // add a this for the date
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public Question getQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            throw new IllegalArgumentException("Index must be between 0 and 5)");
        }
        return questions.get(index);
    }

    public int getScore() {
        return score;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void incrementQuestionsAnswered() {
        if (this.questionsAnswered < 6) {
            this.questionsAnswered++;
        }
    }

    public void recordAnswer(int currentQuestion, int answer) {
        questions.get(currentQuestion).setSelectedAnswer(answer);
        if (questions.get(currentQuestion).isCorrect()) {
            score++;
        }
    }

    public boolean isComplete() {
        return questionsAnswered >= questions.size();
    }
}
