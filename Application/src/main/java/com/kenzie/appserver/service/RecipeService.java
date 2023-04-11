package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.DietaryRestrictionInfoRequest;
import com.kenzie.appserver.controller.model.RecipeCreateRequest;
import com.kenzie.appserver.controller.model.RecipeUpdateRequest;
import com.kenzie.appserver.repositories.RecipeRepository;
import com.kenzie.appserver.service.model.Recipe;
import com.kenzie.capstone.service.client.LambdaRecipeServiceClient;
import com.kenzie.capstone.service.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private LambdaRecipeServiceClient lambdaRecipeServiceClient;

    private CacheStore cache;

    public RecipeService(RecipeRepository recipeRepository, LambdaRecipeServiceClient lambdaRecipeServiceClient, CacheStore cache) {
        this.recipeRepository = recipeRepository;
        this.lambdaRecipeServiceClient = lambdaRecipeServiceClient;
        this.cache = cache;
    }

    public Recipe findById(String recipeId) {

        if (recipeId == null || recipeId.length() == 0) {
            throw new IllegalArgumentException("Recipe Id is not valid");
        }

        //get it from the cache if it exists
        Recipe cachedRecipe = cache.get(recipeId);
        if (cachedRecipe != null){
            return cachedRecipe;
        }

        // getting data from the lambda
        RecipeData recipeFromLambda = lambdaRecipeServiceClient.getRecipeData(recipeId);

        return new Recipe(recipeFromLambda.getRecipeId(),
                recipeFromLambda.getTitle(), recipeFromLambda.getIngredients(),
                recipeFromLambda.getSteps(), recipeFromLambda.isGlutenFree(),
                recipeFromLambda.isDairyFree(), recipeFromLambda.isEggFree(),
                recipeFromLambda.isVegetarian(), recipeFromLambda.isVegan(), recipeFromLambda.getRatings());
    }

    public Recipe addNewRecipe(RecipeCreateRequest recipeCreateRequest) {

        if (recipeCreateRequest.getTitle() == null || recipeCreateRequest.getTitle().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Recipe");
        }

        //convert RecipeCreateRequest to Recipe Request
        List<Integer> ratings = new ArrayList<> ();
        RecipeRequest recipeRequest = new RecipeRequest(recipeCreateRequest.getTitle(),
                recipeCreateRequest.getIngredients(), recipeCreateRequest.getSteps(), recipeCreateRequest.isGlutenFree(),
                recipeCreateRequest.isDairyFree(), recipeCreateRequest.isEggFree(), recipeCreateRequest.isVegetarian(),
                recipeCreateRequest.isVegan(), ratings);

        // sending data to Lambda
        RecipeResponse recipeFromLambda = lambdaRecipeServiceClient.setRecipeData(recipeRequest);

        return new Recipe(recipeFromLambda.getRecipeId(),
                            recipeFromLambda.getTitle(), recipeFromLambda.getIngredients(),
                            recipeFromLambda.getSteps(), recipeFromLambda.isGlutenFree(),
                            recipeFromLambda.isDairyFree(), recipeFromLambda.isEggFree(),
                            recipeFromLambda.isVegetarian(), recipeFromLambda.isVegan(), recipeFromLambda.getRatings());
    }

    public List<Recipe> findByDietaryRestriction(DietaryRestrictionInfoRequest dietaryRestrictionInfoRequest){

        if (dietaryRestrictionInfoRequest == null) {
            throw new IllegalArgumentException("Invalid Dietary Restrictions");
        }

        //getting it from the lambda
        DietaryRestrictionData data = dietaryRestrictionInfoRequestToData(dietaryRestrictionInfoRequest);
        List<RecipeData> recipesFromLambda = lambdaRecipeServiceClient.getRecipesByDietaryRestriction(data);

        //convert and also cache
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeData recipeData : recipesFromLambda){
            Recipe recipe = recipeDataToRecipe(recipeData);
            recipes.add(recipe);
            cache.add(recipe.getRecipeId(), recipe);
        }

        //return recipes;
        return recipes;
    }

    public Recipe updateRecipe(RecipeUpdateRequest recipeUpdateRequest){

        if (recipeUpdateRequest.getRecipeId() == null || recipeUpdateRequest.getRecipeId().length() == 0) {
            throw new IllegalArgumentException("Invalid Recipe Id");
        }

        if (recipeUpdateRequest.getNewRating() == null ||
                recipeUpdateRequest.getNewRating() < 0 || recipeUpdateRequest.getNewRating() > 5) {
            throw new IllegalArgumentException("Invalid Rating");
        }

        Recipe recipe = cache.get(recipeUpdateRequest.getRecipeId());

        if (recipe == null){
            recipe = findById(recipeUpdateRequest.getRecipeId());
        }

        cache.evict(recipeUpdateRequest.getRecipeId());

        RecipeUpdateRequestLambda recipeUpdateRequestLambda = new RecipeUpdateRequestLambda();
        recipeUpdateRequestLambda.setRecipeId(recipeUpdateRequest.getRecipeId());
        recipeUpdateRequestLambda.setTitle(recipe.getTitle());
        recipeUpdateRequestLambda.setIngredients(recipe.getIngredients());
        recipeUpdateRequestLambda.setSteps(recipe.getSteps());
        recipeUpdateRequestLambda.setIsGlutenFree(recipe.isGlutenFree());
        recipeUpdateRequestLambda.setIsDairyFree(recipe.isDairyFree());
        recipeUpdateRequestLambda.setIsEggFree(recipe.isEggFree());
        recipeUpdateRequestLambda.setIsVegetarian(recipe.isVegetarian());
        recipeUpdateRequestLambda.setIsVegan(recipe.isVegan());
        recipeUpdateRequestLambda.setRatings(recipe.getRatings());
        recipeUpdateRequestLambda.addRating(recipeUpdateRequest.getNewRating());

        RecipeResponse recipeFromLambda = lambdaRecipeServiceClient.updateRecipeData(recipeUpdateRequestLambda);

        return new Recipe(recipeFromLambda.getRecipeId(),
                recipeFromLambda.getTitle(), recipeFromLambda.getIngredients(),
                recipeFromLambda.getSteps(), recipeFromLambda.isGlutenFree(),
                recipeFromLambda.isDairyFree(), recipeFromLambda.isEggFree(),
                recipeFromLambda.isVegetarian(), recipeFromLambda.isVegan(), recipeFromLambda.getRatings());
    }

    public void deleteRecipe(String recipeId) {

        if (recipeId == null || recipeId.length() == 0) {
            throw new IllegalArgumentException("Recipe Id is not valid");
        }

        lambdaRecipeServiceClient.deleteById(recipeId);

        cache.evict(recipeId);
    }

    //helper methods

    private Recipe recipeDataToRecipe(RecipeData data){

        return new Recipe(data.getRecipeId(),
                data.getTitle(), data.getIngredients(),
                data.getSteps(), data.isGlutenFree(),
                data.isDairyFree(), data.isEggFree(),
                data.isVegetarian(), data.isVegan(), data.getRatings());
    }

    private DietaryRestrictionData dietaryRestrictionInfoRequestToData(DietaryRestrictionInfoRequest request){

        DietaryRestrictionData data = new DietaryRestrictionData();
        data.setIsGlutenFree(request.isGlutenFree());
        data.setIsDairyFree(request.isDairyFree());
        data.setIsEggFree(request.isEggFree());
        data.setIsVegetarian(request.isVegetarian());
        data.setIsVegan(request.isVegan());

        return data;
    }

}


