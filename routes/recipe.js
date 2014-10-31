/**
 * Created by Tristan on 31/10/2014.
 */

var recipe = require("./recipeHandler")

module.exports = function (app) {
    app.get("/recipe/:id", ingredient.getById);
    app.post("/recipe", ingredient.create);
    app.get("/recipes", ingredient.getAll);
};