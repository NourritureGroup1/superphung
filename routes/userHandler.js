/**
 * Created by Eric on 31/10/2014.
 */

var User = require("../models/user.js");
var sjcl = require("sjcl");
var error = require("./errorHandler");

exports.getById = function(req, res) {
    User.findById(req.params.id, function(err, user){
        if (err) {
            res.status(500).send(err);
        }
        /*for (int i=0; i < user.friends.length; i++)
        {

        }*/
        //res.json(user);
        res.status(200);
        res.render("user.ejs", {user: user});
    });
};

exports.getAll = function(req, res) {
    //if(req.session && req.session.authenticated) {
        User.find(function (err, users) {
            if (err) {
                res.send(err);
            }
            res.json(users);
        });
    /*}
    else {
        res.redirect("/login");
    }*/

};

exports.getRestrictedFood = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err)
            return (res.send(err));
        res.json(user.restrictedFood);
    });
};

exports.getFavoriteFood = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err)
            return (res.send(err));
        res.json(user.favoriteFood);
    });
};

exports.create = function(req, res, next) {
    //test si user already exists
    User.findOne({ username: req.body.username }, function(err, user) {
        if (err) {
            console.log("erreur create user");
            //return (res.send(err));
            res.send(err);
        }
        else if (user) {
            console.log("USER EXIST");
            res.send("user exist deja");
        }
        else {
            console.log("toto");
            var _user = new User();
            _user.username = req.body.username;
            _user.pass = sjcl.encrypt("pass", req.body.pass);
            _user.email = req.body.email;
            _user.followings = req.body.followings;
            _user.likes = req.body.likes;
            _user.dislikes = req.body.dislikes;
            _user.favoriteFood = req.body.favoriteFood;
            _user.restrictedFood = req.body.restrictedFood;

            _user.save(function(err) {
                if (err) {
                    return (res.status(500).end(err));
                }
                res.status(201).json({ message: "user created" });
            });
        }
    });
};

exports.update = function(req, res, next) {
    User.findById(req.params.id, function(err, user){
        if (err) {
            error.logError(req, res, err);
            return (res.status(500).end());
        }

        if (req.body.email) user.email = req.body.email;
        if (req.body.followings) user.followings = req.body.followings;
        if (req.body.likes) user.likes = req.body.likes;
        if (req.body.dislikes) user.dislikes = req.body.dislikes;
        if (req.body.favoriteFood) user.favoriteFood = req.body.favoriteFood;
        if (req.body.restrictedFood) user.restrictedFood = req.body.restrictedFood;
        user.save(function(err) {
            if (err) {
                return (res.send(err));
            }
            console.log("USER UPDATE");
            //res.json({ message: "user updated"});
            //res.end();
            res.status(200).json(user);
        });
    });
};

exports.delete = function(req, res, next) {
    User.remove({ _id : req.params.id }, function(err) {
        if (err) {
            return (res.send(err));
        }
        res.json({ message: "Successfully deleted" });
    });
};