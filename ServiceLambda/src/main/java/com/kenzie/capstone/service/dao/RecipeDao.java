package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.RecipeData;
import com.kenzie.capstone.service.model.RecipeRecord;

import java.util.List;


public class RecipeDao {

    private DynamoDBMapper mapper;

    public RecipeDao(DynamoDBMapper mapper){
        this.mapper = mapper;
    }

    public RecipeData storeRecipeData(RecipeData recipeData){
        try {
            mapper.save(recipeData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "recipeId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e){
            throw new IllegalArgumentException("recipeId has already been used");
        }
        return recipeData;
    }

    public List<RecipeRecord> getRecipeData(String recipeId) {
        RecipeRecord recipeRecord = new RecipeRecord();
        recipeRecord.setRecipeId(recipeId);

        DynamoDBQueryExpression<RecipeRecord> queryExpression = new DynamoDBQueryExpression<RecipeRecord>()
                .withHashKeyValues(recipeRecord)
                .withConsistentRead(false);

        return mapper.query(RecipeRecord.class, queryExpression);
    }

    public RecipeRecord setRecipeData(String recipeId,
                                      String title,
                                      List<String> ingredients,
                                      List<String> steps,
                                      boolean isGlutenFree,
                                      boolean isDairyFree,
                                      boolean isEggFree,
                                      boolean isVegetarian,
                                      boolean isVegan,
                                      List<Integer> ratings) {
        RecipeRecord recipeRecord = new RecipeRecord();
        recipeRecord.setRecipeId(recipeId);
        recipeRecord.setTitle(title);
        recipeRecord.setIngredients(ingredients);
        recipeRecord.setSteps(steps);
        recipeRecord.setIsGlutenFree(isGlutenFree);
        recipeRecord.setIsDairyFree(isDairyFree);
        recipeRecord.setIsEggFree(isEggFree);
        recipeRecord.setIsVegetarian(isVegetarian);
        recipeRecord.setIsVegan(isVegan);
        recipeRecord.setRatings(ratings);

        try {
            mapper.save(recipeRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "recipeId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("recipeId already exists");
        }

        return recipeRecord;
    }
}