/**
 * Created by Eric on 11/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("GmistManageRecipesCtrl", GmistManageRecipesCtrl);

GmistManageRecipesCtrl.$inject = ["RecipeService", "IngredientService","$routeParams", "UserService", "$location"];

function GmistManageRecipesCtrl(recipeService, ingredientService, $routeParams, userService, $location) {
    var self = this;

    self.recipe = {};
    self.recipeIngredients = [];
    self.ingredients = [];
    self.ingredientsId = [];
    self.errMessageIng = "";
    self.successMessageUpdate = "";

    self.fetchRecipeById = fetchRecipeById;
    self.fetchRecipeIngredients = fetchRecipeIngredients;
    self.addStep = addStep;
    self.addIngredient = addIngredient;
    self.deleteStep = deleteStep;
    self.deleteIngredient = deleteIngredient;
    self.deleteRecipe = deleteRecipe;
    self.submit = submit;

    userService.session();
    fetchRecipeById();
    fetchIngredients();

    function fetchRecipeById() {
        recipeService.getRecipeById($routeParams.id)
            .then(function(res) {
                self.recipe = res.data;
                fetchRecipeIngredients();
            });
    }

    function fetchRecipeIngredients() {
        recipeService.getRecipeIngredients(self.recipe._id)
            .then(function(res) {
                self.recipeIngredients = res.data;
            });
    }

    function fetchIngredients() {
        ingredientService.getIngredients()
            .then(function(res) {
                self.ingredients = res.data;
            });
    }

    function addStep() {
        self.recipe.steps.push("");
    }

    function deleteStep(index) {
        self.recipe.steps.splice(index, 1);
    }

    function addIngredient() {
        self.recipeIngredients.push("");
    }

    function deleteIngredient(index) {
        self.recipeIngredients.splice(index, 1);
    }

    function deleteRecipe() {
        /*for (var i = 0; i < userService.user.recipesCreated.length; i++) {
            if (userService.user.recipesCreated[i] == self.recipe._id) {
                userService.user.recipesCreated.splice(i, 1);
                userService.updateUser(userService.user);
            }
        }*/
        recipeService.deleteRecipe(self.recipe._id)
            .then(function(res) {
                $location.path("/gastronomist-manager");
                $location.replace();
            });

    }

    function submit() {
        console.log(self.recipeIngredients);

        for(var i = 0; i < self.recipeIngredients.length; i++) {
            if (self.recipeIngredients[i]._id) {
                self.ingredientsId.push(self.recipeIngredients[i]._id);
                self.errMessageIng = "";
            }
            else {
                self.ingredientsId = [];
                self.errMessageIng = "Some Ingredients don't exist";
                self.successMessageUpdate = "";
            }
        }
        if (self.errMessageIng)
            return;
        self.errMessageIng = "";
        self.successMessageUpdate = "Recipe update";

        self.recipe.ingredients = self.ingredientsId;
        recipeService.putRecipe(self.recipe);
    }
}