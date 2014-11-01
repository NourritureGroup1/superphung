/**
 * Created by Eric on 28/10/2014.
 */
var express = require("express");
var https = require("https");
var fs = require("fs");
var cookieParser = require("cookie-parser");
var session = require("express-session");
var app = express();

//module.exports = app;

var mongoose = require("mongoose");
mongoose.connect("mongodb://superphung:superphung@novus.modulusmongo.net:27017/y7dysOta");

var options = {
    key: fs.readFileSync("server_key.pem"),
    cert: fs.readFileSync("server_cert.pem")
};

function main () {

    app.use(express.static(__dirname + "/public"));
    app.use(cookieParser());
    app.use(session({ secret : "loliblastobar" }));
    require("./config")(app);

    app.get("/", function(req, res) {
       res.end("Welcome to the dashboard" + " " + req.protocol);
    });

    app.get("/owi_ca_marche", function(req, res, next) {
       res.end("LOLIBLASTORBAR");
    });

    require("./routes")(app);

    https.createServer(options, app).listen(app.get("port"));
}

main();