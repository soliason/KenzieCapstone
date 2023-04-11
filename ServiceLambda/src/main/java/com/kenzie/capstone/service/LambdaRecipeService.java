package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.RecipeDao;
import com.kenzie.capstone.service.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        RecipeRecord record = recipeDao.getRecipeData(recipeId);

        if (record != null) {
            return new RecipeData(record.getRecipeId(), record.getTitle(), record.getIngredients(),
                    record.getSteps(), record.getIsGlutenFree(), record.getIsDairyFree(),
                    record.getIsEggFree(), record.getIsVegetarian(), record.getIsVegan(), record.getRatings());
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

        return new RecipeResponse(recipeId, recipeRequest.getTitle(), recipeRequest.getIngredients(),
                recipeRequest.getSteps(), recipeRequest.isGlutenFree(), recipeRequest.isDairyFree(), recipeRequest.isEggFree(),
                recipeRequest.isVegetarian (), recipeRequest.isVegan(), recipeRequest.getRatings());
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

    public RecipeResponse updateRecipeData(RecipeUpdateRequestLambda recipeRequest){
        RecipeRecord recipeRecord = new RecipeRecord();
        recipeRecord.setRecipeId(recipeRequest.getRecipeId());
        recipeRecord.setTitle(recipeRequest.getTitle());
        recipeRecord.setIngredients(recipeRequest.getIngredients());
        recipeRecord.setSteps(recipeRequest.getSteps());
        recipeRecord.setIsDairyFree(recipeRequest.isDairyFree());
        recipeRecord.setIsGlutenFree(recipeRequest.isGlutenFree());
        recipeRecord.setIsEggFree(recipeRequest.isEggFree());
        recipeRecord.setIsVegetarian(recipeRequest.isVegetarian());
        recipeRecord.setIsVegan(recipeRequest.isVegan());
        recipeRecord.setRatings(recipeRequest.getRatings());

        recipeDao.updateRecipeData(recipeRecord);

        return new RecipeResponse(recipeRequest.getRecipeId(), recipeRequest.getTitle(), recipeRequest.getIngredients(),
                recipeRequest.getSteps(), recipeRequest.isGlutenFree(), recipeRequest.isDairyFree(), recipeRequest.isEggFree(),
                recipeRequest.isVegetarian (), recipeRequest.isVegan(), recipeRequest.getRatings());
    }

    public void deleteRecipeData(String recipeId) {
        recipeDao.deleteRecipeData(recipeId);
    }
    
    //helper functions

    private RecipeData recipeRecordToRecipeData(RecipeRecord record){
        RecipeData data = new RecipeData();
        data.setRecipeId(record.getRecipeId());
        data.setTitle(record.getTitle());
        data.setIngredients(record.getIngredients());
        data.setSteps(record.getSteps());
        data.setIsGlutenFree(record.getIsGlutenFree());
        data.setIsDairyFree(record.getIsDairyFree());
        data.setIsEggFree(record.getIsEggFree());
        data.setIsVegetarian(record.getIsVegetarian());
        data.setIsVegan(record.getIsVegan());
        data.setRatings(record.getRatings());
        return data;
    }

}
