/**
 * Created by Eric on 10/11/2014.
 */
var passport = require("passport");
//var User = require("../models/user.js");

module.exports = function(app) {
    app.get("/account", ensureAuthenticated, function(req, res) {
        //User.findById(req.session.passport.user, function(err, user) {
          //if (err)
          //    console.log(err);
         // else
              //res.render("account.ejs");
        res.redirect("/");
        //res.sendFile(__dirname + "/../public/app.html");
       // });
    });

    app.post("/login", function(req, res, next) {
        passport.authenticate("local-login", function(err, user, info) {
            if (err)
                return next(err);
            if (!user)
                return res.status(400).send("Fail, try again :)");
            req.logIn(user, function(err) {
               if (err)
                   return next(err);
                return res.status(200).json(user);
            });
        })(req, res, next);
    });

    app.post("/signup", function(req, res, next) {
        passport.authenticate("local-signup", function(err, user, info) {
            if (err)
                return next(err);
            if (!user)
                return res.status(409).send("user exist");

            req.logIn(user, function(err) {
                if (err)
                    return next(err);
                return res.status(201).json(user);
            });
        })(req, res, next);
    });
    /*app.post('/signup', passport.authenticate('local-signup', {
        successRedirect : '/account', // redirect to the secure profile section
        failureRedirect : '/' // redirect back to the signup page if there is an error
    }));*/

    app.get("/auth/facebook", passport.authenticate("facebook", { scope : "email" }));
    app.get("/auth/facebook/callback",
        passport.authenticate("facebook", {
            successRedirect : "/account",
            failureRedirect : "/"
        }));

    app.get("/auth/google", passport.authenticate("google", { scope : ["profile", "email"] }));
    app.get("/auth/google/callback",
        passport.authenticate("google", {
            successRedirect : "/account",
            failureRedirect : "/"
        }));

    app.get("/logout", function(req, res) {
        req.logout();
        res.redirect("/");
    });

    app.get("/session", isLoggedIn, function(req, res) {
        console.log(req.user);
        res.status(200).json(req.user);
    });

    function isLoggedIn(req, res, next) {
        if (req.isAuthenticated()) {
            next();
        } else {
            res.status(400).send({msg : "Please login to access this information"});
        }
    }

    function ensureAuthenticated(req, res, next) {
        if (req.isAuthenticated())
            return next();
        res.redirect("/");
    }
};