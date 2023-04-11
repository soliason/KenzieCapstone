package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class RecipeUpdateRequest {

    @NotEmpty
    @JsonProperty("recipeId")
    private String recipeId;

    @NotEmpty
    @JsonProperty("newRating")
    private Integer newRating;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getNewRating() {
        return newRating;
    }

    public void setNewRating(Integer newRating) {
        this.newRating = newRating;
    }
}
