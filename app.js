/**
 * Created by Eric on 28/10/2014.
 */
var express = require("express");
var https = require("https");
var fs = require("fs");
var formidable = require("formidable");
var sjcl = require("sjcl");
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
    require("./config")(app);

    app.get("/", function(req, res) {
       //res.end("Welcome to the dashboard" + " " + req.protocol);
        //res.render("index");
        res.sendFile(__dirname + "/public/index.html");
    });

    app.post("/data", function(req, res) {
        var form = new formidable.IncomingForm();

        form.parse(req, function(err, fields, files) {
            console.log(fields);
            var pass = sjcl.decrypt("pass", fields.pass);
            console.log(pass);
            res.end("va te faire enculer par des grosses vaches");

        });
    });

    require("./routes")(app);

    https.createServer(options, app).listen(app.get("port"));
}

main();