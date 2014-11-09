/**
 * Created by Eric on 28/10/2014.
 */
var express = require("express");
var https = require("https");
var fs = require("fs");
var cookieParser = require("cookie-parser");
var session = require("express-session");
var app = express();
var passport = require("passport");
//module.exports = app;

var mongoose = require("mongoose");
//CONNECT TO MONGODB MODULUS WORKING IN LOCAL
//mongoose.connect("mongodb://superphung:superphung@novus.modulusmongo.net:27017/y7dysOta");

//CONNECT TO MONGODB IN SERVER WORKING IN SERVER OR IN LOCAL
mongoose.connect("mongodb://localhost:27017/");

//CONNECT TO MONGODB IN SERVER WORKING IN LOCAL
//mongoose.connect("mongodb://54.64.212.101:27017/");

var options = {
    key: fs.readFileSync("server_key.pem"),
    cert: fs.readFileSync("server_cert.pem")
};

var User = require("./models/user.js");

function main () {

    app.use(express.static(__dirname + "/public"));
    app.use(cookieParser());
    app.use(session({ secret : "loliblastobar" }));
    app.use(passport.initialize());
    app.use(passport.session());

    require("./config")(app);
    require("./config/passportAuthentication")(passport);

    app.get("/", function(req, res) {
       //res.end("Welcome to the dashboard" + " " + req.protocol);
        res.render("fb.ejs");
    });

    app.get("/owi_ca_marche", function(req, res, next) {
       res.end("LOLIBLASTORBAR");
    });

    //TEST GROS CRADE : ROUTE FACEBOOK
    app.get("/account", ensureAuthenticated, function(req, res) {
        //User.findById(req.session.passport.user, function(err, user) {
          //  if (err)
            //    console.log(err);
           // else
                res.render("account.ejs");
        //});
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
    //TEST END

    require("./routes")(app);

    https.createServer(options, app).listen(app.get("port"));
}

main();