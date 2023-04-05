package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.LambdaRecipeService;

import com.kenzie.capstone.service.converter.JsonStringToRecipeUpdateConverter;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.exceptions.InvalidDataException;

import com.kenzie.capstone.service.model.RecipeResponse;
import com.kenzie.capstone.service.model.RecipeUpdateRequestLambda;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateRecipeData implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

        JsonStringToRecipeUpdateConverter jsonStringToRecipeConverter = new JsonStringToRecipeUpdateConverter();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        LambdaRecipeService lambdaRecipeService = serviceComponent.provideLambdaRecipeService();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            RecipeUpdateRequestLambda recipeRequest = jsonStringToRecipeConverter.convert(input.getBody());
            log.info("new log statement:" + recipeRequest.getRecipeId());
            RecipeResponse recipeResponse = lambdaRecipeService.updateRecipeData(recipeRequest);

            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(recipeResponse));

        } catch (InvalidDataException e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.errorPayload()));
        }
    }
}
