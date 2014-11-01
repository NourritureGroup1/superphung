/**
 * Created by Eric on 31/10/2014.
 */

var User = require("../models/user.js");
var sjcl = require("sjcl");

exports.getById = function(req, res) {
    User.findById(req.params.id, function(err, user){
        if (err)
            res.send(err);
        /*for (int i=0; i < user.friends.length; i++)
        {

        }*/
        //res.json(user);
        res.render("user.ejs", {user: user});
    });
};

exports.getAll = function(req, res) {
    if(req.session && req.session.authenticated) {
        User.find(function (err, users) {
            if (err)
                res.send(err);
            res.json(users);
        });
    }
    else
        res.redirect("/login");
};

exports.create = function(req, res) {
    var user = new User();
    user.username = req.body.username;
    user.pass = sjcl.encrypt("pass", req.body.pass);
    user.email = req.body.email;
    // test si user already exists
    /*User.find({"username": user.username}, function(err, users) {
        if (err)
            res.send(err);
        if (users)
        {
            res.json({ message: "user already exists"});
        }
    });*/
    user.save(function(err) {
        if (err)
            res.send(err);
        res.json({ message: "user created" });
    });
};

exports.update = function(req, res) {
    User.findById(req.params.id, function(err, user){
        if (err)
            res.send(err);

        user.email = req.body.email;
        user.friends = req.body.friends;
        user.likes = req.body.likes;
        user.dislikes = req.body.dislikes;
        user.save(function(err) {
            if (err)
                res.send(err);
            res.json({ message: "user updated"})
        });
    });
}