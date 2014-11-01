/**
 * Created by Eric on 31/10/2014.
 */
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var UserSchema = new Schema({
    username: String,
    pass: JSON,
    email: String,
    followings: Array,
    likes: Array,
    dislikes: Array
});

module.exports = mongoose.model("User", UserSchema);