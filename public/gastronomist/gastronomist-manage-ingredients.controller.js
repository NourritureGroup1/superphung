/**
 * Created by Eric on 13/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("GmistManageIngredientsCtrl", GmistManageIngredientsCtrl);

GmistManageIngredientsCtrl.$inject = ["IngredientService", "$routeParams"];

function GmistManageIngredientsCtrl(ingredientService, $routeParams) {
    var self = this;

    self.ingredient = {};
    self.successMessageUpdate = "";

    self.fetchIngredientById = fetchIngredientById;
    self.addCategory = addCategory;
    self.addNutrient = addNutrient;
    self.deleteCategory = deleteCategory;
    self.deleteNutrient = deleteNutrient;
    self.deleteIngredient = deleteIngredient;
    self.submit = submit;

    fetchIngredientById();

    function fetchIngredientById() {
        ingredientService.getIngredientById($routeParams.id)
            .then(function(res) {
                self.ingredient = res.data;
            });
    }

    function addCategory() {
        self.ingredient.category.push("");
    }

    function addNutrient() {
        self.ingredient.nutrients.push("");
    }

    function deleteCategory(index) {
        self.ingredient.category.splice(index, 1);
    }

    function deleteNutrient(index) {
        self.ingredient.nutrients.splice(index, 1);
    }

    function deleteIngredient() {
        ingredientService.deleteIngredient(self.ingredient._id)
            .then(function(res) {
                $location.path("/gastronomist-manager");
                $location.replace();
            });
    }

    function submit() {
        ingredientService.putIngredient(self.ingredient)
            .success(function() {
                self.successMessageUpdate = "Ingredient update";
            })
            .error(function() {
                self.successMessageUpdate = "";
            });
    }
}