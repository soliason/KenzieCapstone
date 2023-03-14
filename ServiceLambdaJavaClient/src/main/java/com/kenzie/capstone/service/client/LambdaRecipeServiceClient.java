package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.RecipeData;
import com.kenzie.capstone.service.model.RecipeRequest;
import com.kenzie.capstone.service.model.RecipeResponse;

import java.util.List;


public class LambdaRecipeServiceClient {

    private static final String GET_RECIPE_ENDPOINT = "recipe/{recipeId}";

    private static final String SET_RECIPE_ENDPOINT = "recipe";

    //private static final String ADD_REFERRAL_ENDPOINT = "recipe";

    private ObjectMapper mapper;

    public LambdaRecipeServiceClient() {this.mapper = new ObjectMapper();}

    public RecipeData getRecipeData(String recipeId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_RECIPE_ENDPOINT.replace("{recipeId}", recipeId));
        RecipeData recipeData;
        try {
            recipeData = mapper.readValue(response, RecipeData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return recipeData;
    }

    public RecipeResponse setRecipeData(RecipeRequest recipeRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
//        String response = endpointUtility.postEndpoint(SET_RECIPE_ENDPOINT, recipeRequest.toString());
//        RecipeData recipeData;
//        try {
//            recipeData = mapper.readValue(response, RecipeData.class);
//        } catch (Exception e) {
//            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
//        }
//        return recipeData;
        String request;
        try {
            request = mapper.writeValueAsString(recipeRequest);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(SET_RECIPE_ENDPOINT, request);
        RecipeResponse recipeResponse;
        try {
            recipeResponse = mapper.readValue(response, RecipeResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException ("Unable to map deserialize JSON: " + e);
        }
        return recipeResponse;
    }
}
