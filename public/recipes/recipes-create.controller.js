/**
 * Created by Eric on 29/11/2014.
 */

angular.module("NourritureApp")
    .controller("RecipesCreateCtrl", RecipesCreateCtrl);

RecipesCreateCtrl.$inject = ["RecipeCreateService"];

function RecipesCreateCtrl(recipeCreateService) {
    var self = this;

    self.steps = [""];
    self.ingredients = [""];
    self.ingredientsId = [""];
    self.addStep = addStep;
    self.addIngredient = addIngredient;

    self.recipeCreateService = recipeCreateService;

    recipeCreateService.getIngredient();

    function addStep() {
        self.steps.push("");
    }

    function addIngredient() {
        self.ingredients.push("");
    }
}