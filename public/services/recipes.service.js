/**
 * Created by Eric on 06/12/2014.
 */

angular
    .module("NourritureApp")
    .factory("RecipeService", RecipeService);

RecipeService.$inject = ["$http"];

function RecipeService($http) {
    var service = {
        getRecipes : getRecipes,
        getRecipeById : getRecipeById,
        getRecipeIngredients: getRecipeIngredients,
        putRecipe : putRecipe
    };

    return service;

    function getRecipes() {
        return $http.get("/recipe");/*
            .then(function(res) {
                service.recipes = res.data;
                console.log(JSON.stringify(service.recipes));
                return res;
            });*/
    }

    function getRecipeById(id) {
        return $http.get("/recipe/" + id);
    }

    function getRecipeIngredients(id) {
        return $http.get("/recipe/" + id + "/ingredient");
    }

    function putRecipe(recipe) {
        return $http.put("/recipe/" + recipe._id, recipe);
    }
}