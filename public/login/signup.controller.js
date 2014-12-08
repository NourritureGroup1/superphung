/**
 * Created by Eric on 08/12/2014.
 */

angular.module("NourritureApp")
    .controller("SignupCtrl", SignupCtrl);

SignupCtrl.$inject = ["$modalInstance", "$http"];

function SignupCtrl($modalInstance, $http) {
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

    function submit(em, pass) {
        $http.post("/signup", {
            email: em,
            password: pass
        })
            .success(function(data, status, headers, config) {
                console.log("profile cree");
                self.showFail = "";
                self.showSuccess = "Account created";
                self.cancel();
            })
            .error(function(data, status, headers, config) {
                console.log("user exist hahahah");
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