import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import RecipeClient from "../api/recipeClient";

class RecipePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'rateMe', 'getRecipesMatchingDR', 'getRandomRecipe', 'onCreate', 'renderRecipe', 'renderRecipeSummary', 'getRecipe'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('result-getByDR').addEventListener('click', this.onGet);
        document.getElementById("submitBtn").addEventListener('click', this.getRecipesMatchingDR);
        document.getElementById("result-getById").addEventListener('submit', this.rateMe);
        document.getElementById("submitBtn2").addEventListener('click', this.getRandomRecipe);

        this.client = new RecipeClient();

        this.dataStore.addChangeListener(this.renderRecipe)
        this.dataStore.addChangeListener(this.renderRecipeSummary)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderRecipe() {
        let resultArea = document.getElementById("result-getById");

        const recipe = this.dataStore.get("recipe");

        if (recipe) {
            resultArea.innerHTML = `
                    <h6>TITLE: </h6>
                    <p>${recipe.title}</p>
                    <div>Ingredients: ${recipe.ingredients}</div>
                    <div>Steps: ${recipe.steps}</div>
                    <br>
                    <div>GlutenFree: ${recipe.isGlutenFree}</div>
                    <div>EggFree: ${recipe.isEggFree}</div>
                    <div>DairyFree: ${recipe.isDairyFree}</div>
                    <div>Vegetarian: ${recipe.isVegetarian}</div>
                    <div>Vegan: ${recipe.isVegan}</div>
                    <br>
                    <br>
                    <h3>Rate Me</h3>
                    <form>
                            <label>
                                <input type="checkbox" name=${recipe.recipeId} id="oneS" value=1> One Star
                            </label>
                            <br>
                            <label>
                                <input type="checkbox" id="twoS" name=${recipe.recipeId} value=2> Two Stars
                            </label>
                            <br>
                            <label>
                                <input type="checkbox" id="threeS" name=${recipe.recipeId} value=3> Three Stars
                            </label>
                            <br>
                            <label>
                                <input type="checkbox" id="fourS" name=${recipe.recipeId} value=4> Four Stars
                            </label>
                            <br>
                            <button type="submit" id="recipeButton">Submit</button>
                        </form>
                    <br>
                    <button type="submit"><a class="nav-link active" aria-current="page" href="recipe.html">Search For More Recipes</a></button>
                `
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    async renderRecipeSummary() {

        let resultArea = document.getElementById("result-getByDR");
        const recipes = this.dataStore.get("recipeDR");

        if (recipes) {

            resultArea.innerHTML = `
                                <div>
                    ${recipes.map((recipe) => ` <div class="card"></div><div class = "card-body">

            let resultArea = document.getElementById("result-getByDR");
            const recipes = this.dataStore.get("recipeDR");

            if (recipes) {

                        resultArea.innerHTML = `
                                <div>
                    ${recipes.map((recipe) => ` <div class = "card-body">

                                            <h5 class="card-title">${recipe.title}</h5>
                                            <h2>Rating: ${recipe.averageRating} of 4 Stars</h2>
                                            <a id=${recipe.recipeId} class="btn btn-primary">View Recipe</a>
                                        </div>
                                    `).join('')}
                    </div>
                    </div>
                            `
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------
    async rateMe(event) {
        event.preventDefault();

        let oneS = document.getElementById("oneS");
        let twoS = document.getElementById("twoS");
        let threeS = document.getElementById("threeS");
        let fourS = document.getElementById("fourS");

        if (oneS.checked) {
            let recipeId = oneS.name;
            let rating = 1;
            let result = await this.client.rate(recipeId, rating, this.errorHandler);
            if (!result) {
                this.errorHandler("Error doing GET!  Try again...");
            } else {
                this.showMessage("Your Rating Has Been Saved!")
            }
        }
        if (twoS.checked) {
            let recipeId = twoS.name;
            let rating = 2;
            let result = await this.client.rate(recipeId, rating, this.errorHandler);
            if (!result) {
                this.errorHandler("Error doing GET!  Try again...");
            } else {
                this.showMessage("Your Rating Has Been Saved!")
            }
        }
        if (threeS.checked) {
            let recipeId = threeS.name;
            let rating = 3;
            let result = await this.client.rate(recipeId, rating, this.errorHandler);
            if (!result) {
                this.errorHandler("Error doing GET!  Try again...");
            } else {
                this.showMessage("Your Rating Has Been Saved!")
            }
        }
        if (fourS.checked) {
            let recipeId = oneS.name;
            let rating = 4;
            let result = await this.client.rate(recipeId, rating, this.errorHandler);
            if (!result) {
                this.errorHandler("Error doing GET!  Try again...");
            } else {
                this.showMessage("Your Rating Has Been Saved!")
            }
        }
    }

    async getRandomRecipe(event) {
        event.preventDefault();

        this.dataStore.set("recipe", null);

        var gluten = document.getElementById("GF2");
        var dairy = document.getElementById("DF2");
        var egg = document.getElementById("EF2");
        var vegetarian = document.getElementById("vegetarian2");
        var vegan = document.getElementById("vegan2");
        var glutenValue = false;
        var dairyValue = false;
        var eggValue = false;
        var vegetarianValue = false;
        var veganValue = false;

        if (gluten.checked == true) {
            glutenValue = true;
        }
        if (dairy.checked == true) {
            dairyValue = true;
        }
        if (egg.checked == true) {
            eggValue = true;
        }
        if (vegetarian.checked == true) {
            vegetarianValue = true;
        }
        if (vegan.checked == true) {
            veganValue = true;
        }

        let result = await this.client.getRecipeByDR(glutenValue, dairyValue, eggValue, vegetarianValue, veganValue, this.errorHandler);
        let max = result.length - 1;
        let randomNumber = Math.floor(Math.random() * (max - 0));
        let recipeId = result[randomNumber].recipeId;
        let result2 = await this.client.getRecipeById(recipeId, this.errorHandler);
        console.log("before hide");
        let dietaryRestrictions = document.getElementById("dietary-restrictions");
        let recipeToSee = document.getElementById("result3");
        dietaryRestrictions.style.display = "none";
        result3.style.display = "block";
        console.log("after")

        this.dataStore.set("recipe", result2);
        if (!result) {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = event.target.id;

        this.dataStore.set("recipeDR", null);
        let dietaryRestrictions = document.getElementById("hideMe");

        let result = await this.client.getRecipeById(id, this.errorHandler);
        let hide = document.getElementById("hidden-results");
        let unhide = document.getElementById("result3")
        hide.style.display = "none";
        unhide.style.display = "block";
        this.dataStore.set("recipe", result);
        if (!result) {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async getRecipesMatchingDR(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        var gluten = document.getElementById("GF");
        var dairy = document.getElementById("DF");
        var egg = document.getElementById("EF");
        var vegetarian = document.getElementById("vegetarian");
        var vegan = document.getElementById("vegan");
        var glutenValue = false;
        var dairyValue = false;
        var eggValue = false;
        var vegetarianValue = false;
        var veganValue = false;

        if (gluten.checked == true) {
            glutenValue = true;
        }
        if (dairy.checked == true) {
            dairyValue = true;
        }
        if (egg.checked == true) {
            eggValue = true;
        }
        if (vegetarian.checked == true) {
            vegetarianValue = true;
        }
        if (vegan.checked == true) {
            veganValue = true;
        }

        this.dataStore.set("recipeDR", null);

        let result = await this.client.getRecipeByDR(glutenValue, dairyValue, eggValue, vegetarianValue, veganValue, this.errorHandler);

        let dietaryRestrictions = document.getElementById("hideMe");
        let dietaryRestrictions2 = document.getElementById("hideMe2");
        dietaryRestrictions.style.display = "none";
        dietaryRestrictions2.style.display = "none";

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