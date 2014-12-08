/**
 * Created by Eric on 08/12/2014.
 */

angular.module("NourritureApp")
    .controller("LoginCtrl", LoginCtrl);

LoginCtrl.$inject = ["$modalInstance"];

function LoginCtrl($modalInstance) {
    var self = this;

    self.close = close;
    self.cancel = cancel;

    function close() {
        $modalInstance.close();
    }

    function cancel() {
        $modalInstance.dismiss("cancel");
    }
}