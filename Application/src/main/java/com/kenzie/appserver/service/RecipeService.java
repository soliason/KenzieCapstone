package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.RecipeResponse;
import com.kenzie.appserver.repositories.RecipeRepository;
import com.kenzie.appserver.repositories.model.RecipeRecord;
import com.kenzie.appserver.service.model.Recipe;
import com.kenzie.capstone.service.client.LambdaRecipeServiceClient;
import com.kenzie.capstone.service.model.RecipeData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private LambdaRecipeServiceClient lambdaRecipeServiceClient;

    public RecipeService(RecipeRepository recipeRepository, LambdaRecipeServiceClient lambdaRecipeServiceClient) {
        this.recipeRepository = recipeRepository;
        this.lambdaRecipeServiceClient = lambdaRecipeServiceClient;
    }

    public Recipe findById(String recipeId) {

        // getting data from the lambda
        RecipeData recipeFromLambda = lambdaRecipeServiceClient.getRecipeData(recipeId);

        // getting data from the local repository
//        Recipe dataFromDynamo = recipeRepository
//                .findById(recipeId)
//                .map(recipe -> new Recipe(recipe.getRecipeId(), recipe.getTitle(), recipe.getIngredients(),
//                        recipe.getSteps(), recipe.getIsGlutenFree(), recipe.getIsDairyFree(), recipe.getIsEggFree(),
//                        recipe.getIsVegetarian(), recipe.getIsVegan()))
//                .orElse(null);

        //return dataFromDynamo;
        //return null;

        Recipe recipe = new Recipe(recipeFromLambda.getRecipeId(),
                recipeFromLambda.getTitle(), recipeFromLambda.getIngredients(),
                recipeFromLambda.getSteps(), recipeFromLambda.isGlutenFree(),
                recipeFromLambda.isDairyFree(), recipeFromLambda.isEggFree(),
                recipeFromLambda.isVegetarian(), recipeFromLambda.isVegan());
        return recipe;
    }

    public Recipe addNewRecipe(String title, List<String> ingredients, List<String> steps,
                               boolean isGlutenFree, boolean isDairyFree, boolean isEggFree,
                               boolean isVegetarian, boolean isVegan) {

        // sending data to Lambda
        RecipeData recipeFromLambda = lambdaRecipeServiceClient.setRecipeData(title);

        // sending data to the local repository
        RecipeRecord recipeRecord = new RecipeRecord();
        recipeRecord.setRecipeId(recipeFromLambda.getRecipeId());
        recipeRecord.setTitle(recipeFromLambda.getTitle());
        recipeRecord.setIngredients(recipeFromLambda.getIngredients());
        recipeRecord.setSteps(recipeFromLambda.getSteps());
        recipeRecord.setIsGlutenFree(recipeFromLambda.isGlutenFree());
        recipeRecord.setIsDairyFree(recipeFromLambda.isDairyFree());
        recipeRecord.setIsEggFree(recipeFromLambda.isEggFree());
        recipeRecord.setIsVegetarian(recipeFromLambda.isVegetarian());
        recipeRecord.setIsVegan(recipeFromLambda.isVegan());
        recipeRepository.save(recipeRecord);

        Recipe recipe = new Recipe(recipeFromLambda.getRecipeId(),
                            title, recipeFromLambda.getIngredients(),
                            recipeFromLambda.getSteps(), recipeFromLambda.isGlutenFree(),
                            recipeFromLambda.isDairyFree(), recipeFromLambda.isEggFree(),
                            recipeFromLambda.isVegetarian(), recipeFromLambda.isVegan());
        return recipe;
    }

}


