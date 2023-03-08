package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.RecipeDao;
import com.kenzie.capstone.service.model.RecipeData;
import com.kenzie.capstone.service.model.RecipeRecord;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class LambdaRecipeService {

    private RecipeDao recipeDao;

//    @Inject
//    public LambdaRecipeService(RecipeDao recipeDao) {
//        this.recipeDao = recipeDao;
//    }
//
//    public RecipeData getRecipeData(String dietaryRestriction) {
//        List<RecipeRecord> records = recipeDao.getRecipeData(dietaryRestriction);
//        if (records.size() > 0) {
//            return new RecipeData(records.get(0).getRecipeId(), records.get(0).getData());
//        }
//        return null;
//    }
//
//    public RecipeData setRecipeData(String data) {
//        //String id = UUID.randomUUID().toString();
//        String dietaryRestriction = ;
//        RecipeRecord record = recipeDao.setRecipeData(dietaryRestriction, data);
//        return new RecipeData(dietaryRestriction, data);
//    }

}
