/**
 * Created by Eric on 26/11/2014.
 */

angular.module("NourritureApp")
    .controller("MainCtrl", MainCtrl);

MainCtrl.$inject = ["UserService"];

function MainCtrl(userService) {
    var self = this;

    self.userService = userService;

    userService.session();
}