package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.RecipeService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.service.model.Recipe;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class RecipeControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    RecipeService recipeService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getRecipesByDietaryRestriction() throws Exception {

        ResultActions actions = mvc.perform(
                get("/recipe/dietaryRestriction/{isGlutenFree}/{isDairyFree}/{isEggFree}/{isVegetarian}/{isVegan}",
                        true, false, false, false, false)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        List<RecipeSummaryResponse> responses = mapper.readValue(responseBody, new TypeReference<>() {
        });

        if (!responses.isEmpty()){
            boolean areTheyAllGlutenFree = true;
            for (RecipeSummaryResponse recipeSummaryResponse : responses){
                Recipe recipe = recipeService.findById(recipeSummaryResponse.getRecipeId());
                if (!recipe.isGlutenFree()){
                    areTheyAllGlutenFree = false;
                    break;
                }
            }
            Assertions.assertTrue(areTheyAllGlutenFree);
        } else {
            throw new Exception("there were no recipes in the list to test");
        }
    }

    //no bad case for get by dietary restriction because we use check boxes and there could always be recipes for any
    //combination of true/false for the five dietary restriction options

    @Test
    public void getById_Exists() throws Exception {

        mvc.perform(get("/recipe/{recipeId}", "3072cefb-ba0b-43c2-bd32-59a5b9e84b29")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("recipeId")
                        .isString())
                .andExpect(jsonPath("title")
                        .value(is("Lemon Raspberry Gelatin Gummies")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getById_doesNotExist() throws Exception {
        mvc.perform(get("/recipe/{recipeId}", "this is not an id")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createRecipe_CreateSuccessful() throws Exception {

        String title = "this one should be deleted";
        List<String> ingredients = new ArrayList<>();
        ingredients.add("beast");
        List<String> steps = new ArrayList<>();
        steps.add("roast it");

        RecipeCreateRequest recipeCreateRequest = new RecipeCreateRequest();
        recipeCreateRequest.setTitle(title);
        recipeCreateRequest.setIngredients(ingredients);
        recipeCreateRequest.setSteps(steps);
        recipeCreateRequest.setIsGlutenFree(true);
        recipeCreateRequest.setIsDairyFree(true);
        recipeCreateRequest.setIsEggFree(false);
        recipeCreateRequest.setIsVegetarian(false);
        recipeCreateRequest.setIsVegan(false);

        mapper.registerModule(new JavaTimeModule());

        ResultActions actions = mvc.perform(post("/recipe")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeCreateRequest)))
                .andExpect(jsonPath("recipeId")
                        .exists())
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(status().is2xxSuccessful());

        //cleanup
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        RecipeResponse response = mapper.readValue(responseBody, new TypeReference<>() {
        });
        recipeService.deleteRecipe(response.getRecipeId());
    }

    @Test
    public void createRecipe_noTitle() throws Exception {
        String title = "";
        List<String> ingredients = new ArrayList<>();
        ingredients.add("beast");
        List<String> steps = new ArrayList<>();
        steps.add("roast it");

        RecipeCreateRequest recipeCreateRequest = new RecipeCreateRequest();
        recipeCreateRequest.setTitle(title);
        recipeCreateRequest.setIngredients(ingredients);
        recipeCreateRequest.setSteps(steps);
        recipeCreateRequest.setIsGlutenFree(true);
        recipeCreateRequest.setIsDairyFree(true);
        recipeCreateRequest.setIsEggFree(false);
        recipeCreateRequest.setIsVegetarian(false);
        recipeCreateRequest.setIsVegan(false);

        mapper.registerModule(new JavaTimeModule());

        ResultActions actions = mvc.perform(post("/recipe")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeCreateRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateRecipe() throws Exception {
        RecipeCreateRequest request = new RecipeCreateRequest();
        request.setTitle("testRecipe");
        request.setIngredients(new ArrayList<>());
        request.setSteps(new ArrayList<>());
        request.setIsGlutenFree(false);
        request.setIsDairyFree(false);
        request.setIsEggFree(false);
        request.setIsVegetarian(false);
        request.setIsVegan(false);

        Recipe createdRecipe = recipeService.addNewRecipe(request);

        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();

        recipeUpdateRequest.setRecipeId(createdRecipe.getRecipeId());
        recipeUpdateRequest.setNewRating(5);

        mapper.registerModule(new JavaTimeModule());

        ResultActions actions = mvc.perform(put("/recipe/rating")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeUpdateRequest)))
                .andExpect(jsonPath("recipeId")
                        .exists())
                .andExpect(jsonPath("ratings")
                        .value(contains(5)))
                .andExpect(status().is2xxSuccessful());

        //cleanup
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        RecipeResponse response = mapper.readValue(responseBody, new TypeReference<>() {
        });
        recipeService.deleteRecipe(response.getRecipeId());
    }

    @Test
    public void updateRecipe_nullNewRating() throws Exception {
        RecipeCreateRequest request = new RecipeCreateRequest();
        request.setTitle("testRecipe");
        request.setIngredients(new ArrayList<>());
        request.setSteps(new ArrayList<>());
        request.setIsGlutenFree(false);
        request.setIsDairyFree(false);
        request.setIsEggFree(false);
        request.setIsVegetarian(false);
        request.setIsVegan(false);

        Recipe createdRecipe = recipeService.addNewRecipe(request);

        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();

        recipeUpdateRequest.setRecipeId(createdRecipe.getRecipeId());
        recipeUpdateRequest.setNewRating(null);

        mapper.registerModule(new JavaTimeModule());

        ResultActions actions = mvc.perform(put("/recipe/rating")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeUpdateRequest)))
                .andExpect(status().is4xxClientError());

        //cleanup
        recipeService.deleteRecipe(createdRecipe.getRecipeId());
    }
}