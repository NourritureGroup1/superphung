/**
 * Created by Eric on 10/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("AdminCtrl", AdminCtrl);

AdminCtrl.$inject = ["UserService", "SortService", "$location"];

function AdminCtrl(userService, sortService, $location) {
    var self = this;

    self.currentPage = 1;
    self.numPerPage = 10;
    self.maxSize = 5;
    self.users = [];
    self.usersDisplay = [];


    self.showUser = showUser;
    self.fetchUsers = fetchUsers;
    self.updateUsers = updateUsers;

    self.sortRoleAsc = sortRoleAsc;
    self.sortRoleDesc = sortRoleDesc;
    self.sortNameAsc = sortNameAsc;
    self.sortNameDesc = sortNameDesc;

    function showUser(user) {
        $location.path("/admin-manager/users/" + user._id);
    }

    function fetchUsers() {
        userService.getAllUsers()
            .success(function(data, status, header, config) {
                console.log("userService.getAllUsers() success : " + status);
                self.users = data;
                self.updateUsers();
            })
            .error(function(data, status, header, config) {
                console.log("userService.getAllUsers() error : " + status);
            });
    }

    fetchUsers();

    function updateUsers() {
        var start = ((self.currentPage - 1) * self.numPerPage),
            end = start + self.numPerPage;
        self.usersDisplay = self.users.slice(start, end);
    }

    //Sort functions
    function sortRoleAsc() {
        self.users.sort(sortService.roleAsc);
        self.updateUsers();
    }

    function sortRoleDesc() {
        self.users.sort(sortService.roleDesc);
        self.updateUsers();
    }

    function sortNameAsc() {
        self.users.sort(sortService.nameAsc);
        self.updateUsers();
    }

    function sortNameDesc() {
        self.users.sort(sortService.nameDesc);
        self.updateUsers();
    }
}