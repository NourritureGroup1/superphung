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

    app.get("/auth/facebook",
        passport.authenticate("facebook", { scope : "email" }),
        function(req,res){});
    app.get("/auth/facebook/callback",
        passport.authenticate("facebook", { failureRedirect : "#/login"}),
        function(req, res) {
            res.redirect("/account");
        });
    app.get("/logout", function(req, res) {
        req.logout();
        res.redirect("/");
    });

    app.get("/session", isLoggedIn, function(req, res) {
        console.log(req.user);
        res.status(200).json(req.user);
        /*res.send({
            //loginStatus: true,
            user: req.user
        });*/
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
        res.redirect("#/login");
    }
};