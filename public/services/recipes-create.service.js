/**
 * Created by Eric on 30/11/2014.
 */

angular.module("NourritureApp")
    .factory("RecipeCreateService", RecipeCreateService);

RecipeCreateService.$inject = ["$http", "fileUpload"];

function RecipeCreateService($http, fileUpload) {
    var service = {
        ingredients: [],

        getIngredient: getIngredient,
        postRecipe: postRecipe
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

    /*function postRecipe(newRecipe, file) {
        return $http.post("/recipe", newRecipe)
            .then(function(recipeData) {
                if (recipeData.status != 409 && recipeData.status != 500) {
                  return  fileUpload.uploadFileToUrl("/uploadsRecipes", file, recipeData);
                }
            })
            .then(function(fileData) {
                $http.post("/recipe/" + recipeData)
            });
    }*/
    function postRecipe(newRecipe, file, userId) {
        $http.post("/recipe", newRecipe).
            success(function (data, status, headers, config) {
                console.log("data=", data);
                fileUpload.uploadFileToUrl("/uploadsRecipes", file, data._id, userId);
            }).
            error(function (data, status, headers, config) {
                console.log(status);
            });
    }
}
