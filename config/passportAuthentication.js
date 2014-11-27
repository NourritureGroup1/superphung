/**
 * Created by Eric on 10/11/2014.
 */

//var passport = require("passport");
var FacebookStrategy = require("passport-facebook").Strategy;
var User = require("../models/user.js");
var config = require("./oauth.js");

module.exports = function(passport) {

    passport.serializeUser(function(user, done) {
        done(null, user._id);
    });

    passport.deserializeUser(function(id, done) {
       User.findById(id, function(err, user) {
           if (!err)
               done(null, user);
           else
               done(err, null);
       });
    });

    passport.use(new FacebookStrategy({
        clientID : config.facebook.clientID,
        clientSecret : config.facebook.clientSecret,
        callbackURL : config.facebook.callbackURL
    },
    function(token, refreshToken, profile, done) {
       User.findOne({ oauthID : profile.id }, function(err, user) {
           if (err)
               return done(err);
           if (!err && user != null)
               return done(null, user);
           else {
               var _user = new User({
                   oauthID : profile.id,
                   name : profile.displayName,
                   email : profile.emails[0].value
               });
               _user.save(function(err) {
                   if (err)
                       console.log(err);
                   else
                       done(null, user);
               });
           }
       });

    }));
};