/**
 * Created by Eric on 13/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("IngredientsListCtrl", IngredientsListCtrl);

IngredientsListCtrl.$inject = ["IngredientService"];

function IngredientsListCtrl(IngredientService) {
    var self = this;

    self.currentPage = 1;
    self.numPerPage = 10;
    self.maxSize = 5;
    self.ingredients = [];
    self.ingredientsDisplay = [];

    self.fetchIngredients = fetchIngredients;
    self.updateIngredients = updateIngredients;

    function fetchIngredients() {
        IngredientService.getIngredients()
            .then(function(res) {
                self.ingredients = res.data;
                self.updateIngredients();
            });
    }

    fetchIngredients();

    function updateIngredients() {
        var start = ((self.currentPage - 1) * self.numPerPage),
            end = start + self.numPerPage;
        self.ingredientsDisplay = self.ingredients.slice(start, end);
    }
}