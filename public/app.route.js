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
        .when("/admin-manager", {
            templateUrl: "admin/admin-manager.html",
            controller: "AdminCtrl as adminCtrl",
            resolve: { auth : auth , isAdmin : isAdmin}
        })
        .when("/admin-manager/users/:id", {
            templateUrl: "admin/admin-manage-users.html",
            controller: "AdminManageUsersCtrl as adminMUCtrl",
            resolve: { auth : auth , isAdmin : isAdmin}
        })
        .when("/gastronomist-manager", {
            templateUrl: "gastronomist/gastronomist-manager.html",
            controller: "GmistCtrl as gmistCtrl",
            resolve: { auth : auth , isGastronomist : isGastronomist}
        })
        .when("/gastronomist-manager/recipes/:id", {
            templateUrl: "gastronomist/gastronomist-manage-recipes.html",
            controller: "GmistManageRecipesCtrl as gmistMRCtrl",
            resolve: { auth : auth , isGastronomist : isGastronomist}
        })
        .when("/gastronomist-manager/ingredients/:id", {
            templateUrl: "gastronomist/gastronomist-manage-ingredients.html",
            controller: "GmistManageIngredientsCtrl as gmistMICtrl",
            resolve: { auth : auth, isGastronomist: isGastronomist}
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

isAdmin.$inject = ["$location", "UserService"];

function isAdmin($location, UserService) {
    if (!UserService.isAdmin()) {
        $location.path("/");
        $location.replace();
    }
}

isGastronomist.$inject = ["$location", "UserService"];

function isGastronomist($location, UserService) {
    if (!UserService.isGastronomist()) {
        $location.path("/");
        $location.replace();
    }
}