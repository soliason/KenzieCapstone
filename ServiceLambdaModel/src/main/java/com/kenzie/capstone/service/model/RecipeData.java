package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
@JsonInclude (JsonInclude.Include.NON_NULL)
public class RecipeData {

    @JsonProperty ("recipeId")
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

    public RecipeData(String recipeId,
                      String title,
                      List<String> ingredients,
                      List<String> steps,
                      boolean isGlutenFree,
                      boolean isDairyFree,
                      boolean isEggFree,
                      boolean isVegetarian,
                      boolean isVegan,
                      List<Integer> ratings){
        this.recipeId = recipeId;
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

    public RecipeData(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeData)) return false;
        RecipeData that = (RecipeData) o;
        return isGlutenFree() == that.isGlutenFree() && isDairyFree() == that.isDairyFree() && isEggFree() == that.isEggFree() && isVegetarian() == that.isVegetarian() && isVegan() == that.isVegan() && getRecipeId().equals(that.getRecipeId()) && getTitle().equals(that.getTitle()) && getIngredients().equals(that.getIngredients()) && getSteps().equals(that.getSteps()) && getRatings().equals(that.getRatings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipeId(), getTitle(), getIngredients(), getSteps(), isGlutenFree(), isDairyFree(), isEggFree(), isVegetarian(), isVegan(), getRatings());
    }
}
