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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class RecipeDao {

    static final Logger log = LogManager.getLogger();
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

    public RecipeRecord updateRecipeData(RecipeRecord recipeRecord){



        try {
            mapper.save(recipeRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "recipeId",
                                new ExpectedAttributeValue().withValue(new AttributeValue(recipeRecord.getRecipeId())).withExists(true)
///                            new ExpectedAttributeValue().withExists(true)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("recipeId does not exist");
        }

        return recipeRecord;
    }

    public List<RecipeRecord> getRecipesByDietaryRestriction(DietaryRestrictionData dietaryRestrictionInfo){

        try {
            Map<String, AttributeValue> valueMap = new HashMap<>();
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            StringBuilder sb = new StringBuilder();
            List<String> filterExpressions = new ArrayList<>();

            if (dietaryRestrictionInfo.isGlutenFree()) {
                valueMap.put(":glutenFree", new AttributeValue().withN(String.valueOf(1)));
                filterExpressions.add("isGlutenFree = :glutenFree");
            }
            if (dietaryRestrictionInfo.isDairyFree()) {
                valueMap.put(":dairyFree", new AttributeValue().withN(String.valueOf(1)));
                filterExpressions.add("isDairyFree = :dairyFree");
            }
            if (dietaryRestrictionInfo.isEggFree()) {
                valueMap.put(":eggFree", new AttributeValue().withN(String.valueOf(1)));
                filterExpressions.add("isEggFree = :eggFree");
            }
            if (dietaryRestrictionInfo.isVegetarian()) {
                valueMap.put(":vegetarian", new AttributeValue().withN(String.valueOf(1)));
                filterExpressions.add("isVegetarian = :vegetarian");
            }
            if (dietaryRestrictionInfo.isVegan()) {
                valueMap.put(":vegan", new AttributeValue().withN(String.valueOf(1)));
                filterExpressions.add("isVegan = :vegan");
            }

            for (int i = 0; i < filterExpressions.size(); i++){
                if (i < filterExpressions.size() - 1){
                    sb.append(filterExpressions.get(i)).append(" AND ");
                } else {
                    sb.append(filterExpressions.get(i));
                }
            }

            scanExpression.withFilterExpression(sb.toString());
            scanExpression.withExpressionAttributeValues(valueMap);

            PaginatedScanList<RecipeRecord> recipeList = mapper.scan(RecipeRecord.class, scanExpression);

            return recipeList;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("something else bad happened");
        }
    }

    public void deleteRecipeData(String recipeId) {

        mapper.delete(recipeId);
    }

}
