import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import RecipeClient from "../api/recipeClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class RecipePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'getRecipesMatchingDR', 'onCreate', 'renderRecipe', 'renderRecipe2', 'getRecipe'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('get-by-res-form').addEventListener('submit', this.getRecipesMatchingDR);
        document.getElementById('result-getByDR').addEventListener('click', this.onGet);

        this.client = new RecipeClient();

        this.dataStore.addChangeListener(this.renderRecipe)
        this.dataStore.addChangeListener(this.renderRecipe2)
    }

    // Hide an Element_____________________________________

//    async myFunction() {
//          console.log("pushed");
//          var x = document.getElementById("testing");
//          if (x.style.display === "none") {
//            x.style.display = "block";
//          } else {
//            x.style.display = "none";
//          }
//    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderRecipe() {
            let resultArea = document.getElementById("result-getById");

            const recipe = this.dataStore.get("recipe");

            if (recipe) {
                resultArea.innerHTML = `
                    <h6>TITLE: </h6>
                    <p>     ${recipe.title}</p>
                    <div>Ingredients: ${recipe.ingredients}</div>
                    <div>Steps: ${recipe.steps}</div>
                    <br>
                    <div>GlutenFree: ${recipe.isGlutenFree}</div>
                    <div>EggFree: ${recipe.isEggFree}</div>
                    <div>DairyFree: ${recipe.isDairyFree}</div>
                    <div>Vegetarian: ${recipe.isVegetarian}</div>
                    <div>Vegan: ${recipe.isVegan}</div>
                `
            } else {
                resultArea.innerHTML = "No Item";
            }
        }

    async renderRecipe2() {
            let resultArea = document.getElementById("result-getByDR");

            const recipes = this.dataStore.get("recipeDR");

            if (recipes) {
                        resultArea.innerHTML = `
                                <div>
                    ${recipes.map((recipe) => ` <div>
                                            <h1 id = ${recipe.recipeId}>${recipe.title}</h1>
                                            <h2>Rating: COMING SOON</h2>
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

        let id = event.target.id;

        this.dataStore.set("recipe", null);

        let result = await this.client.getRecipeById(id, this.errorHandler);
        console.log(result);
        this.dataStore.set("recipe", result);
        if (!result) {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }
    //byDR
    async getRecipesMatchingDR(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let gluten = document.getElementById("gluten-field").value;
        let dairy = document.getElementById("dairy-field").value;
        let egg = document.getElementById("egg-field").value;
        let vegetarian = document.getElementById("vegetarian-field").value;
        let vegan = document.getElementById("vegan-field").value;

        this.dataStore.set("recipeDR", null);

        let result = await this.client.getRecipeByDR(gluten, dairy, egg, vegetarian, vegan, this.errorHandler);

        let dietaryRestrictions = document.getElementById("dietary-restrictions");

        if (dietaryRestrictions.style.display === "none") {
            dietaryRestrictions.style.display = "block";
        } else {
            dietaryRestrictions.style.display = "none";
        }

        let resultsElement = document.getElementById("hidden-results");

        if (resultsElement.style.display === "block") {
            resultsElement.style.display = "none";
        } else {
            resultsElement.style.display = "block";
        }

        let resultsElement2 = document.getElementById("dietary-restrictions");
        resultsElement2.style.display = "none";



        this.dataStore.set("recipeDR", result);

        if (!result) {

            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async getRecipe(event) {
        event.preventDefault();

        let id = event.target.id;
        console.log(id);
        let result = await this.client.getRecipeById(id, this.errorHandler);
        console.log(result);

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