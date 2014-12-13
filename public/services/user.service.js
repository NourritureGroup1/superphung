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
        isAdmin: isAdmin,
        isGastronomist: isGastronomist,
        signup: signup,
        getAllUsers: getAllUsers,
        getUserById: getUserById,
        updateUser: updateUser,
        getLikesRecipes: getLikesRecipes,
        getRestrictedFood: getRestrictedFood
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

    function isAdmin() {
        return (service.user.role == "admin");
        /*if (service.user.role == "admin")
            return true;
        return false;*/
    }

    function isGastronomist() {
        return (service.user.role == "gastronomist");
       /*if (service.user.role == "gastronomist") {
            return true;
        }
        return false;*/
    }

    function signup(data) {
        return $http.post("/signup", data);
    }

    function getAllUsers() {
        return $http.get("/user");
    }

    function getUserById(id) {
        return $http.get("/user/" + id);
    }

    function updateUser(user) {
        return $http.put("/user/" + user._id, user);
    }

    function getLikesRecipes(id) {
        return $http.get("/user/" + id + "/likes");
    }

    function getRestrictedFood(id) {
        return $http.get("/user/" + id + "/rfood");
    }
}