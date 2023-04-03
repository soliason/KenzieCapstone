package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeSummaryResponse {

    @JsonProperty("recipeId")
    private String recipeId;

    @JsonProperty("title")
    private String title;

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
