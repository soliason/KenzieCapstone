package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.RecipeResponse;
import com.kenzie.appserver.service.RecipeService;

import com.kenzie.appserver.service.model.Recipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private RecipeService recipeService;

    RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{dietaryRestriction}")
    public ResponseEntity<RecipeResponse> get(@PathVariable("dietaryRestriction") String dietaryRestriction) {

        Recipe recipe = recipeService.findById(dietaryRestriction);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }

        RecipeResponse recipeResponse = recipeToResponse(recipe);

        return ResponseEntity.ok(recipeResponse);

    }

    private RecipeResponse recipeToResponse(Recipe recipe) {

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
