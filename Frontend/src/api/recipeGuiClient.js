import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class RecipeGuiClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getRecipe', 'createRecipe'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */
//    async getRecipe(id, errorCallback) {
//        console.log("hitting here");
//      try {
//        const params = {
//          isGlutenFree: 'false',
//          isDairyFree: 'false',
//          isEggFree: 'true',
//          isVegetarian: 'false',
//          isVegan: 'false'
//        };
//
//        const response = await this.client.get(`/recipe/dietaryRestriction`, { params });
//        return response.data;
//      } catch (error) {
//        this.handleError("getRecipe", error, errorCallback);
//      }
//    }

    async getRecipe(id, errorCallback) {
        try {
            const isGlutenFree = true;
            const isDairyFree = false;
            const isEggFree = true;
            const isVegetarian = false;
            const isVegan = false;
            const response = await this.client.get(`/recipe/dietaryRestriction/${isGlutenFree}/${isDairyFree}/${isEggFree}/${isVegetarian}/${isVegan}`);
            return response.data;
        } catch (error) {
            this.handleError("getConcert", error, errorCallback)
        }
    }


    async createRecipe(name, errorCallback) {
        try {
            const response = await this.client.post(`recipe`, {
                title: "Gerauld secret sauce",
                ingredients: ["water", "flour pasta", "salt"],
                steps: ["heat water", "add pasta", "wait", "eat"],
                isGlutenFree: true,
                isDairyFree: false,
                isEggFree: false,
                isVegetarian: false,
                isVegan: false,
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
