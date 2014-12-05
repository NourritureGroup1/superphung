/**
 * Created by Eric on 30/11/2014.
 */
angular.module("NourritureApp")
    .service("fileUpload", fileUpload);

fileUpload.$inject = ["$http"];

function fileUpload($http) {
    var self = this;

    self.uploadFileToUrl = uploadFileToUrl;

    function uploadFileToUrl(url, file, idCreation, idUser) {
        var fd = new FormData();
        console.log(file);
        fd.append("file", file);
        fd.append("idCreation", idCreation);
        fd.append("idUser", idUser);
        $http.post(url, fd, {
            transformRequest: angular.identity,
            headers: {"Content-type": undefined}
        });
    }
}