package com.kenzie.appserver.service;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecipeServiceTest {
    private RecipeRepository recipeRepository;
    private RecipeService recipeService;
    private LambdaRecipeServiceClient lambdaRecipeServiceClient;

    @BeforeEach
    void setup(){
        recipeRepository = mock(RecipeRepository.class);
        lambdaRecipeServiceClient = mock(LambdaRecipeServiceClient.class);
        recipeService = new RecipeService(recipeRepository, lambdaRecipeServiceClient);
    }

    @Test
    void findById(){
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
    void findById_invalidId(){
        //GIVEN
        String id = UUID.randomUUID().toString();

        //WHEN

    }


}
