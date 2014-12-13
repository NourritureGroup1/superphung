/**
 * Created by Eric on 13/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("IngredientsCreateCtrl", IngredientsCreateCtrl);

IngredientsCreateCtrl.$inject = ["$scope", "$http", "fileUpload"];

function IngredientsCreateCtrl($scope, $http, fileUpload) {
    var self = this;

    self.name = "";
    self.description = "";
    self.category = [""];
    self.nutrients = [""];
    self.errMessageFile = "";

    self.addCategory = addCategory;
    self.addNutrient = addNutrient;
    self.deleteCategory = deleteCategory;
    self.deleteNutrient = deleteNutrient;
    self.submit = submit;

    function addCategory() {
        self.category.push("");
    }

    function addNutrient() {
        self.nutrients.push("");
    }

    function deleteCategory(index) {
        self.category.splice(index, 1);
    }

    function deleteNutrient(index) {
        self.nutrients.splice(index, 1);
    }

    function submit() {

        var file = $scope.myFile;

        if (file.type != "image/png" && file.type != "image/jpg" && file.type != "image/jpeg") {
            self.errMessageFile = "File should be jpg or png";
        } else {
            self.errMessageFile = "";
        }
        if (self.errMessageFile)
            return;
        self.errMessageFile = "";

        $http.post("/ingredient", {
            name : self.name,
            description : self.description,
            category : self.category,
            nutrients : self.nutrients
        })
            .success(function(data, status, headers, config) {
                fileUpload.uploadFileToUrl("/uploadsIngredients", file, data._id);
            })
            .error();
    }
}