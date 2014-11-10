/**
 * Created by Eric on 31/10/2014.
 */
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var UserSchema = new Schema({
    oauthID: Number,        //Facebook Id
    name: String,
    username: String,
    //pass: JSON,
    email: String,
    followings: Array,      //username
    likes: Array,           //recipes
    dislikes: Array,        //recipes
    favoriteFood: Array,    //ingredient
    restrictedFood: Array,  //ingredient
    badFood: Array          //ingredient
});

module.exports = mongoose.model("User", UserSchema);