/**
 * Created by Eric on 14/01/2015.
 */
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var CommentSchema = new Schema({
    date: Date,
    creator: String,
    recipe: String,
    description: String
});

module.exports = mongoose.model("Comment", CommentSchema);