package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "LambdaRecipe")
public class RecipeRecord {

    private String recipeId;
    private String title;
    private List<String> ingredients;
    private List<String> steps;
    private boolean isGlutenFree;
    private boolean isDairyFree;
    private boolean isEggFree;
    private boolean isVegetarian;
    private boolean isVegan;
    private List<Integer> ratings;

    @DynamoDBHashKey(attributeName = "recipeId")
    public String getRecipeId(){
        return this.recipeId;
    }

    public void setRecipeId(String recipeId){
        this.recipeId = recipeId;
    }

    @DynamoDBAttribute(attributeName = "title")
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "ingredients")
    public List<String> getIngredients(){
        return this.ingredients;
    }

    public void setIngredients(List<String> ingredients){
        this.ingredients = ingredients;
    }

    @DynamoDBAttribute(attributeName = "steps")
    public List<String> getSteps(){
        return this.steps;
    }

    public void setSteps(List<String> steps){
        this.steps = steps;
    }

    @DynamoDBAttribute(attributeName = "isGlutenFree")
    public boolean getIsGlutenFree(){
        return this.isGlutenFree;
    }

    public void setIsGlutenFree(boolean isGlutenFree){
        this.isGlutenFree = isGlutenFree;
    }

    @DynamoDBAttribute(attributeName = "isDairyFree")
    public boolean getIsDairyFree(){
        return this.isDairyFree;
    }

    public void setIsDairyFree(boolean isDairyFree){
        this.isDairyFree = isDairyFree;
    }

    @DynamoDBAttribute(attributeName = "isEggFree")
    public boolean getIsEggFree(){ return this.isEggFree;}

    public void setIsEggFree(boolean isEggFree){
        this.isEggFree = isEggFree;
    }

    @DynamoDBAttribute(attributeName = "isVegetarian")
    public boolean getIsVegetarian(){ return this.isVegetarian;}

    public void setIsVegetarian(boolean isVegetarian){
        this.isVegetarian = isVegetarian;
    }

    @DynamoDBAttribute(attributeName = "isVegan")
    public boolean getIsVegan(){
        return this.isVegan;
    }

    public void setIsVegan(boolean isVegan){
        this.isVegan = isVegan;
    }

    @DynamoDBAttribute(attributeName = "ratings")
    public List<Integer> getRatings(){
        return this.ratings;
    }

    public void setRatings(List<Integer> ratings){
        this.ratings = ratings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeRecord)) return false;
        RecipeRecord that = (RecipeRecord) o;
        return isGlutenFree == that.isGlutenFree && isDairyFree == that.isDairyFree &&
                isEggFree == that.isEggFree && isVegetarian == that.isVegetarian &&
                isVegan == that.isVegan && getRecipeId().equals(that.getRecipeId()) &&
                getTitle().equals(that.getTitle()) && getIngredients().equals(that.getIngredients()) &&
                getSteps().equals(that.getSteps()) && getRatings().equals(that.getRatings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipeId(), getTitle(), getIngredients(), getSteps(), isGlutenFree, isDairyFree, isEggFree, isVegetarian, isVegan, getRatings());
    }
}
