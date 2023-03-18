package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.DietaryRestrictionInfoRequest;
import com.kenzie.appserver.controller.model.RecipeCreateRequest;
import com.kenzie.appserver.controller.model.RecipeResponse;
import com.kenzie.appserver.service.RecipeService;

import com.kenzie.appserver.service.model.Recipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private RecipeService recipeService;

    RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable("recipeId") String recipeId) {

        Recipe recipe = recipeService.findById(recipeId);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeId(recipe.getRecipeId());
        recipeResponse.setTitle(recipe.getTitle());
        recipeResponse.setIngredients(recipe.getIngredients());
        recipeResponse.setSteps(recipe.getSteps());
        recipeResponse.setGlutenFree(recipe.isGlutenFree());
        recipeResponse.setDairyFree(recipe.isDairyFree());
        recipeResponse.setEggFree(recipe.isEggFree());
        recipeResponse.setVegetarian(recipe.isVegetarian());
        recipeResponse.setVegan(recipe.isVegan());
        recipeResponse.setRatings(recipe.getRatings());

        return ResponseEntity.ok(recipeResponse);

    }

    @PostMapping
    public ResponseEntity<RecipeResponse> addNewRecipe(@RequestBody RecipeCreateRequest recipeCreateRequest) {

        Recipe recipe = recipeService.addNewRecipe(recipeCreateRequest);

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeId(recipe.getRecipeId());
        recipeResponse.setTitle(recipe.getTitle());
        recipeResponse.setIngredients(recipe.getIngredients());
        recipeResponse.setSteps(recipe.getSteps());
        recipeResponse.setGlutenFree(recipe.isGlutenFree());
        recipeResponse.setDairyFree(recipe.isDairyFree());
        recipeResponse.setEggFree(recipe.isEggFree());
        recipeResponse.setVegetarian(recipe.isVegetarian());
        recipeResponse.setVegan(recipe.isVegan());
        recipeResponse.setRatings(recipe.getRatings());

        return ResponseEntity.ok(recipeResponse);
    }

    @GetMapping("/dietaryRestriction/{isGlutenFree}/{isDairyFree}/{isEggFree}/{isVegetarian}/{isVegan}")
    public ResponseEntity<List<RecipeResponse>> getRecipesByDietaryRestriction(
            @PathVariable ("isGlutenFree") Boolean isGlutenFree,
            @PathVariable("isDairyFree") Boolean isDairyFree,
            @PathVariable ("isEggFree") Boolean isEggFree,
            @PathVariable ("isVegetarian") Boolean isVegetarian,
            @PathVariable ("isVegan") Boolean isVegan){

        DietaryRestrictionInfoRequest dietaryRestrictionInfoRequest = new DietaryRestrictionInfoRequest ();
        dietaryRestrictionInfoRequest.setGlutenFree(isGlutenFree);
        dietaryRestrictionInfoRequest.setDairyFree(isDairyFree);
        dietaryRestrictionInfoRequest.setEggFree(isEggFree);
        dietaryRestrictionInfoRequest.setVegetarian(isVegetarian);
        dietaryRestrictionInfoRequest.setVegan(isVegan);

        List<Recipe> recipes = recipeService.findByDietaryRestriction(dietaryRestrictionInfoRequest);

        if (recipes == null){
            return ResponseEntity.notFound().build();
        }

        List<RecipeResponse> responseList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            responseList.add(createRecipeResponse(recipe));
        }

        return ResponseEntity.ok(responseList);

    }

    //helper methods

    private RecipeResponse createRecipeResponse(Recipe recipe) {

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeId(recipe.getRecipeId());
        recipeResponse.setTitle(recipe.getTitle());
        recipeResponse.setIngredients(recipe.getIngredients());
        recipeResponse.setSteps(recipe.getSteps());
        recipeResponse.setGlutenFree(recipe.isGlutenFree());
        recipeResponse.setDairyFree(recipe.isDairyFree());
        recipeResponse.setEggFree(recipe.isEggFree());
        recipeResponse.setVegetarian(recipe.isVegetarian());
        recipeResponse.setVegan(recipe.isVegan());
        recipeResponse.setRatings(recipe.getRatings());

        return recipeResponse;
    }

}
