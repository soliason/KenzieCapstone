package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.DietaryRestrictionData;
import com.kenzie.capstone.service.model.RecipeData;
import com.kenzie.capstone.service.model.RecipeRecord;
import com.kenzie.capstone.service.model.RecipeRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public RecipeRecord setRecipeData(RecipeRecord recipeRecord) {

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

    public List<RecipeRecord> getRecipesByDietaryRestriction(DietaryRestrictionData dietaryRestrictionInfo){
        try {
            Map<String, AttributeValue> valueMap = new HashMap<>();
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

            if (dietaryRestrictionInfo.isGlutenFree()){
                valueMap.put(":glutenFree", new AttributeValue().withBOOL(true));
                scanExpression
                        .withFilterExpression("isGlutenFree equals :glutenFree");
            }
            if (dietaryRestrictionInfo.isDairyFree()){
                valueMap.put(":dairyFree", new AttributeValue().withBOOL(true));
                scanExpression
                        .withFilterExpression("isDairyFree equals :dairyFree");
            }
            if (dietaryRestrictionInfo.isEggFree()){
                valueMap.put(":eggFree", new AttributeValue().withBOOL(true));
                scanExpression
                        .withFilterExpression("isEggFree equals :eggFree");
            }
            if (dietaryRestrictionInfo.isVegetarian()){
                valueMap.put(":vegetarian", new AttributeValue().withBOOL(true));
                scanExpression
                        .withFilterExpression("isVegetarian equals :vegetarian");
            }
            if (dietaryRestrictionInfo.isVegan()){
                valueMap.put(":vegan", new AttributeValue().withBOOL(true));
                scanExpression
                        .withFilterExpression("isVegan equals :vegan");
            }
            scanExpression.withExpressionAttributeValues(valueMap);
            PaginatedScanList<RecipeRecord> recipeList = mapper.scan(RecipeRecord.class, scanExpression);

            return recipeList;

        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("something bad happened");
        }
    }
}
