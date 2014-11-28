/**
 * Created by Eric on 26/11/2014.
 */

angular
    .module("NourritureApp")
    .config(config);

function config($routeProvider) {
    $routeProvider
        /*.when("/login", {
            templateUrl: "views/login.html"
        })*/
        .when("/account", {
            templateUrl: "views/account.html",
            controller: "MainCtrl as mainCtrl",
            resolve: { auth: auth }
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