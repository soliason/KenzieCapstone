package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.DietaryRestrictionData;
import com.kenzie.capstone.service.model.RecipeData;
import com.kenzie.capstone.service.model.RecipeRequest;

public class JsonStringToDietaryRestrictionInfoConverter {
    public DietaryRestrictionData convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            DietaryRestrictionData dietaryRestrictionData = gson.fromJson(body, DietaryRestrictionData.class);
            return dietaryRestrictionData;
        } catch (Exception e) {
            throw new InvalidDataException("Dietary Restriction Info could not be deserialized");
        }
    }
}
