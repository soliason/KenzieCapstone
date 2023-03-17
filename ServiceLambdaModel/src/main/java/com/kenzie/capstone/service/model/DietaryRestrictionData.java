package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude (JsonInclude.Include.NON_NULL)
public class DietaryRestrictionData {

    @JsonProperty("isGlutenFree")
    private boolean isGlutenFree;


    @JsonProperty("isDairyFree")
    private boolean isDairyFree;


    @JsonProperty("isEggFree")
    private boolean isEggFree;


    @JsonProperty("isVegetarian")
    private boolean isVegetarian;


    @JsonProperty("isVegan")
    private boolean isVegan;

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        isGlutenFree = glutenFree;
    }

    public boolean isDairyFree() {
        return isDairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        isDairyFree = dairyFree;
    }

    public boolean isEggFree() {
        return isEggFree;
    }

    public void setEggFree(boolean eggFree) {
        isEggFree = eggFree;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }
}
