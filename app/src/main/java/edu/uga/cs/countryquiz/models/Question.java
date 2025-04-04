package edu.uga.cs.countryquiz.models;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a quiz question where a user selects the continent of a given country
 * from a list of multiple-choice options.
 * This class implements Serializable for object serialization.
 */
public class Question implements Serializable {

    private final Country country;
    private final List<Country> options;
    private int selectedAnswer;

    /**
     * Constructs a new Question with a given country and a list of possible answer choices.
     * Initially, the question is unanswered.
     *
     * @param country the country chosen
     * @param options the list of answer choices (countries)
     */
    public Question(Country country, List<Country> options) {
        this.country = country;
        this.options = options;
        this.selectedAnswer = -1;
    }

    /**
     * Gets the country associated with this question.
     *
     * @return the country being asked about
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Gets the list of possible answer choices.
     *
     * @return the list of options
     */
    public List<Country> getOptions() {
        return options;
    }

    /**
     * Gets the index of the selected answer.
     *
     * @return the index of the selected answer (-1 if not answered yet)
     */
    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    /**
     * Sets the selected answer by specifying the index in the options list.
     *
     * @param selectedAnswer the index of the selected answer
     */
    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    /**
     * Checks if the question has been answered.
     *
     * @return true if an answer has been selected, false otherwise
     */
    public boolean isAnswered() {
        return selectedAnswer != -1;
    }

    /**
     * Checks if the selected answer is correct.
     * The answer is correct if the selected country's continent matches the question's country continent.
     *
     * @return true if the selected answer is correct, false otherwise
     */
    public boolean isCorrect() {
        if (selectedAnswer == -1) return false;
        return options.get(selectedAnswer).getContinent().equals(country.getContinent());
    }
 }
