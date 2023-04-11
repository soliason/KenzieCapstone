package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.LambdaRecipeService;

import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.model.DietaryRestrictionData;
import com.kenzie.capstone.service.model.RecipeData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRecipesByDietaryRestriction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        LambdaRecipeService lambdaRecipeService = serviceComponent.provideLambdaRecipeService();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        Map<String, String> data = input.getPathParameters();

        if (data == null || data.size() == 0) {
            return response
                    .withStatusCode(400)
                    .withBody("data is invalid");
        }

        try {
            DietaryRestrictionData dietaryRestrictionData = mapToDietaryRestrictionData(data);
            List<RecipeData> recipeData = lambdaRecipeService.getRecipesByDietaryRestriction(dietaryRestrictionData);
            String output = gson.toJson(recipeData);

            return response
                    .withStatusCode(200)
                    .withBody(output);

        } catch (Exception e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.getMessage()));
        }
    }

    private DietaryRestrictionData mapToDietaryRestrictionData(Map<String, String> data) {

        DietaryRestrictionData dietaryRestrictionData = new DietaryRestrictionData();
        dietaryRestrictionData.setIsGlutenFree(Boolean.parseBoolean(data.get("isGlutenFree")));
        dietaryRestrictionData.setIsDairyFree(Boolean.parseBoolean(data.get("isDairyFree")));
        dietaryRestrictionData.setIsEggFree(Boolean.parseBoolean(data.get("isEggFree")));
        dietaryRestrictionData.setIsVegetarian(Boolean.parseBoolean(data.get("isVegetarian")));
        dietaryRestrictionData.setIsVegan(Boolean.parseBoolean(data.get("isVegan")));

        return dietaryRestrictionData;
    }
}
