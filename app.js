/**
 * Created by Eric on 28/10/2014.
 */
var express = require("express");
var app = express();
var https = require("https");
var fs = require("fs");
var passport = require("passport");

var cookieParser = require("cookie-parser");
var bodyParser = require("body-parser");
var multer = require("multer");
var session = require("express-session");



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
    app.use(bodyParser.urlencoded({ extended : false }));
    app.use(bodyParser.json());
    app.use(multer({
        dest : "./public/uploads/",
        rename : function(fieldname, filename) {
            return filename.replace(/\W+/g, "-").toLocaleLowerCase() + Date.now();
        }}));
    app.use(session({ secret : "loliblastobar" }));
    app.use(passport.initialize());
    app.use(passport.session());

    require("./config")(app);
    require("./config/passportAuthentication")(passport);

    app.get("/", function(req, res) {
       //res.end("Welcome to the dashboard" + " " + req.protocol);
        res.render("fb.ejs");
    });

    require("./routes")(app);

    require("./config/end_points")(app._router.stack);

    app.get("/api", function(req, res) {
        res.render("end_points", { routes : app._router.stack });
    });

    https.createServer(options, app).listen(app.get("port"));
}

main();

module.exports = app;