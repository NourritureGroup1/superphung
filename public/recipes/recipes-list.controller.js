/**
 * Created by Eric on 06/12/2014.
 */

angular.module("NourritureApp")
    .controller("RecipesListCtrl", RecipesListCtrl)
    .filter("FilterRecipesIngredients", FilterRecipesIngredients);

RecipesListCtrl.$inject = ["RecipeService", "UserService"];

function RecipesListCtrl(RecipeService, userService) {
    var self = this;

    self.currentPage = 1;
    self.numPerPage = 10;
    self.maxSize = 5;
    self.recipes = [];
    self.recipesDisplay = [];
    self.restrictedFood = [];
    self.recipesIngredients = [];

    self.fetchRecipes = fetchRecipes;
    self.updateRecipes = updateRecipes;

    self.fetchRestrictedFood = fetchRestrictedFood;
    //self.fetchRecipesIngredients = fetchRecipesIngredients;

    function fetchRecipes() {
        RecipeService.getRecipes()
            .then(function(res) {
                self.recipes = res.data;
                self.updateRecipes();
            });
    }

    userService.session();
    fetchRecipes();
    fetchRestrictedFood();
    //fetchRecipesIngredients();

    function updateRecipes() {
        var start = ((self.currentPage - 1) * self.numPerPage),
            end = start + self.numPerPage;
        self.recipesDisplay = self.recipes.slice(start, end);
    }

    function fetchRestrictedFood() {
        userService.getRestrictedFood(userService.user._id)
            .then(function(res) {
                if (res.data)
                    self.restrictedFood = res.data;
            });
    }

    /*function fetchRecipesIngredients() {
        userService.getRecipeIngredients(userService.user._id)
            .then(function(res) {
                if (res.data)
                    self.recipesIngredients = res.data;
            });
    }*/
}

function FilterRecipesIngredients() {
    return function(recipes, restrictedFilter, restrictedFood) {
        if (restrictedFilter) {
            var outId = [];
            var out = [];
            var ret = 0;

            for (var i = 0; i < recipes.length; i++) {
                out.push(recipes[i]);
                console.log(recipes[i]);
                for (var j = 0; j < recipes[i].ingredients.length; j++) {
                    ret = 0;
                    for (var k = 0; k < restrictedFood.length; k++) {
                        if (recipes[i].ingredients[j] == restrictedFood[k]._id) {
                            ret = 1;
                            console.log("recipes[i].ingredients[j]:", recipes[i].ingredients[j]);
                            console.log("restrictedFood[k]._id:", restrictedFood[k]._id);
                        }
                    }
                    if (!ret) {
                        outId.push(recipes[i].ingredients[j]);
                    }
                }
                out[i].ingredients = outId;
            }
            console.log(out);
            return out;
        }
        else {
            return recipes;
        }
    }
}
