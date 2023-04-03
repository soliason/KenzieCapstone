package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeResponse {

    @JsonProperty("recipeId")
    private String recipeId;

    @JsonProperty("title")
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

    @JsonProperty("averageRating")
    private Double averageRating;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

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

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

    public double getAverageRating(){
        return ratings.stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0.0);
    }

    public void setAverageRating() {
        this.averageRating = Math.ceil(getAverageRating());
    }
}
