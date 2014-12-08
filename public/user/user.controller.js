/**
 * Created by Eric on 26/11/2014.
 */

angular.module("NourritureApp")
    .controller("UserCtrl", UserCtrl);

UserCtrl.$inject = ["UserService", "$modal"];

function UserCtrl(userService, $modal) {
    var self = this;

    self.openLogin = openLogin;
    self.openSignup = openSignup;
    self.userService = userService;

    userService.session();

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