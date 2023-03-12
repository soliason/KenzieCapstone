package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.RecipeCreateRequest;
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

        Recipe recipe = recipeService.addNewRecipe(recipeCreateRequest.getTitle(), recipeCreateRequest.getIngredients(),
                recipeCreateRequest.getSteps(), recipeCreateRequest.isGlutenFree(), recipeCreateRequest.isDairyFree(),
                recipeCreateRequest.isEggFree(), recipeCreateRequest.isVegetarian(), recipeCreateRequest.isVegan());

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

}
