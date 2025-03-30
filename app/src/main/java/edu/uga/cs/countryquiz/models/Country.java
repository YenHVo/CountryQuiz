package edu.uga.cs.countryquiz.models;

public class Country {

    private String name;
    private String continent;

    public Country() {
        this.name = "";
        this.continent = "";
    }

    public Country(String name, String continent) {
        this.name = name;
        this.continent = continent;
    }

    public String getName() {
        return name;
    }

    public String getContinent() {
        return continent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }
}
