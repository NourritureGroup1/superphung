/**
 * Created by Tristan on 31/10/2014.
 */

var recipe = require("./recipeHandler")

module.exports = function (app) {
    app.get("/recipe/:id", recipe.getById);
    app.post("/recipe", recipe.create);
    app.get("/recipe", recipe.getAll);
};