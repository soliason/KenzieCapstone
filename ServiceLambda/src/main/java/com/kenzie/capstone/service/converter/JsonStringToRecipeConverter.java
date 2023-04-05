package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.RecipeRequest;

public class JsonStringToRecipeConverter {

    public RecipeRequest convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(body, RecipeRequest.class);
        } catch (Exception e) {
            throw new InvalidDataException ("Recipe Request could not be deserialized");
        }
    }
}
