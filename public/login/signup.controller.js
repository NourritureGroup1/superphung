/**
 * Created by Eric on 08/12/2014.
 */

angular.module("NourritureApp")
    .controller("SignupCtrl", SignupCtrl);

SignupCtrl.$inject = ["$modalInstance", "$http", "$location"];

function SignupCtrl($modalInstance, $http, $location) {
    var self = this;

    self.showFail = "";
    self.showSuccess = "";

    self.close = close;
    self.cancel = cancel;
    self.submit = submit;

    //self.userService = userService;

    function close() {
        $modalInstance.close();
    }

    function cancel() {
        $modalInstance.dismiss("cancel");
    }

    function submit(em, pass, n) {
        $http.post("/signup", {
            email: em,
            password: pass,
            name: n
        })
            .success(function(data, status, headers, config) {
                console.log(status);
                console.log("profile cree");
                self.showFail = "";
                self.showSuccess = "Account created";
                $location.path("/account");
                self.close();
            })
            .error(function(data, status, headers, config) {
                self.showFail = "This email is already taken";
                self.showSuccess = "";
            });
        //self.userService.signup(self.user);
            /*.success(function(data, status, headers, config) {
                console.log("success");
            })
            .error(function(data, status, headers, config) {
                console.log("error");
            });*/
    }
}