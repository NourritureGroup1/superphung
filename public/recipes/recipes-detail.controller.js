/**
 * Created by Eric on 07/12/2014.
 */

angular.module("NourritureApp")
    .controller("RecipesDetailCtrl", RecipesDetailCtrl);

RecipesDetailCtrl.$inject = ["RecipeService", "$routeParams"];

function RecipesDetailCtrl(RecipeService, $routeParams) {
    var self = this;

    self.recipe = {};
    self.fetchRecipeById = fetchRecipeById;

    function fetchRecipeById() {
        RecipeService.getRecipeById($routeParams.id)
            .then(function(res) {
                self.recipe = res.data;
            });
    }

    fetchRecipeById();
}