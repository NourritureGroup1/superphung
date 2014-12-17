/**
 * Created by Eric on 11/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("GmistCtrl", GmistCtrl);

GmistCtrl.$inject = ["RecipeService", "IngredientService", "SortService", "$location"];

function GmistCtrl(recipeService, ingredientService, sortService, $location) {
    var self = this;

    self.currentPage = 1;
    self.numPerPage = 10;
    self.maxSize = 5;

    //recipes array
    self.recipes = [];
    self.recipesDisplay = [];

    //ingredients array
    self.ingredients = [];
    self.ingredientsDisplay = [];

    //recipes methods
    self.showRecipe = showRecipe;
    self.fetchRecipes = fetchRecipes;
    self.updateRecipes = updateRecipes;
    self.createRecipe = createRecipe;

    //ingredients methods
    self.showIngredient = showIngredient;
    self.fetchIngredients = fetchIngredients;
    self.updateIngredients = updateIngredients;
    self.createIngredient = createIngredient;

    self.sortNameAsc = sortNameAsc;
    self.sortNameDesc = sortNameDesc;
    self.sortCreatorAsc = sortCreatorAsc;
    self.sortCreatorDesc = sortCreatorDesc;

    fetchRecipes();
    fetchIngredients();

    function showRecipe(recipe) {
        $location.path("/gastronomist-manager/recipes/" + recipe._id);
    }

    function fetchRecipes() {
        recipeService.getRecipes()
            .success(function(data, status, header, config) {
                console.log("recipeService.getRecipes() success : " + status);
                self.recipes = data;
                self.updateRecipes();
            })
            .error(function(data, status, header, config) {
                console.log("recipeService.getRecipes() error : " + status);
            });
    }

    function updateRecipes() {
        var start = ((self.currentPage - 1) * self.numPerPage),
            end = start + self.numPerPage;
        self.recipesDisplay = self.recipes.slice(start, end);
    }

    function createRecipe() {
        $location.path("/recipes-create");
    }

    function showIngredient(ingredient) {
        $location.path("/gastronomist-manager/ingredients/" + ingredient._id);
    }

    function fetchIngredients() {
        ingredientService.getIngredients()
            .success(function(data, status, header, config) {
                console.log("ingredientService.getIngredients() success : " + status);
                self.ingredients = data;
                self.updateIngredients();
            })
            .error(function(data, status, header, config) {
                console.log("ingredientService.getIngredients() success : " + status);
            });
    }

    function updateIngredients() {
        var start = ((self.currentPage - 1) * self.numPerPage),
            end = start + self.numPerPage;
        self.ingredientsDisplay = self.ingredients.slice(start, end);
    }

    function createIngredient() {
        $location.path("/ingredients-create");
    }

    function sortNameAsc() {
        self.recipes.sort(sortService.nameAsc);
        self.updateRecipes();
    }

    function sortNameDesc() {
        self.recipes.sort(sortService.nameDesc);
        self.updateRecipes();
    }

    function sortCreatorAsc() {
        self.recipes.sort(sortService.creatorAsc);
        self.updateRecipes();
    }

    function sortCreatorDesc() {
        self.recipes.sort(sortService.creatorDesc);
        self.updateRecipes();
    }
}