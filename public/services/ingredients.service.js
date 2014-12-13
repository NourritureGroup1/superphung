/**
 * Created by Eric on 11/12/2014.
 */

angular
    .module("NourritureApp")
    .factory("IngredientService", IngredientService);

IngredientService.$inject = ["$http"];

function IngredientService($http) {
    var service = {
        getIngredients: getIngredients,
        getIngredientById: getIngredientById,
        putIngredient: putIngredient
    };

    return service;

    function getIngredients() {
        return $http.get("/ingredient");
    }

    function getIngredientById(id) {
        return $http.get("/ingredient/" + id);
    }

    function putIngredient(ingredient) {
        return $http.put("/ingredient/" + ingredient._id, ingredient);
    }
}