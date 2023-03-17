package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class DietaryRestrictionInfoRequest {
    @NotEmpty
    @JsonProperty("isGlutenFree")
    private boolean isGlutenFree;

    @NotEmpty
    @JsonProperty("isDairyFree")
    private boolean isDairyFree;

    @NotEmpty
    @JsonProperty("isEggFree")
    private boolean isEggFree;

    @NotEmpty
    @JsonProperty("isVegetarian")
    private boolean isVegetarian;

    @NotEmpty
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
