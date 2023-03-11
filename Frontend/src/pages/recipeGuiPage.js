import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import RecipeGuiClient from "../api/recipeGuiClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class RecipeGuiPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onCreate', 'renderRecipe'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('get-by-id-form').addEventListener('submit', this.onGet);
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
        this.client = new RecipeGuiClient();

        this.dataStore.addChangeListener(this.renderRecipe)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderRecipe() {
//        let resultArea = document.getElementById("result-info");
//
//        const recipe = this.dataStore.get("recipe");
//
//        if (recipe) {
//            resultArea.innerHTML = `
//                <div>ID: ${example.id}</div>
//                <div>Name: ${example.name}</div>
//            `
//        } else {
//            resultArea.innerHTML = "No Item";
//        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("id-field").value;
        this.dataStore.set("recipe", null);

        let result = await this.client.getRecipe(id, this.errorHandler);
        this.dataStore.set("recipe", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("recipe", null);

//        let name = document.getElementById("create-name-field").value;

        const createdRecipe = await this.client.createRecipe("name", this.errorHandler);
        this.dataStore.set("recipe", createdRecipe);

        if (createdRecipe) {
            this.showMessage(`Created ${createdRecipe.name}!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const recipeGuiPage = new RecipeGuiPage();
    recipeGuiPage.mount();
};

window.addEventListener('DOMContentLoaded', main);