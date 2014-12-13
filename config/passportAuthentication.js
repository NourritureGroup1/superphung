/**
 * Created by Eric on 10/11/2014.
 */

//var passport = require("passport");
var LocalStrategy = require("passport-local").Strategy;
var FacebookStrategy = require("passport-facebook").Strategy;
var GoogleStrategy = require("passport-google-oauth").OAuth2Strategy;
var User = require("../models/user.js");
var config = require("./oauth.js");

module.exports = function(passport) {

    passport.serializeUser(function (user, done) {
        done(null, user._id);
    });

    passport.deserializeUser(function (id, done) {
        User.findById(id, function (err, user) {
            if (!err)
                done(null, user);
            else
                done(err, null);
        });
    });

   passport.use("local-signup", new LocalStrategy({
        usernameField: "email",
        passwordField: "password",
        passReqToCallback: true
    }, configLocalSignup));

    //passport.use("local-signup", new LocalStrategy(configLocalSignup));

    passport.use("local-login", new LocalStrategy({
        usernameField: "email",
        passwordField: "password",
        passReqToCallback: true
    }, configLocalLogin));

    passport.use(new FacebookStrategy({
        clientID: config.facebook.clientID,
        clientSecret: config.facebook.clientSecret,
        callbackURL: config.facebook.callbackURL
    }, configStrategy));

    passport.use(new GoogleStrategy({
        clientID: config.google.clientID,
        clientSecret: config.google.clientSecret,
        callbackURL: config.google.callbackURL
    }, configStrategy));

    function configStrategy(token, refreshToken, profile, done) {
        User.findOne({ oauthID : profile.id }, function (err, user) {
            if (err)
                return done(err);
            if (!err && user != null)
                return done(null, user);
            else {
                var _user = new User({
                    oauthID: profile.id,
                    name: profile.displayName,
                    email: profile.emails[0].value,
                    role: "consumer"
                });
                _user.save(function(err) {
                    if (err)
                        console.log(err);
                    else
                        return done(null, _user);
                });
            }
        });
    }

    function configLocalSignup(req, email, password, done) {
        User.findOne({ email : email }, function(err, user) {
            if (err) {
                return done(err);
            }
            if (user) {
                return done(null, false);
            } else {
                var _user = new User();

                _user.email = email;
                _user.name = req.body.name;
                _user.password = _user.generateHash(password);
                _user.role = "consumer";

                _user.save(function(err) {
                    if (err)
                        throw err;
                    return done(null, _user);
                });
            }
        });
    }

    function configLocalLogin(req, email, password, done) {
        User.findOne({ email : email }, function(err, user) {
            if (err)
                return done(err);
            if (!user)
                return done(null, false);
            if (!user.validPassword(password))
                return done(null, false);
            return done(null, user);
        });
    }
};