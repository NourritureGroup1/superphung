/**
 * Created by Eric on 08/12/2014.
 */

angular.module("NourritureApp")
    .controller("LoginCtrl", LoginCtrl);

LoginCtrl.$inject = ["$modalInstance", "$http", "$location"];

function LoginCtrl($modalInstance, $http, $location) {
    var self = this;

    self.showFail = "";

    self.close = close;
    self.cancel = cancel;
    self.submit = submit;

    function close() {
        $modalInstance.close();
    }

    function cancel() {
        $modalInstance.dismiss("cancel");
    }

    function submit(em, pass) {
        $http.post("/login", {
            email: em,
            password: pass
        })
            .success(function(data, status, header, config) {
                self.showFail = "";
                self.close();
                $location.path("/account");

            })
            .error(function(data, status, header, config) {
                self.showFail = "Fail, try again :)"
            });
    }
}