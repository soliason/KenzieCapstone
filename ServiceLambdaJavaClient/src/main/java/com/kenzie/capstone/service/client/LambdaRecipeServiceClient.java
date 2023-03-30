package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class LambdaRecipeServiceClient {

    private static final String GET_RECIPE_ENDPOINT = "recipe/{recipeId}";

    private static final String SET_RECIPE_ENDPOINT = "recipe";

    private static final String UPDATE_RATING_ENDPOINT = "recipe/rating";

    private static final String GET_DIETARY_RESTRICTION_ENDPOINT =
            "recipe/dietaryRestriction/{isGlutenFree}/{isDairyFree}/{isEggFree}/{isVegetarian}/{isVegan}";

    //private static final String ADD_REFERRAL_ENDPOINT = "recipe";

    private ObjectMapper mapper;

    static final Logger log = LogManager.getLogger();

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

    public List<RecipeData> getRecipesByDietaryRestriction(DietaryRestrictionData dietaryRestrictionData) {

        String gluten = String.valueOf(dietaryRestrictionData.isGlutenFree());
        String dairy = String.valueOf(dietaryRestrictionData.isDairyFree());
        String egg = String.valueOf(dietaryRestrictionData.isEggFree());
        String vegetarian = String.valueOf(dietaryRestrictionData.isVegetarian());
        String vegan = String.valueOf(dietaryRestrictionData.isVegan());


        EndpointUtility endpointUtility = new EndpointUtility();
        //String response = endpointUtility.getEndpoint(GET_DIETARY_RESTRICTION_ENDPOINT);
        String response = endpointUtility.getEndpoint(
                GET_DIETARY_RESTRICTION_ENDPOINT.replace("{isGlutenFree}", gluten)
                        .replace("{isDairyFree}", dairy)
                        .replace("{isEggFree}", egg)
                        .replace("{isVegetarian}", vegetarian)
                        .replace("{isVegan}", vegan));

        List<RecipeData> recipeDataList;

        try {
            recipeDataList = mapper.readValue(response, new TypeReference<>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }

        return recipeDataList;
    }

    public RecipeResponse updateRecipeData(RecipeUpdateRequestLambda recipeUpdateRequestLambda){
        EndpointUtility endpointUtility = new EndpointUtility();
        log.info("LambdaRecipeServiceClient updateRecipeData method line 94 title:" + recipeUpdateRequestLambda.getTitle());
        log.info("LambdaRecipeServiceClient updateRecipeData method line 95 id:" + recipeUpdateRequestLambda.getRecipeId());

        String request;
        try {
           request = mapper.writeValueAsString(recipeUpdateRequestLambda);
///////            request = mapper.writeValueAsString(UPDATE_RATING_ENDPOINT.replace("{recipeId}", recipeUpdateRequestLambda.getRecipeId()));
            log.info("LambdaRecipeServiceClient updateRecipeData method line 100 request:" + request.toString());
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }

        String response = endpointUtility.putEndpoint(UPDATE_RATING_ENDPOINT, request);
//////        String response = endpointUtility.putEndpoint(request, recipeUpdateRequestLambda.setRatings();
        log.info("LambdaRecipeServiceClient updateRecipeData method line 106 response:" + response.toString());
        RecipeResponse recipeResponse;
        try {
            recipeResponse = mapper.readValue(response, RecipeResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException ("Unable to map deserialize JSON: " + e);
        }
        return recipeResponse;
    }
}
