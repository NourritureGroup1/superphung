/**
 * Created by Eric on 06/12/2014.
 */

angular.module("NourritureApp")
    .controller("RecipesListCtrl", RecipesListCtrl);

RecipesListCtrl.$inject = ["RecipeService"];

function RecipesListCtrl(RecipeService) {
    var self = this;

    self.currentPage = 1;
    self.numPerPage = 10;
    self.maxSize = 5;
    self.recipes = [];
    self.recipesDisplay = [];

    self.fetchRecipes = fetchRecipes;
    self.updateRecipes = updateRecipes;

    function fetchRecipes() {
        RecipeService.getRecipes()
            .then(function(res) {
                self.recipes = res.data;
                self.updateRecipes();
            });
    }

    fetchRecipes();

    function updateRecipes() {
        var start = ((self.currentPage - 1) * self.numPerPage),
            end = start + self.numPerPage;
        self.recipesDisplay = self.recipes.slice(start, end);
    }
}
