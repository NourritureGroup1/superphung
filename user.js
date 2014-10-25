/**
 * Created by Eric on 25/10/2014.
 */

var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var UserSchema = new Schema({
    name: String
});

module.exports = mongoose.model("User", UserSchema);