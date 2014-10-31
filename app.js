/**
 * Created by Eric on 28/10/2014.
 */
var express = require("express");
var https = require("https");
var fs = require("fs");
var app = express();
//module.exports = app;

var options = {
    key: fs.readFileSync("server_key.pem"),
    cert: fs.readFileSync("server_cert.pem")
};

function main () {

    require("./config")(app);

    app.get("/", function(req, res) {
       res.end("Welcome to the dashboard" + " " + req.protocol);
    });

    require("./routes")(app);

    https.createServer(options, app).listen(443);
}

main();