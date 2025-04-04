package edu.uga.cs.countryquiz.models;

import java.io.Serializable;

/**
 * Represents a country with a name and the continent it belongs to.
 * This class implements Serializable for object serialization.
 */
public class Country implements Serializable {

    private String name;
    private String continent;

    /**
     * Constructs a new Country object with the specified name and continent.
     *
     * @param name the name of the country
     * @param continent the continent of the country
     */
    public Country(String name, String continent) {
        this.name = name;
        this.continent = continent;
    }

    /**
     * Returns the name of the country.
     *
     * @return the country's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the continent of the country.
     *
     * @return the continent
     */
    public String getContinent() {
        return continent;
    }
}
