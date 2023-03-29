package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.ExampleService;
import com.kenzie.appserver.service.RecipeService;
import com.kenzie.appserver.service.model.Example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.service.model.Recipe;
import net.andreinc.mockneat.MockNeat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


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

    private final MockNeat mockNeat = MockNeat.threadLocal();

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
    public void createRecipe_CreateSuccessful() throws Exception {

        String title = mockNeat.strings().valStr();
        List<String> ingredients = new ArrayList<>();
        ingredients.add("beast");
        List<String> steps = new ArrayList<>();
        steps.add("roast it");

        RecipeCreateRequest recipeCreateRequest = new RecipeCreateRequest();
        recipeCreateRequest.setTitle(title);
        recipeCreateRequest.setIngredients(ingredients);
        recipeCreateRequest.setSteps(steps);
        recipeCreateRequest.setGlutenFree(true);
        recipeCreateRequest.setDairyFree(true);
        recipeCreateRequest.setEggFree(false);
        recipeCreateRequest.setVegetarian(false);
        recipeCreateRequest.setVegan(false);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/recipe")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeCreateRequest)))
                .andExpect(jsonPath("recipeId")
                        .exists())
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(status().is2xxSuccessful());


    }

    @Test
    public void updateRecipe() throws Exception {
        RecipeCreateRequest request = new RecipeCreateRequest();
        request.setTitle("testRecipe");
        request.setIngredients(new ArrayList<>());
        request.setSteps(new ArrayList<>());
        request.setGlutenFree(true);
        request.setDairyFree(false);
        request.setEggFree(false);
        request.setVegetarian(true);
        request.setVegan(false);

        Recipe createdRecipe = recipeService.addNewRecipe(request);

        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();

        recipeUpdateRequest.setRecipeId(createdRecipe.getRecipeId());
        recipeUpdateRequest.setNewRating(5);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(put("/recipe/rating")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(recipeUpdateRequest)))
                .andExpect(jsonPath("recipeId")
                        .exists())
                .andExpect(jsonPath("ratings")
                        .value(contains(5)))
                .andExpect(status().is2xxSuccessful());
    }
}