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
              res.render("account.ejs");
       // });
    });

    app.get("/auth/facebook",
        passport.authenticate("facebook"),
        function(req,res){});
    app.get("/auth/facebook/callback",
        passport.authenticate("facebook", { failureRedirect : "/"}),
        function(req, res) {
            res.redirect("/account");
        });
    app.get("/logout", function(req, res) {
        req.logout();
        res.redirect("/");
    });

    function ensureAuthenticated(req, res, next) {
        if (req.isAuthenticated())
            return next();
        res.redirect("/");
    }
};