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

    public void setIsGlutenFree(boolean glutenFree) {
        isGlutenFree = glutenFree;
    }

    public boolean isDairyFree() {
        return isDairyFree;
    }

    public void setIsDairyFree(boolean dairyFree) {
        isDairyFree = dairyFree;
    }

    public boolean isEggFree() {
        return isEggFree;
    }

    public void setIsEggFree(boolean eggFree) {
        isEggFree = eggFree;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setIsVegan(boolean vegan) {
        isVegan = vegan;
    }
}
