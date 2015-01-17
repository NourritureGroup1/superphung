/**
 * Created by Eric on 14/01/2015.
 */
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var MomentSchema = new Schema({
    date: Date,
    imgUrl: String,
    creator: String,
    description: String
});

module.exports = mongoose.model("Moment", MomentSchema);