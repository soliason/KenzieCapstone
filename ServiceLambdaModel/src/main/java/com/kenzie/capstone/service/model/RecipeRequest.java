package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecipeRequest {



    @JsonProperty ("title")
    private String title;


    @JsonProperty("ingredients")
    private List<String> ingredients;


    @JsonProperty("steps")
    private List<String> steps;


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

    @JsonProperty("ratings")
    private List<Integer> ratings;

    public RecipeRequest (String title, List<String> ingredients, List<String> steps, boolean isGlutenFree, boolean isDairyFree, boolean isEggFree, boolean isVegetarian, boolean isVegan, List<Integer> ratings) {

        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.isGlutenFree = isGlutenFree;
        this.isDairyFree = isDairyFree;
        this.isEggFree = isEggFree;
        this.isVegetarian = isVegetarian;
        this.isVegan = isVegan;
        this.ratings = ratings;
    }



    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public List<String> getIngredients () {
        return ingredients;
    }

    public void setIngredients (List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps () {
        return steps;
    }

    public void setSteps (List<String> steps) {
        this.steps = steps;
    }

    public boolean isGlutenFree () {
        return isGlutenFree;
    }

    public void setIsGlutenFree(boolean glutenFree) {
        isGlutenFree = glutenFree;
    }

    public boolean isDairyFree () {
        return isDairyFree;
    }

    public void setIsDairyFree(boolean dairyFree) {
        isDairyFree = dairyFree;
    }

    public boolean isEggFree () {
        return isEggFree;
    }

    public void setIsEggFree(boolean isEggFree) {
        this.isEggFree = isEggFree;
    }

    public boolean isVegetarian () {
        return isVegetarian;
    }

    public void setIsVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public boolean isVegan () {
        return isVegan;
    }

    public void setIsVegan(boolean isVegan) {
        this.isVegan = isVegan;
    }

    public List<Integer> getRatings () {
        return ratings;
    }

    public void setRatings (List<Integer> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString () {
        return "RecipeRequest{" +
                ", title='" + title + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", isGlutenFree=" + isGlutenFree +
                ", isDairyFree=" + isDairyFree +
                ", isEggFree=" + isEggFree +
                ", isVegetarian=" + isVegetarian +
                ", isVegan=" + isVegan +
                ", ratings=" + ratings +
                '}';
    }
}
