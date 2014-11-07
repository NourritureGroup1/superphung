/**
 * Created by Tristan on 31/10/2014.
 */
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var RecipeSchema = new Schema({
    name: String,
    creator: String,
    description: String,
    steps: Array,
    ingredients: Array, //ref to ingredient
    creationDate: String
});

module.exports = mongoose.model("Recipe", RecipeSchema);