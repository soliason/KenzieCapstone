package com.kenzie.appserver.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {

    private final String recipeId;
    private final String title;
    private final List<String> ingredients;
    private final List<String> steps;

    private final boolean isGlutenFree;
    private final boolean isDairyFree;
    private final boolean isEggFree;
    private final boolean isVegetarian;
    private final boolean isVegan;
    private final List<Integer> ratings;

    public Recipe(String recipeId,
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

    public double getAverageRating(){
        return ratings.stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0.0);
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public boolean isDairyFree() {
        return isDairyFree;
    }

    public boolean isEggFree() {
        return isEggFree;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public boolean isVegan() {
        return isVegan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        return isGlutenFree() == recipe.isGlutenFree() && isDairyFree() == recipe.isDairyFree() && isEggFree() == recipe.isEggFree() && isVegetarian() == recipe.isVegetarian() && isVegan() == recipe.isVegan() && getRecipeId().equals(recipe.getRecipeId()) && getTitle().equals(recipe.getTitle()) && getIngredients().equals(recipe.getIngredients()) && getSteps().equals(recipe.getSteps());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipeId(), getTitle(), getIngredients(), getSteps(), isGlutenFree(), isDairyFree(), isEggFree(), isVegetarian(), isVegan());
    }
}
