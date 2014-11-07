/**
 * Created by Eric on 07/11/2014.
 */

var fs = require("fs");
var moment = require("moment");

exports.logError = function(req, res, err) {
    var ip = req.headers["x-forwarded-for"] || req.connection.remoteAddress;

    fs.appendFile("log/log", "date=" + moment().format("MM/DD/YYYY h:mm:ss a") + ", ip=" + ip + ", method=" + req.method + ", err=" + err + "\n", function(err) {
        if (err)
            console.log("erreur appendFile");
    });
    console.log("date=" + moment().format("MM/DD/YYYY h:mm:ss a") + ", ip=" + ip + ", method=" + req.method + ", err=" + err);
};