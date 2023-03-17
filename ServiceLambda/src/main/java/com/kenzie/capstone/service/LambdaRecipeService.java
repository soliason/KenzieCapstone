package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.RecipeDao;
import com.kenzie.capstone.service.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
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

    public List<RecipeData> getRecipesByDietaryRestriction(DietaryRestrictionData data){
        List<RecipeRecord> records = recipeDao.getRecipesByDietaryRestriction(data);
        if (records.size() > 0) {
            List<RecipeData> recipeDataList = new ArrayList<>();
            for (RecipeRecord recipeRecord : records){
                recipeDataList.add(recipeRecordToRecipeData(recipeRecord));
            }
            return recipeDataList;
        }
        return null;
    }

    //helper functions

    private RecipeData recipeRecordToRecipeData(RecipeRecord record){
        RecipeData data = new RecipeData();
        data.setRecipeId(record.getRecipeId());
        data.setTitle(record.getTitle());
        data.setIngredients(record.getIngredients());
        data.setSteps(record.getSteps());
        data.setGlutenFree(record.getIsGlutenFree());
        data.setDairyFree(record.getIsDairyFree());
        data.setEggFree(record.getIsEggFree());
        data.setVegetarian(record.getIsVegetarian());
        data.setVegan(record.getIsVegan());
        return data;
    }

}
