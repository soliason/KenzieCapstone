package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.RecipeDao;
import com.kenzie.capstone.service.model.RecipeData;
import com.kenzie.capstone.service.model.RecipeRecord;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class LambdaRecipeService {

    private RecipeDao recipeDao;

    @Inject
    public LambdaRecipeService(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    public RecipeData getRecipeData(String recipeId) {
        List<RecipeRecord> records = recipeDao.getRecipeData(recipeId);
        if (records.size() > 0) {
            return new RecipeData(records.get(0).getRecipeId(), records.get(0).getTitle(), records.get(0).getIngredients(),
                    records.get(0).getSteps(), records.get(0).getIsGlutenFree(), records.get(0).getIsDairyFree(),
                    records.get(0).getIsEggFree(), records.get(0).getIsVegetarian(), records.get(0).getIsVegan());
        }
        return null;
    }

    public RecipeData setRecipeData(String title, List<String> ingredients, List<String> steps,
                                    boolean isGlutenFree, boolean isDairyFree, boolean isEggFree,
                                    boolean isVegetarian, boolean isVegan, List<Integer> ratings) {
        String recipeId = UUID.randomUUID().toString();
        RecipeRecord record = recipeDao.setRecipeData(recipeId, title, ingredients,
                                    steps, isGlutenFree, isDairyFree, isEggFree,
                                    isVegetarian, isVegan, ratings);
        return new RecipeData(recipeId, title, ingredients, steps, isGlutenFree, isDairyFree,
                                    isEggFree, isVegetarian, isVegan);
    }

}
