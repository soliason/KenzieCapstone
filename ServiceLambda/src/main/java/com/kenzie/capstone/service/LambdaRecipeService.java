package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.RecipeDao;
import com.kenzie.capstone.service.model.RecipeData;
import com.kenzie.capstone.service.model.RecipeRecord;
import com.kenzie.capstone.service.model.RecipeRequest;
import com.kenzie.capstone.service.model.RecipeResponse;

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
                    records.get(0).getIsEggFree(), records.get(0).getIsVegetarian(), records.get(0).getIsVegan(), records.get(0).getRatings());
        }
        return null;
    }

    public RecipeResponse setRecipeData(RecipeRequest recipeRequest) {
        String recipeId = UUID.randomUUID().toString();
        RecipeRecord recipeRecord =  new RecipeRecord();
        recipeRecord.setRecipeId(recipeId);
        recipeRecord.setTitle(recipeRequest.getTitle());
        recipeRecord.setIngredients(recipeRequest.getIngredients());
        recipeRecord.setSteps(recipeRequest.getSteps());
        recipeRecord.setIsDairyFree(recipeRequest.isDairyFree());
        recipeRecord.setIsGlutenFree(recipeRequest.isGlutenFree());
        recipeRecord.setIsEggFree(recipeRequest.isEggFree());
        recipeRecord.setIsVegetarian(recipeRequest.isVegetarian());
        recipeRecord.setIsVegan(recipeRequest.isVegan());
        recipeRecord.setRatings(recipeRequest.getRatings());

        recipeDao.setRecipeData(recipeRecord);
        RecipeResponse recipeResponse = new RecipeResponse(recipeId, recipeRequest.getTitle(), recipeRequest.getIngredients(),
                recipeRequest.getSteps(), recipeRequest.isGlutenFree(), recipeRequest.isDairyFree(), recipeRequest.isEggFree(),
                recipeRequest.isVegetarian (), recipeRequest.isVegan(), recipeRequest.getRatings());

        return recipeResponse;
//        return new RecipeData(recipeId, title, ingredients, steps, isGlutenFree, isDairyFree,
//                                    isEggFree, isVegetarian, isVegan);
    }

}
