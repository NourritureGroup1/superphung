/**
 * Created by Tristan on 31/10/2014.
 */

var recipe = require("./recipeHandler")

module.exports = function (app) {
    app.get("/recipe/:id", recipe.getById);
    app.get("/recipe", recipe.getAll);
    app.post("/recipe", recipe.create);
    app.put("/recipe/:id", recipe.update);
    app.delete("/recipe/:id", recipe.delete);
};