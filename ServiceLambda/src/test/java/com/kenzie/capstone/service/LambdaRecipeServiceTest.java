package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.dao.RecipeDao;
import com.kenzie.capstone.service.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaRecipeServiceTest {

    private RecipeDao recipeDao;
    private LambdaRecipeService lambdaRecipeService;

    @BeforeAll
    void setup() {
        this.recipeDao = mock(RecipeDao.class);
        this.lambdaRecipeService = new LambdaRecipeService(recipeDao);
    }

    @Test
    void setRecipeDataTest() {
        //GIVEN
        RecipeRequest recipeRequest = new RecipeRequest("testRecipe",
                new ArrayList<>(),
                new ArrayList<>(),
                true,
                false,
                false,
                false,
                false,
                new ArrayList<>());

        //WHEN
        RecipeResponse response = lambdaRecipeService.setRecipeData(recipeRequest);

        //THEN
        verify(recipeDao, times(1)).setRecipeData(any());
    }

    @Test
    void getRecipesByDietaryRestrictionTest() {
        //GIVEN
        DietaryRestrictionData data = new DietaryRestrictionData();
        data.setIsGlutenFree(true);
        data.setIsDairyFree(false);
        data.setIsEggFree(false);
        data.setIsVegetarian(false);
        data.setIsVegan(false);

        //WHEN
        List<RecipeData> returnedList = lambdaRecipeService.getRecipesByDietaryRestriction(data);

        //THEN
        verify(recipeDao, times(1)).getRecipesByDietaryRestriction(data);
    }

    @Test
    void getRecipeDataTest(){
        //GIVEN
        //WHEN
        RecipeData data = lambdaRecipeService.getRecipeData("test123");

        //THEN
        verify(recipeDao, times(1)).getRecipeData("test123");
    }

    @Test
    void updateRecipeDataTest(){
        //GIVEN
        RecipeUpdateRequestLambda recipeUpdateRequestLambda = new RecipeUpdateRequestLambda();
        recipeUpdateRequestLambda.setRecipeId("testing123");
        recipeUpdateRequestLambda.setTitle("testRecipe");
        recipeUpdateRequestLambda.setIngredients(new ArrayList<>());
        recipeUpdateRequestLambda.setSteps(new ArrayList<>());
        recipeUpdateRequestLambda.setIsGlutenFree(true);
        recipeUpdateRequestLambda.setIsDairyFree(false);
        recipeUpdateRequestLambda.setIsEggFree(false);
        recipeUpdateRequestLambda.setIsVegetarian(false);
        recipeUpdateRequestLambda.setIsVegan(false);
        recipeUpdateRequestLambda.setRatings(new ArrayList<>());

        //WHEN
        RecipeResponse recipeResponse = lambdaRecipeService.updateRecipeData(recipeUpdateRequestLambda);

        //THEN
        verify(recipeDao, times(1)).updateRecipeData(any());
    }

    @Test
    void deleteRecipeDataTest(){
        //GIVEN
        //WHEN
        lambdaRecipeService.deleteRecipeData("test");

        //THEN
        verify(recipeDao, times(1)).deleteRecipeData("test");
    }

}
