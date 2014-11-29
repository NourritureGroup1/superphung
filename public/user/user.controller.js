/**
 * Created by Eric on 26/11/2014.
 */

angular.module("NourritureApp")
    .controller("UserCtrl", UserCtrl);

UserCtrl.$inject = ["UserService"];

function UserCtrl(userService) {
    var self = this;

    self.userService = userService;

    userService.session();
}