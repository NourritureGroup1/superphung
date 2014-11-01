/**
 * Created by Eric on 01/11/2014.
 */

var formidable = require("formidable");
var sjcl = require("sjcl");
var User = require("../models/user.js");
var path = require("path");

exports.connect = function(req, res, next) {
    //if (req.session && req.session.authenticated)
      //  return next();
    //else
        res.sendFile("login.html", { root: path.join(__dirname, "../public") });
};

exports.authentification = function(req, res, next) {
    var form = new formidable.IncomingForm();

    form.parse(req, function(err, fields, files) {
        //console.log(fields);
        //var pass = sjcl.decrypt("pass", fields.pass);
        //console.log(pass);
        //res.end("login end");

        //var pass = sjcl
        if (req.session && req.session.authenticated)
            return (next(res.redirect("/owi_ca_marche")));
        User.findOne({ username: fields.username }, function(error, user) {
            if (error)
                return next(error);
            if (!user || (sjcl.decrypt("pass", fields.pass) != sjcl.decrypt("pass", user.pass)))
                return next(res.redirect("/login"));
            req.session.user = user;
            req.session.authenticated = true;
            res.redirect("/owi_ca_marche");
        });
    });
};