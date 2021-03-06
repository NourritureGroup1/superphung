/**
 * Created by Tristan on 31/10/2014.
 */

var recipe = require("./recipeHandler");

module.exports = function (app) {
    app.get("/recipe/:id", recipe.getById);
    app.get("/recipe", recipe.getAll);
    app.get("/recipe/:id/ingredient", recipe.getIngredients);
    app.get("/recipe/:name/:creator", recipe.getByCreator);
    app.post("/recipe", recipe.create);
    app.put("/recipe/:id", recipe.update);
    app.delete("/recipe/:id", recipe.delete);
    app.post("/recipeDeleteAndroid", recipe.deleteAndroid);

    app.post("/uploadsRecipes", recipe.upload);
};