package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.RecipeService;

import com.kenzie.appserver.service.model.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private RecipeService recipeService;

    RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable("recipeId") String recipeId) {

        try {
            UUID.fromString(recipeId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Recipe Id");
        }

        Recipe recipe = recipeService.findById(recipeId);

        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeId(recipe.getRecipeId());
        recipeResponse.setTitle(recipe.getTitle());
        recipeResponse.setIngredients(recipe.getIngredients());
        recipeResponse.setSteps(recipe.getSteps());
        recipeResponse.setIsGlutenFree(recipe.isGlutenFree());
        recipeResponse.setIsDairyFree(recipe.isDairyFree());
        recipeResponse.setIsEggFree(recipe.isEggFree());
        recipeResponse.setIsVegetarian(recipe.isVegetarian());
        recipeResponse.setIsVegan(recipe.isVegan());
        recipeResponse.setRatings(recipe.getRatings());

        return ResponseEntity.ok(recipeResponse);

    }

    @PostMapping
    public ResponseEntity<RecipeResponse> addNewRecipe(@RequestBody RecipeCreateRequest recipeCreateRequest) {

        if (recipeCreateRequest.getTitle() == null || recipeCreateRequest.getTitle().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Recipe");
        }

        Recipe recipe = recipeService.addNewRecipe(recipeCreateRequest);

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeId(recipe.getRecipeId());
        recipeResponse.setTitle(recipe.getTitle());
        recipeResponse.setIngredients(recipe.getIngredients());
        recipeResponse.setSteps(recipe.getSteps());
        recipeResponse.setIsGlutenFree(recipe.isGlutenFree());
        recipeResponse.setIsDairyFree(recipe.isDairyFree());
        recipeResponse.setIsEggFree(recipe.isEggFree());
        recipeResponse.setIsVegetarian(recipe.isVegetarian());
        recipeResponse.setIsVegan(recipe.isVegan());
        recipeResponse.setRatings(recipe.getRatings());

        return ResponseEntity.ok(recipeResponse);
    }

    @GetMapping("/dietaryRestriction/{isGlutenFree}/{isDairyFree}/{isEggFree}/{isVegetarian}/{isVegan}")
    public ResponseEntity<List<RecipeSummaryResponse>> getRecipesByDietaryRestriction(
            @PathVariable ("isGlutenFree") Boolean isGlutenFree,
            @PathVariable("isDairyFree") Boolean isDairyFree,
            @PathVariable ("isEggFree") Boolean isEggFree,
            @PathVariable ("isVegetarian") Boolean isVegetarian,
            @PathVariable ("isVegan") Boolean isVegan){

        DietaryRestrictionInfoRequest dietaryRestrictionInfoRequest = new DietaryRestrictionInfoRequest ();
        dietaryRestrictionInfoRequest.setIsGlutenFree(isGlutenFree);
        dietaryRestrictionInfoRequest.setIsDairyFree(isDairyFree);
        dietaryRestrictionInfoRequest.setIsEggFree(isEggFree);
        dietaryRestrictionInfoRequest.setIsVegetarian(isVegetarian);
        dietaryRestrictionInfoRequest.setIsVegan(isVegan);

        List<Recipe> recipes = recipeService.findByDietaryRestriction(dietaryRestrictionInfoRequest);

        if (recipes == null){
            return ResponseEntity.notFound().build();
        }

        List<RecipeSummaryResponse> responseList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            responseList.add(createRecipeSummaryResponse(recipe));
        }

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/rating")
    public ResponseEntity<RecipeResponse> updateRecipe(@RequestBody RecipeUpdateRequest recipeUpdateRequest){

        if (recipeUpdateRequest.getRecipeId() == null || recipeUpdateRequest.getRecipeId().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Recipe Id");
        }

        if (recipeUpdateRequest.getNewRating() == null ||
                recipeUpdateRequest.getNewRating() < 0 || recipeUpdateRequest.getNewRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Recipe Rating");
        }

        Recipe updatedRecipe = recipeService.updateRecipe(recipeUpdateRequest);

        RecipeResponse recipeResponse = createRecipeResponse(updatedRecipe);

        return ResponseEntity.ok(recipeResponse);
    }

    //helper methods

    private RecipeResponse createRecipeResponse(Recipe recipe) {

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeId(recipe.getRecipeId());
        recipeResponse.setTitle(recipe.getTitle());
        recipeResponse.setIngredients(recipe.getIngredients());
        recipeResponse.setSteps(recipe.getSteps());
        recipeResponse.setIsGlutenFree(recipe.isGlutenFree());
        recipeResponse.setIsDairyFree(recipe.isDairyFree());
        recipeResponse.setIsEggFree(recipe.isEggFree());
        recipeResponse.setIsVegetarian(recipe.isVegetarian());
        recipeResponse.setIsVegan(recipe.isVegan());
        recipeResponse.setRatings(recipe.getRatings());
        recipeResponse.setAverageRating();

        return recipeResponse;
    }

    private RecipeSummaryResponse createRecipeSummaryResponse(Recipe recipe) {

        RecipeSummaryResponse recipeSummaryResponse = new RecipeSummaryResponse();
        recipeSummaryResponse.setRecipeId(recipe.getRecipeId());
        recipeSummaryResponse.setTitle(recipe.getTitle());
        recipeSummaryResponse.setRatings(recipe.getRatings());
        recipeSummaryResponse.setAverageRating();

        return recipeSummaryResponse;
    }

}
