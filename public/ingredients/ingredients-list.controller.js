/**
 * Created by Eric on 13/12/2014.
 */

angular
    .module("NourritureApp")
    .controller("IngredientsListCtrl", IngredientsListCtrl)
    .filter("FilterIngredient", FilterIngredient);

IngredientsListCtrl.$inject = ["IngredientService", "UserService"];

function IngredientsListCtrl(IngredientService, userService) {
    var self = this;

    self.currentPage = 1;
    self.numPerPage = 10;
    self.maxSize = 5;
    self.ingredients = [];
    self.ingredientsDisplay = [];
    self.restrictedFood = [];

    self.fetchIngredients = fetchIngredients;
    self.updateIngredients = updateIngredients;

    self.fetchRestrictedFood = fetchRestrictedFood;

    function fetchIngredients() {
        IngredientService.getIngredients()
            .then(function(res) {
                self.ingredients = res.data;
                self.updateIngredients();
            });
    }

    userService.session();
    fetchIngredients();
    fetchRestrictedFood();

    function updateIngredients() {
        var start = ((self.currentPage - 1) * self.numPerPage),
            end = start + self.numPerPage;
        self.ingredientsDisplay = self.ingredients.slice(start, end);
    }

    function fetchRestrictedFood() {
        userService.getRestrictedFood(userService.user._id)
            .then(function(res) {
                if (res.data)
                    self.restrictedFood = res.data;
                console.log(self.restrictedFood);
            });
    }
}

function FilterIngredient() {
    return function(items, restrictedFilter, restrictedFood) {

        if (restrictedFilter) {
            var out = [];
            var ret = 0;
            for (var i = 0; i < items.length; i++) {
                ret = 0;
                for (var j = 0; j < restrictedFood.length; j++) {
                   if (items[i].name == restrictedFood[j].name) {
                        ret = 1;
                    }
                }
                if (!ret) {
                    out.push(items[i]);
                }
            }
            return out;
        }
        else {
            return items;
        }
    }
}