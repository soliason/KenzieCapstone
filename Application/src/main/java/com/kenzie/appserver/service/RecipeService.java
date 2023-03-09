package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.RecipeRepository;
import com.kenzie.appserver.service.model.Recipe;
import com.kenzie.capstone.service.client.LambdaRecipeServiceClient;
import com.kenzie.capstone.service.model.RecipeData;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private RecipeRepository recipeRepository;

    private LambdaRecipeServiceClient lambdaRecipeServiceClient;

    public RecipeService(RecipeRepository recipeRepository, LambdaRecipeServiceClient lambdaRecipeServiceClient) {
        this.recipeRepository = recipeRepository;
        this.lambdaRecipeServiceClient = lambdaRecipeServiceClient;
    }

    public Recipe findById(String dietaryRestriction) {

        // getting data from the lambda
        RecipeData recipeFromLambda = lambdaRecipeServiceClient.getRecipeData(dietaryRestriction);

        // getting data from the local repository
        Recipe dataFromDynamo = recipeRepository
                .findById(dietaryRestriction)
                .map(recipe -> new Recipe(recipe.getRecipeId(), recipe.getTitle(), recipe.getIngredients(),
                        recipe.getSteps(), recipe.getIsGlutenFree(), recipe.getIsDairyFree(), recipe.getIsEggFree(),
                        recipe.getIsVegetarian(), recipe.getIsVegan()))
                .orElse(null);

        return dataFromDynamo;
        //return null;
        //return recipeFromLambda;
    }

}


