package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.RecipeData;


public class LambdaRecipeServiceClient {

    private static final String GET_RECIPE_ENDPOINT = "recipe/{dietaryRestriction}";

    private static final String SET_RECIPE_ENDPOINT = "recipe";

    private ObjectMapper mapper;

    public LambdaRecipeServiceClient() {this.mapper = new ObjectMapper();}

    public RecipeData getRecipeData(String dietaryRestriction) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_RECIPE_ENDPOINT.replace("{dietaryRestriction}", dietaryRestriction));
        RecipeData recipeData;
        try {
            recipeData = mapper.readValue(response, RecipeData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return recipeData;
    }

    public RecipeData setRecipeData(String data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(SET_RECIPE_ENDPOINT, data);
        RecipeData recipeData;
        try {
            recipeData = mapper.readValue(response, RecipeData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return recipeData;
    }
}
