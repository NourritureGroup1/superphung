/**
 * Created by Eric on 26/11/2014.
 */

angular.module("NourritureApp")
    .controller("UserCtrl", UserCtrl);

UserCtrl.$inject = ["UserService", "$modal", "IngredientService", "$location"];

function UserCtrl(userService, $modal, ingredientService, $location) {
    var self = this;

    self.likesRecipes = [];
    self.restrictedFood = [];
    self.restrictedFoodId = [];
    self.ingredients = [];

    self.openLogin = openLogin;
    self.openSignup = openSignup;
    self.showWelcome = showWelcome;
    self.userService = userService;

    self.fetchLikesRecipes = fetchLikesRecipes;
    self.fetchRestrictedFood = fetchRestrictedFood;
    self.fetchIngredients = fetchIngredients;

    self.addRestrictedFood = addRestrictedFood;
    self.deleteRestrictedFood = deleteRestrictedFood;
    self.submit = submit;

    userService.session();
    if (userService.isLoggedIn) {
        fetchLikesRecipes();
        fetchRestrictedFood();
        fetchIngredients();
    }


    function openLogin() {
        $modal.open({
            templateUrl: "../login/login.html",
            controller: "LoginCtrl as loginCtrl"
        });
    }

    function openSignup() {
        $modal.open({
            templateUrl: "../login/signup.html",
            controller: "SignupCtrl as signupCtrl"
        });
    }

    function showWelcome() {
        return ($location.path() == "/");
    }

    function fetchLikesRecipes() {
        userService.getLikesRecipes(self.userService.user._id)
            .then(function(res) {
                self.likesRecipes = res.data;
            });
    }

    function fetchRestrictedFood() {
        userService.getRestrictedFood(self.userService.user._id)
            .then(function(res) {
                if (res.data)
                    self.restrictedFood = res.data;
            });
    }

    function fetchIngredients() {
        ingredientService.getIngredients()
            .then(function(res) {
                self.ingredients = res.data;
            });
    }

    function addRestrictedFood() {
        self.restrictedFood.push("");
    }

    function deleteRestrictedFood(index) {
        self.restrictedFood.splice(index, 1);
    }

    function submit() {

        self.restrictedFoodId = [];
        for (var i = 0; i < self.restrictedFood.length; i++) {
            if (self.restrictedFood[i]._id)
                self.restrictedFoodId.push(self.restrictedFood[i]._id);
        }
        self.userService.user.restrictedFood = self.restrictedFoodId;
        userService.updateUser(self.userService.user);
    }
}

/*ModalLoginCtrl.$inject = ["$modalInstance"];

function ModalLoginCtrl($modalInstance) {
    var self = this;

    self.ok = ok;
    self.cancel = cancel;

    function ok() {
        $modalInstance.close();
    }

    function cancel() {
        $modalInstance.dismiss("cancel");
    }
}*/