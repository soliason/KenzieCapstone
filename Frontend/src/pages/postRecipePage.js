import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import PostRecipeClient from "../api/postRecipeClient";

console.log("post Recipe Page is working");
/**
 * Logic needed for the view playlist page of the website.
 */
class PostRecipePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onCreate'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('create-form').addEventListener('submit', this.onCreate);

        this.client = new PostRecipeClient();


    }

    // Render Methods --------------------------------------------------------------------------------------------------



    // Event Handlers --------------------------------------------------------------------------------------------------




    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("recipe", null);

        let title = document.getElementById("title").value;
        let ingredients = document.getElementById("ingredients").value;
        let steps = document.getElementById("steps").value;
        let gluten = document.getElementById("gluten").checked;
        let dairy = document.getElementById("dairy").checked;
        let egg = document.getElementById("egg").checked;
        let vegetarian = document.getElementById("vegetarian").checked;
        let vegan = document.getElementById("vegan").checked;


        const createdRecipe = await this.client.createRecipe(title, ingredients, steps, gluten, dairy, egg, vegetarian, vegan, this.errorHandler);
        this.dataStore.set("recipe", createdRecipe);

        if (createdRecipe) {
            this.showMessage(`Created ${createdRecipe.title}!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const postRecipePage = new PostRecipePage();
    await postRecipePage.mount();
};

window.addEventListener('DOMContentLoaded', main);