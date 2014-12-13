/**
 * Created by Eric on 13/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("IngredientsDetailCtrl", IngredientsDetailCtrl);

IngredientsDetailCtrl.$inject = ["IngredientService", "UserService", "$routeParams"];

function IngredientsDetailCtrl(ingredientService, userService, $routeParams) {
    var self = this;

    self.ingredient = {};
    self.fetchIngredientById = fetchIngredientById;
    self.likeIngredient = likeIngredient;
    self.removeLikeIngredient = removeLikeIngredient;
    self.showLike = showLike;

    userService.session();
    fetchIngredientById();

    function fetchIngredientById() {
        ingredientService.getIngredientById($routeParams.id)
            .then(function(res) {
                self.ingredient = res.data;
            });
    }

    function likeIngredient() {
        userService.user.favoriteFood.push($routeParams.id);
        userService.updateUser(userService.user);
    }

    function removeLikeIngredient() {
        for (var i = 0; i < userService.user.favoriteFood.length; i++) {
            if (userService.user.favoriteFood[i] == $routeParams.id) {
                userService.user.favoriteFood.splice(i, 1);
                userService.updateUser(userService.user);
            }
        }
    }

    function showLike() {
        for (var i = 0; i < userService.user.favoriteFood.length; i++) {
            if (userService.user.favoriteFood[i] == $routeParams.id)
                return true;
        }
        return false;
    }
}