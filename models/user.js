/**
 * Created by Eric on 31/10/2014.
 */
var mongoose = require("mongoose");
var bcrypt = require("bcrypt-nodejs");
var Schema = mongoose.Schema;

var UserSchema = new Schema({
    oauthID: Number,        //Facebook Id
    name: String,
    username: String,
    //pass: JSON,
    email: String,
    password: String,
    followings: Array,      //username
    likes: Array,           //recipes
    dislikes: Array,        //recipes
    favoriteFood: Array,    //ingredient
    restrictedFood: Array,  //ingredient
    badFood: Array,          //ingredient
    recipesCreated: Array
});

UserSchema.methods.generateHash = generateHash;
UserSchema.methods.validPassword = validPassword;

function generateHash(password) {
    return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
}

function validPassword(password) {
    return bcrypt.compareSync(password, this.local.password);
}

module.exports = mongoose.model("User", UserSchema);