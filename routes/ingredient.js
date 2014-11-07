/**
 * Created by Tristan on 31/10/2014.
 */

var ingredient = require("./ingredientHandler")

module.exports = function (app) {
    app.get("/ingredient/:id", ingredient.getById);
    app.get("/ingredient", ingredient.getAll);
    app.post("/ingredient", ingredient.create);
    app.get("/ingredient/getIngredient/:name", ingredient.getByPartialName);
    app.put("/ingredient/:id", ingredient.update);
    app.delete("/ingredient/:id", ingredient.delete);
};