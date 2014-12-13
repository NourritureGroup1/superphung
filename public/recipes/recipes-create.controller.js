/**
 * Created by Eric on 29/11/2014.
 */

angular.module("NourritureApp")
    .controller("RecipesCreateCtrl", RecipesCreateCtrl);

RecipesCreateCtrl.$inject = ["RecipeCreateService", "UserService", "$scope"];

function RecipesCreateCtrl(recipeCreateService, userService, $scope) {
    var self = this;

    self.name = "";
    self.description = "";
    self.steps = [""];
    self.ingredients = [""];
    self.ingredientsId = [];
    self.errMessageIng = "";
    self.errMessageFile = "";

    self.addStep = addStep;
    self.addIngredient = addIngredient;
    self.deleteStep = deleteStep;
    self.deleteIngredient = deleteIngredient;
    self.submit = submit;

    self.recipeCreateService = recipeCreateService;

    recipeCreateService.getIngredient();
    userService.session();

    function addStep() {
        self.steps.push("");
    }

    function deleteStep(index) {
        self.steps.splice(index, 1);
    }

    function addIngredient() {
        self.ingredients.push("");
    }

    function deleteIngredient(index) {
        self.ingredients.splice(index, 1);
    }

    function submit() {
        console.log(self.ingredients[0]);

        for(var i = 0; i < self.ingredients.length; i++) {
            if (self.ingredients[i]._id) {
                self.ingredientsId.push(self.ingredients[i]._id);
                self.errMessageIng = "";
            }
            else {
                self.ingredientsId = [];
                self.errMessageIng = "Some Ingredients don't exist";
            }
        }

        //check file img upload
        var file = $scope.myFile;

        console.log("file = ", file);
        console.log(file.type);

        if (file.type != "image/png" && file.type != "image/jpg" && file.type != "image/jpeg") {
            self.errMessageFile = "File should be jpg or png";
        } else {
            self.errMessageFile = "";
        }
        if (self.errMessageIng || self.errMessageFile)
            return;

        self.errMessageIng = "";
        self.errMessageFile = "";
        console.log("ingredientsId = " + self.ingredientsId);
        console.log("send data");
        console.log("username = " + userService.user.name);

        //send recipe input field
        self.recipeCreateService.postRecipe({
            name: self.name,
            creator: userService.user.name,
            description: self.description,
            steps: self.steps,
            ingredients: self.ingredientsId
        }, file, userService.user._id);



        //var file = $scope.myFile;
        //var uploadUrl = "/uploads";
        //fileUpload.uploadFileToUrl(file);
    }
}