package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.DietaryRestrictionInfoRequest;
import com.kenzie.appserver.repositories.RecipeRepository;
import com.kenzie.appserver.service.model.Recipe;
import com.kenzie.capstone.service.client.LambdaRecipeServiceClient;
import com.kenzie.capstone.service.model.RecipeData;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RecipeServiceTest {
    private RecipeRepository recipeRepository;
    private RecipeService recipeService;
    private LambdaRecipeServiceClient lambdaRecipeServiceClient;
    private CacheStore cache;

    @BeforeEach
    void setup(){
        recipeRepository = mock(RecipeRepository.class);
        lambdaRecipeServiceClient = mock(LambdaRecipeServiceClient.class);
        cache = mock(CacheStore.class);
        recipeService = new RecipeService(recipeRepository, lambdaRecipeServiceClient, cache);
    }

    @Test
    void findById_getFromLambda(){
        //GIVEN
        String id = UUID.randomUUID().toString();

        RecipeData data = new RecipeData();
        data.setRecipeId(id);
        data.setTitle("test");
        List<String> steps = new ArrayList<>();
        List<String> ingredients = new ArrayList<>();
        steps.add("mix it");
        ingredients.add("stuff");
        data.setSteps(steps);
        data.setIngredients(ingredients);
        data.setGlutenFree(true);
        data.setDairyFree(false);
        data.setEggFree(false);
        data.setVegetarian(false);
        data.setVegan(false);

        //WHEN
        when(lambdaRecipeServiceClient.getRecipeData(id)).thenReturn(data);

        Recipe recipe = recipeService.findById(id);

        //THEN
        Assertions.assertEquals(id, recipe.getRecipeId());
    }

    @Test
    void findById_getFromCache(){
        //GIVEN
        String id = "testing123";
        String title = "test recipe";
        String ingredient = "ingredients";
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        String step = "steps";
        List<String> steps = new ArrayList<>();
        steps.add(step);
        List<Integer> ratings = new ArrayList<>();

        Recipe recipe = new Recipe(id,
                title,
                ingredients,
                steps,
                true,
                false,
                true,
                false,
                false,
                ratings);

        //WHEN
        when(cache.get(id)).thenReturn(recipe);
        Recipe returnedRecipe = recipeService.findById(id);

        //THEN
        Assertions.assertEquals(id, returnedRecipe.getRecipeId());
    }

    @Test
    void findByDietaryRestriction(){
        //GIVEN
        DietaryRestrictionInfoRequest request = new DietaryRestrictionInfoRequest();
        request.setGlutenFree(true);
        request.setDairyFree(false);
        request.setEggFree(true);
        request.setVegetarian(false);
        request.setVegan(false);

        RecipeData data = new RecipeData();
        data.setRecipeId("testing123");
        data.setTitle("test recipe");
        String ingredient = "ingredients";
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        String step = "steps";
        data.setIngredients(ingredients);
        List<String> steps = new ArrayList<>();
        steps.add(step);
        data.setSteps(steps);
        data.setGlutenFree(true);
        data.setDairyFree(false);
        data.setEggFree(true);
        data.setVegetarian(false);
        data.setVegan(false);
        data.setRatings(new ArrayList<>());
        List<RecipeData> recipeList = new ArrayList<>();
        recipeList.add(data);

        //WHEN
        when(lambdaRecipeServiceClient.getRecipesByDietaryRestriction(any())).thenReturn(recipeList);
        List<Recipe> returnedList = recipeService.findByDietaryRestriction(request);

        //THEN
        Assertions.assertEquals(returnedList.get(0).getRecipeId(), "testing123");
    }


}
