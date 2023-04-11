package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.DietaryRestrictionData;
import com.kenzie.capstone.service.model.RecipeData;
import com.kenzie.capstone.service.model.RecipeRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class RecipeDao {

    static final Logger log = LogManager.getLogger();
    private DynamoDBMapper mapper;

    public RecipeDao(DynamoDBMapper mapper){
        this.mapper = mapper;
    }

    public RecipeRecord getRecipeData(String recipeId) {

        RecipeRecord record = mapper.load(RecipeRecord.class, recipeId);

        if(record == null) {
            throw new InvalidDataException ("There is no recipe that matches that id");
        }

        return record;

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

            if (!dietaryRestrictionInfo.isGlutenFree() &&
                    !dietaryRestrictionInfo.isDairyFree() &&
                    !dietaryRestrictionInfo.isEggFree() &&
                    !dietaryRestrictionInfo.isVegetarian() &&
                    !dietaryRestrictionInfo.isVegan()){
                try {
                    return mapper.scan(RecipeRecord.class, scanExpression);
                } catch (Exception e) {
                    throw new IllegalArgumentException("can't get all the recipes");
                }
            }

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

            return mapper.scan(RecipeRecord.class, scanExpression);
        } catch (Exception e) {
            throw new IllegalArgumentException("something else bad happened");
        }
    }

    public void deleteRecipeData(String recipeId) {

        log.info("RecipeID: " + recipeId);
        RecipeRecord recipeRecord = new RecipeRecord();
        recipeRecord.setRecipeId(recipeId);
        DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression()
                .withExpected(ImmutableMap.of("recipeId", new ExpectedAttributeValue()
                        .withValue(new AttributeValue(recipeId))
                        .withExists(true)));
        mapper.delete(recipeRecord, deleteExpression);
    }
}
