/**
 * Created by Eric on 31/10/2014.
 */

var User = require("../models/user.js");
var Ingredient = require("../models/ingredient.js");
var Recipe = require("../models/recipe.js");
var sjcl = require("sjcl");
var error = require("./errorHandler");

exports.getById = function(req, res) {
    User.findById(req.params.id, function(err, user){
        if (err) {
            error.logError(req, res, err);
            return (res.status(500).send(err));
        }
        /*for (int i=0; i < user.friends.length; i++)
        {

        }*/
        //res.json(user);
        res.status(200).json(user);
        //res.render("user.ejs", {user: user});
    });
};

exports.getAll = function(req, res) {
    //if(req.session && req.session.authenticated) {
        User.find(function (err, users) {
            if (err) {
                error.logError(req, res, err);
                return (res.status(500).send(err));
            }
            res.status(200).json(users);
        });
    /*}
    else {
        res.redirect("/login");
    }*/

};

exports.getLikesRecipes = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err) {
            error.logError(req, res, err);
            return (res.status(500).send(err));
        }
        Recipe.find({ _id : { $in : user.likes }}, function(err, lrecipes) {
            if (err) {
                error.logError(req, res, err);
                return (res.status(500).send(err));
            }
            if (lrecipes.length == 0)
                return (res.status(204).end());
            res.status(200).json(lrecipes);
        });
    });
};

exports.getDislikesRecipes = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err) {
            error.logError(req, res, err);
            return (res.status(500).send(err));
        }
        Recipe.find({ _id : { $in : user.dislikes }}, function(err, drecipes) {
            if (err) {
                return (res.status(500).send(err));
            }
            if (drecipes.length == 0)
                return (res.status(204).end());
            res.status(200).json(drecipes);
        });
    });
};

exports.getRestrictedFood = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err)
            return (res.send(err));
        Ingredient.find({ _id : { $in : user.restrictedFood }}, function(err, rfood) {
            if (err)
                return (res.send(err));
            res.json(rfood);
        });
        //res.json(user.restrictedFood);
    });
};

exports.getFavoriteFood = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err)
            return (res.send(err));
        Ingredient.find({ _id : { $in : user.favoriteFood }}, function(err, ffood) {
            if (err)
                return (res.send(err));
            res.json(ffood);
        });
        //res.json(user.favoriteFood);
    });
};

exports.getBadFood = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err)
            return (res.send(err));
        Ingredient.find({ _id : { $in : user.badFood }}, function(err, bfood) {
            if (err)
                return (res.send(err));
            res.json(bfood);
        });
        //res.json(user.badFood);
    });
};

exports.create = function(req, res) {
    //test si user already exists
    User.findOne({ username: req.body.username }, function(err, user) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        else if (user) {
            //console.log("USER EXIST");
            return res.status(409).send("user exist");
        }
        else {
            //console.log("toto");
            var _user = new User();
            _user.username = req.body.username;
            //_user.pass = sjcl.encrypt("pass", req.body.pass);
            _user.email = req.body.email;
            if (req.body.followings) _user.followings = req.body.followings;
            if (req.body.likes) _user.likes = req.body.likes;
            if (req.body.dislikes) _user.dislikes = req.body.dislikes;
            if (req.body.favoriteFood) _user.favoriteFood = req.body.favoriteFood;
            if (req.body.restrictedFood) _user.restrictedFood = req.body.restrictedFood;
            if (req.body.badFood) _user.badFood = req.body.badFood;

            _user.save(function(err) {
                if (err) {
                    error.logError(req, res, err);
                    return res.status(500).end(err);
                }
                res.status(201).json(_user);
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
        user.favoriteFood = req.body.favoriteFood;
        user.restrictedFood = req.body.restrictedFood;
        user.badFood = req.body.badFood;
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
            return (res.status(500).send(err));
        }
        res.status(200).json({ message: "Successfully deleted" });
    });
};