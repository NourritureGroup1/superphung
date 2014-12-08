/**
 * Created by Eric on 26/11/2014.
 */

angular
    .module("NourritureApp")
    .factory("UserService", UserService);

UserService.$inject = ["$http"];

function UserService($http) {
    var service = {
        isLoggedIn: false,
        spinner: false,
        user: {},

        session: session,
        signup: signup
    };

    return service;

    function session() {
        return $http.get("/session")
            .then(function(res) {
                service.user = res.data;
                service.isLoggedIn = true;
                return res;
            });
    }

    function signup(data) {
        return $http.post("/signup", data);
    }
}