/**
 * Created by Eric on 26/11/2014.
 */

angular
    .module("NourritureApp")
    .config(config);

function config($routeProvider) {
    $routeProvider
        /*.when("/login", {
            templateUrl: "login/login.html"
        })*/
        .when("/account", {
            templateUrl: "user/account.html",
            controller: "UserCtrl as userCtrl",
            resolve: { auth: auth }
        })
        .when("/recipes-list", {
            templateUrl: "recipes/recipes-list.html",
            controller: "RecipesListCtrl as recipesListCtrl",
            resolve: { auth : auth }
        })
        .when("/recipes/:id", {
            templateUrl: "recipes/recipes-detail.html",
            controller: "RecipesDetailCtrl as recipesDetailCtrl",
            resolve: { auth : auth }
        })
        .when("/recipes-create", {
            templateUrl: "recipes/recipes-create.html",
            controller: "RecipesCreateCtrl as recipeCreateCtrl",
            resolve: { auth : auth }
        })
        .otherwise({
            redirectTo: "/"
        });
}

auth.$inject = ["$q", "$location", "UserService"];

function auth($q, $location, UserService) {
    return UserService.session().then(
        function(success) {},
        function(err) {
            $location.path("/");
            $location.replace();
            return $q.reject(err);
        });
}