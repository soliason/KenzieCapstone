package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.DietaryRestrictionInfoRequest;
import com.kenzie.appserver.controller.model.ExampleCreateRequest;
import com.kenzie.appserver.controller.model.RecipeCreateRequest;
import com.kenzie.appserver.controller.model.RecipeResponse;
import com.kenzie.appserver.service.ExampleService;
import com.kenzie.appserver.service.RecipeService;
import com.kenzie.appserver.service.model.Example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.service.model.Recipe;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class RecipeControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    //ExampleService exampleService;
    RecipeService recipeService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getRecipesByDietaryRestriction() throws Exception {
        DietaryRestrictionInfoRequest request = new DietaryRestrictionInfoRequest();
        request.setGlutenFree(true);
        request.setDairyFree(false);
        request.setEggFree(false);
        request.setVegetarian(false);
        request.setVegan(false);

        ResultActions actions = mvc.perform(get("/recipe/dietaryRestriction", request)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        List<RecipeResponse> responses = mapper.readValue(responseBody, new TypeReference<List<RecipeResponse>>() {});
        if (!responses.isEmpty()){
            boolean areTheyAllGlutenFree = true;
            for (RecipeResponse recipeResponse : responses){
                if (recipeResponse.isGlutenFree() == false){
                    areTheyAllGlutenFree = false;
                    break;
                }
            }
            Assertions.assertTrue(areTheyAllGlutenFree);
        } else {
            throw new Exception("there were no recipes in the list to test");
        }
    }

//    @Test
//    public void getById_Exists() throws Exception {
//
//        String title = mockNeat.strings().valStr();
//        //String recipeId = UUID.randomUUID().toString();
//        List<String> ingredients = new ArrayList<>();
//        ingredients.add("beast");
//        List<String> steps = new ArrayList<>();
//        ingredients.add("roast it");
//
//        RecipeCreateRequest recipeCreateRequest = new RecipeCreateRequest();
//        recipeCreateRequest.s
//        Recipe persistedRecipe = recipeService.addNewRecipe("roast beast", ingredients, steps, true,
//                true, true, false, false);
//        mvc.perform(get("/recipe/{recipeId}", persistedRecipe.getRecipeId())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("recipeId")
//                        .isString())
//                .andExpect(jsonPath("title")
//                        .value(is(title)))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    public void createRecipe_CreateSuccessful() throws Exception {
//        //String name = mockNeat.strings().valStr();
//        String title = mockNeat.strings().valStr();
//        List<String> ingredients = new ArrayList<>();
//        ingredients.add("beast");
//        List<String> steps = new ArrayList<>();
//        ingredients.add("roast it");
//
//        RecipeCreateRequest recipeCreateRequest = new RecipeCreateRequest();
//        recipeCreateRequest.setTitle(title);
//        recipeCreateRequest.setIngredients(ingredients);
//        recipeCreateRequest.setSteps(steps);
//        recipeCreateRequest.setGlutenFree(true);
//        recipeCreateRequest.setDairyFree(true);
//        recipeCreateRequest.setEggFree(false);
//        recipeCreateRequest.setVegetarian(false);
//        recipeCreateRequest.setVegan(false);
//
//        mapper.registerModule(new JavaTimeModule());
//
//        mvc.perform(post("/recipe")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(recipeCreateRequest)))
//                .andExpect(jsonPath("recipeId")
//                        .exists())
//    //            .andExpect(jsonPath("title")
//    //                    .value(is(title)))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    public void getRecipe_getSuccessful() {
//
//        String recipeId = "1234";
//
//        Recipe recipeTest = recipeService.findById("1234");
//
//        Assertions.assertEquals(recipeTest.getTitle(), "chicken soup");
//    }
}