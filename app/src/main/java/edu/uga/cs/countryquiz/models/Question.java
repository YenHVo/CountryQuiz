package edu.uga.cs.countryquiz.models;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private final Country country;
    private final List<Country> options;
    private int selectedAnswer; // default for unselected

    public Question(Country country, List<Country> options) {
        this.country = country;
        this.options = options;
        this.selectedAnswer = -1;
    }

    public Country getCountry() {
        return country;
    }

    public List<Country> getOptions() {
        return options;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public boolean isAnswered() {
        return selectedAnswer != -1;
    }

    public boolean isCorrect() {
        return options.get(selectedAnswer).equals(country.getContinent());
    }

 }
