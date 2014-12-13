/**
 * Created by Eric on 07/12/2014.
 */

angular.module("NourritureApp")
    .controller("RecipesDetailCtrl", RecipesDetailCtrl);

RecipesDetailCtrl.$inject = ["RecipeService", "UserService", "$routeParams"];

function RecipesDetailCtrl(RecipeService, userService, $routeParams) {
    var self = this;

    self.recipe = {};
    self.fetchRecipeById = fetchRecipeById;
    self.likeRecipe = likeRecipe;
    self.removeLikeRecipe = removeLikeRecipe;
    self.showLike = showLike;

    userService.session();
    fetchRecipeById();

    function fetchRecipeById() {
        RecipeService.getRecipeById($routeParams.id)
            .then(function(res) {
                self.recipe = res.data;
            });
    }

    function likeRecipe() {
        userService.user.likes.push($routeParams.id);
        userService.updateUser(userService.user);
    }

    function removeLikeRecipe() {
        for (var i = 0; i < userService.user.likes.length; i++) {
            if (userService.user.likes[i] == $routeParams.id) {
                userService.user.likes.splice(i, 1);
                userService.updateUser(userService.user);
            }
        }
    }

    function showLike() {
        for (var i = 0; i < userService.user.likes.length; i++) {
            if (userService.user.likes[i] == $routeParams.id)
                return true;
        }
        return false;
    }
}