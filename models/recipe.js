/**
 * Created by Tristan on 31/10/2014.
 */
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var RecipeSchema = new Schema({
    name: String,
    description: String,
    creator: String,
    steps: Array,
    ingredients: Array,
    creationDate: String
});

module.exports = mongoose.model("Recipe", RecipeSchema);