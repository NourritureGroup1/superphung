/**
 * Created by Tristan on 31/10/2014.
 */
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var IngredientSchema = new Schema({
    name: String,
    description: String,
    category: Array,
    nutrients: Array
});

module.exports = mongoose.model("Ingredient", IngredientSchema);