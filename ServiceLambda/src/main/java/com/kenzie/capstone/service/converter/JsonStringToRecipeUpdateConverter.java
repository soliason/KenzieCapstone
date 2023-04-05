package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.RecipeRequest;
import com.kenzie.capstone.service.model.RecipeUpdateRequestLambda;

public class JsonStringToRecipeUpdateConverter {
    public RecipeUpdateRequestLambda convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(body, RecipeUpdateRequestLambda.class);
        } catch (Exception e) {
            throw new InvalidDataException("Recipe Update Request could not be deserialized");
        }
    }
}
