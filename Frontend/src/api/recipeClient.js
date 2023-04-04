import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class RecipeClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getRecipeByDR', 'getRecipeById', 'createRecipe', 'rate'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async getRecipeById(id, errorCallback) {
      try {
        const response = await this.client.get(`/recipe/${id}`);
        return response.data;
      } catch (error) {
        this.handleError("getRecipe", error, errorCallback);
      }
    }

    async rate(recipeId, rating, errorCallback) {
            console.log("got here");
              try {
                const response = await this.client.put(`/recipe/rating`, {
                    recipeId: recipeId,
                    newRating: rating,

                });
                return response.data;
              } catch (error) {
                this.handleError("getRecipe", error, errorCallback);
              }
    }

    async getRecipeByDR(gluten, dairy, egg, vegetarian, vegan, errorCallback) {

        try {
            const isGlutenFree = gluten;
            const isDairyFree = dairy;
            const isEggFree = egg;
            const isVegetarian = vegetarian;
            const isVegan = vegan;
            const response = await this.client.get(`/recipe/dietaryRestriction/${isGlutenFree}/${isDairyFree}/${isEggFree}/${isVegetarian}/${isVegan}`);
            return response.data;
        } catch (error) {
            this.handleError("getRecipe", error, errorCallback)
        }
    }


    async createRecipe(title, ingredients, steps, gluten, dairy, egg, vegetarian, vegan, errorCallback) {
        try {
            const response = await this.client.post(`recipe`, {
                title: title,
                ingredients: [ingredients],
                steps: [steps],
                isGlutenFree: gluten,
                isDairyFree: dairy,
                isEggFree: egg,
                isVegetarian: vegetarian,
                isVegan: vegan,
            });
            return response.data;
        } catch (error) {
            this.handleError("createRecipe", error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
