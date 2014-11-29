/**
 * Created by Eric on 30/11/2014.
 */

angular.module("NourritureApp")
    .factory("RecipeCreateService", RecipeCreateService);

RecipeCreateService.$inject = ["$http"];

function RecipeCreateService($http) {
    var service = {
        ingredients: [],

        getIngredient: getIngredient
    };

    return service;

    function getIngredient() {
        return $http.get("/ingredient")
            .then(function(res) {
                service.ingredients = res.data;
                console.log(JSON.stringify(service.ingredients));
                return res;
            });
    }
}
