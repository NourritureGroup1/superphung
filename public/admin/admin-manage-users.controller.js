/**
 * Created by Eric on 10/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("AdminManageUsersCtrl", AdminManageUsersCtrl);

AdminManageUsersCtrl.$inject = ["UserService", "$routeParams"];

function AdminManageUsersCtrl(userService, $routeParams) {
    var self = this;

    self.user = {};
    self.role = [
        "consumer",
        "gastronomist",
        "supplier",
        "admin"
    ];
    self.showSuccess = "";

    self.fetchUserById = fetchUserById;
    self.submit = submit;

    fetchUserById();

    function fetchUserById() {
        userService.getUserById($routeParams.id)
            .then(function(res) {
                self.user = res.data;
            });
    }

    function submit() {
        userService.updateUser(self.user)
            .success(function(data, status, headers, config) {
                self.showSuccess = "user update !";
                console.log("user update !!");
            })
            .error(function(data, status, headers, config) {
                self.showSuccess = "";
            });
    }
}