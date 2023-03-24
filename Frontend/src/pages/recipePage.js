import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import RecipeClient from "../api/recipeClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class RecipePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onGet2', 'onCreate', 'renderRecipe2'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('get-by-res-form').addEventListener('submit', this.onGet2);
//        document.getElementById('clickListen').addEventListener('click', this.onDropDown);
        this.client = new RecipeClient();


        this.dataStore.addChangeListener(this.renderRecipe2)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

//    async renderRecipe() {
//        let resultArea = document.getElementById("result-getById");
//
//        const recipe = this.dataStore.get("recipe");
//
//        if (recipe) {
//            resultArea.innerHTML = `
//                <div>RECIPE ID: ${recipe.recipeId}</div>
//                <div>TITLE: ${recipe.title}</div>
//                <div>Ingredients: ${recipe.ingredients}</div>
//                <div>Steps: ${recipe.steps}</div>
//                <div>GlutenFree: ${recipe.isGlutenFree}</div>
//                <div>EggFree: ${recipe.isEggFree}</div>
//                <div>DairyFree: ${recipe.isDairyFree}</div>
//                <div>Vegetarian: ${recipe.isVegetarian}</div>
//                <div>Vegan: ${recipe.isVegan}</div>
//            `
//        } else {
//            resultArea.innerHTML = "No Item";
//        }
//    }

    async renderRecipe2() {
            let resultArea = document.getElementById("result-getByDR");

            const recipes = this.dataStore.get("recipeDR");

            //need loop for list

            if (recipes) {
                        resultArea.innerHTML = `
                                <div>
                    ${recipes.map((recipe) => ` <div>
                                            <p>Recipe Id: ${recipe.recipeId}</p>
                                            <p>Recipe Title: ${recipe.title}</p>
                                        </div>
                                    `).join('')}
                    </div>
                            `
                    } else {
                        resultArea.innerHTML = "No Item";
                    }
        }

    // Event Handlers --------------------------------------------------------------------------------------------------
    async onDropDown(event) {
//            event.preventDefault()
//            let selectElement = document.querySelector('#dropdown_value');
//            let output = selectElement.value;
//            console.log("output is: ");
//            console.log(output);
//            let result = await this.client.getAccommodations(output, this.errorHandler);
//            let resultArea = document.getElementById("result-info4");
//
//            //currently the content won't update when the user selects another option... trying this:
//            resultArea.innerHTML = "";
//
//            if (result.accommodations) {
//                resultArea.innerHTML = `
//                        <div>
//            ${result.accommodations.map((accommodation) => ` <div>
//                                    <p>${accommodation}</p>
//                                </div>
//                            `).join('')}
//            </div>
//                    `
//            } else {
//                resultArea.innerHTML = "No Item";
//            }
    }

    //byId
    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("id-field").value;
        console.log(id);
        this.dataStore.set("recipe", null);

        let result = await this.client.getRecipeById(id, this.errorHandler);
        this.dataStore.set("recipe", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }
    //byDR
    async onGet2(event) {
            // Prevent the page from refreshing on form submit
            event.preventDefault();
            console.log("button works");


            let gluten = document.getElementById("gluten-field").value;
            let dairy = document.getElementById("dairy-field").value;
            let egg = document.getElementById("egg-field").value;
            let vegetarian = document.getElementById("vegetarian-field").value;
            let vegan = document.getElementById("vegan-field").value;
            this.dataStore.set("recipeDR", null);
            console.log(gluten);

            let result = await this.client.getRecipeByDR(gluten, dairy, egg, vegetarian, vegan, this.errorHandler);
            console.log("the result:");

            this.dataStore.set("recipeDR", result);

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

        let title = document.getElementById("title").value;
        let ingredients = document.getElementById("ingredients").value;
        let steps = document.getElementById("steps").value;
        let gluten = document.getElementById("gluten").value;
        let dairy = document.getElementById("dairy").value;
        let egg = document.getElementById("egg").value;
        let vegetarian = document.getElementById("vegetarian").value;
        let vegan = document.getElementById("vegan").value;

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
    const recipePage = new RecipePage();
    recipePage.mount();
};

window.addEventListener('DOMContentLoaded', main);