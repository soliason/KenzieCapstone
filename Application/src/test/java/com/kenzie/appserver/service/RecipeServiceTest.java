package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.DietaryRestrictionInfoRequest;
import com.kenzie.appserver.controller.model.RecipeCreateRequest;
import com.kenzie.appserver.controller.model.RecipeUpdateRequest;
import com.kenzie.appserver.repositories.RecipeRepository;
import com.kenzie.appserver.service.model.Recipe;
import com.kenzie.capstone.service.client.LambdaRecipeServiceClient;
import com.kenzie.capstone.service.model.RecipeData;

import com.kenzie.capstone.service.model.RecipeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

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
        data.setIsGlutenFree(true);
        data.setIsDairyFree(false);
        data.setIsEggFree(false);
        data.setIsVegetarian(false);
        data.setIsVegan(false);

        //WHEN
        when(lambdaRecipeServiceClient.getRecipeData(id)).thenReturn(data);

        Recipe recipe = recipeService.findById(id);

        //THEN
        Assertions.assertEquals(id, recipe.getRecipeId());
    }

    @Test
    void findById_idNull_throwNewIllegalArgumentException(){
        //GIVEN
        String id = null;

        //WHEN
        //THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> recipeService.findById(id));
    }

    @Test
    void findById_idEmpty_throwNewIllegalArgumentException(){
        //GIVEN
        String id = "";

        //WHEN
        //THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> recipeService.findById(id));
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
        request.setIsGlutenFree(true);
        request.setIsDairyFree(false);
        request.setIsEggFree(true);
        request.setIsVegetarian(false);
        request.setIsVegan(false);

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
        data.setIsGlutenFree(true);
        data.setIsDairyFree(false);
        data.setIsEggFree(true);
        data.setIsVegetarian(false);
        data.setIsVegan(false);
        data.setRatings(new ArrayList<>());
        List<RecipeData> recipeList = new ArrayList<>();
        recipeList.add(data);

        //WHEN
        when(lambdaRecipeServiceClient.getRecipesByDietaryRestriction(any())).thenReturn(recipeList);
        List<Recipe> returnedList = recipeService.findByDietaryRestriction(request);

        //THEN
        Assertions.assertEquals(returnedList.get(0).getRecipeId(), "testing123");
    }

    @Test
    void addNewRecipe(){
        //GIVEN
        RecipeCreateRequest request = new RecipeCreateRequest();
        request.setTitle("testRecipe");
        request.setIngredients(new ArrayList<>());
        request.setSteps(new ArrayList<>());
        request.setIsGlutenFree(true);
        request.setIsDairyFree(false);
        request.setIsEggFree(false);
        request.setIsVegetarian(true);
        request.setIsVegan(false);

        RecipeResponse response = new RecipeResponse();
        response.setTitle("testRecipe");
        response.setIngredients(new ArrayList<>());
        response.setSteps(new ArrayList<>());
        response.setIsGlutenFree(true);
        response.setIsDairyFree(false);
        response.setIsEggFree(false);
        response.setIsVegetarian(true);
        response.setIsVegan(false);
        response.setRecipeId("test");
        response.setRatings(new ArrayList<>());

        //WHEN
        when(lambdaRecipeServiceClient.setRecipeData(any())).thenReturn(response);
        Recipe returnedRecipe = recipeService.addNewRecipe(request);

        //THEN
        Assertions.assertEquals("testRecipe", returnedRecipe.getTitle());
    }

    @Test
    void addNewRecipe_nullTitle_throwsNewResponseStatusException() {

        //GIVEN
        RecipeCreateRequest request = new RecipeCreateRequest();
        request.setTitle(null);
        request.setIngredients(new ArrayList<>());
        request.setSteps(new ArrayList<>());
        request.setIsGlutenFree(true);
        request.setIsDairyFree(false);
        request.setIsEggFree(false);
        request.setIsVegetarian(true);
        request.setIsVegan(false);

        //WHEN
        //THEN
        Assertions.assertThrows(ResponseStatusException.class, () -> recipeService.addNewRecipe(request));

    }

    @Test
    void addNewRecipe_emptyTitle_throwsNewResponseStatusException() {

        //GIVEN
        RecipeCreateRequest request = new RecipeCreateRequest();
        request.setTitle("");
        request.setIngredients(new ArrayList<>());
        request.setSteps(new ArrayList<>());
        request.setIsGlutenFree(true);
        request.setIsDairyFree(false);
        request.setIsEggFree(false);
        request.setIsVegetarian(true);
        request.setIsVegan(false);

        //WHEN
        //THEN
        Assertions.assertThrows(ResponseStatusException.class, () -> recipeService.addNewRecipe(request));

    }

    @Test
    void updateRecipe(){
        //GIVEN
        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();

        recipeUpdateRequest.setRecipeId("testing123");
        recipeUpdateRequest.setNewRating(5);

        Recipe cachedRecipe = new Recipe("testing123",
                "test",
                new ArrayList<>(),
                new ArrayList<>(),
                true,
                false,
                false,
                false,
                false,
                new ArrayList<>());

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setTitle("test");
        recipeResponse.setIngredients(new ArrayList<>());
        recipeResponse.setSteps(new ArrayList<>());
        recipeResponse.setIsGlutenFree(true);
        recipeResponse.setIsDairyFree(false);
        recipeResponse.setIsEggFree(false);
        recipeResponse.setIsVegetarian(false);
        recipeResponse.setIsVegan(false);
        recipeResponse.setRecipeId("testing123");
        List<Integer> ratings = new ArrayList<>();
        ratings.add(recipeUpdateRequest.getNewRating());
        recipeResponse.setRatings(ratings);

        //WHEN
        when(cache.get(recipeUpdateRequest.getRecipeId())).thenReturn(cachedRecipe);
        when(lambdaRecipeServiceClient.updateRecipeData(any())).thenReturn(recipeResponse);

        Recipe returnedRecipe = recipeService.updateRecipe(recipeUpdateRequest);

        //THEN
        Assertions.assertEquals(recipeUpdateRequest.getRecipeId(), returnedRecipe.getRecipeId());
        Assertions.assertTrue(returnedRecipe.getRatings().contains(5));
    }

    @Test
    void DeleteById_RecipeDeleted() {
        //GIVEN
        String id = UUID.randomUUID().toString();

        //WHEN
        recipeService.deleteRecipe(id);

        //THEN
        verify(lambdaRecipeServiceClient, times(1)).deleteById(id);
        verify(cache, times(1)).evict(id);
    }

    @Test
    void DeleteById_IdNull_ThrowsNewIllegalArgumentException() {
        //GIVEN
        String id = null;

        //WHEN
        //THEN
        Assertions.assertThrows(IllegalArgumentException.class, ()-> recipeService.deleteRecipe(id));
    }

    @Test
    void UpdateRecipe_NullRecipeId_ThrowsNewIllegalArgumentException() {
        //GIVEN
        String id = null;

        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setRecipeId(id);
        recipeUpdateRequest.setNewRating(3);

        //WHEN
        //THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(recipeUpdateRequest));
    }

    @Test
    void UpdateRecipe_InvalidRating_ThrowsNewIllegalArgumentException() {
        //GIVEN
        String id = UUID.randomUUID().toString();

        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setRecipeId(id);
        recipeUpdateRequest.setNewRating(9);

        //WHEN
        //THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(recipeUpdateRequest));
    }

    @Test
    void UpdateRecipe_EmptyRecipeId_ThrowsNewIllegalArgumentException() {
        //GIVEN
        String id = "";

        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setRecipeId(id);
        recipeUpdateRequest.setNewRating(3);

        //WHEN
        //THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(recipeUpdateRequest));
    }

    @Test
    void UpdateRecipe_NullRating_ThrowsNewIllegalArgumentException() {
        //GIVEN
        String id = UUID.randomUUID().toString();

        RecipeUpdateRequest recipeUpdateRequest = new RecipeUpdateRequest();
        recipeUpdateRequest.setRecipeId(id);
        recipeUpdateRequest.setNewRating(null);

        //WHEN
        //THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(recipeUpdateRequest));
    }
}
