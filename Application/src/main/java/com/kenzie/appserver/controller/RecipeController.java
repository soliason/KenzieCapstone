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

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeId(recipe.getRecipeId());
        recipeResponse.setTitle(recipe.getTitle());
        return ResponseEntity.ok(recipeResponse);
    }
}
