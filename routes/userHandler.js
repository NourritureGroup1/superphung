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
        if (user == null)
            return res.status(204).end();
        res.status(200).json(user);
    });
};

exports.getAll = function(req, res) {
    //if(req.session && req.session.authenticated) {
        User.find(function (err, users) {
            if (err) {
                error.logError(req, res, err);
                return (res.status(500).send(err));
            }
            if (users.length == 0)
                return res.status(204).end();
            res.status(200).json(users);
        });
    /*}
    else {
        res.redirect("/login");
    }*/

};

var getUserPreferences = function(req, res, Type, preferences) {
    Type.find({ _id : { $in : preferences }}, function(err, preferencesData) {
        if (err) {
            error.logError(req, res, err);
            return (res.status(500).send(err));
        }
        if (preferencesData.length == 0)
            return res.status(204).end();
        res.status(200).json(preferencesData);
    });
};


exports.getLikesRecipes = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err) { error.logError(req, res, err); return (res.status(500).send(err)); }
        return getUserPreferences(req, res, Recipe, user.likes);
    });
};

exports.getDislikesRecipes = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err) { error.logError(req, res, err); return (res.status(500).send(err)); }
        return getUserPreferences(req, res, Recipe, user.dislikes);
    });
};

exports.getRestrictedFood = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err) { error.logError(req, res, err); return (res.send(err)); }
        return getUserPreferences(req, res, Ingredient, user.restrictedFood);
    });
};

exports.getFavoriteFood = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err) { error.logError(res, req, err); return (res.status(500).send(err)); }
        return getUserPreferences(req, res, Ingredient, user.favoriteFood);
    });
};

exports.getBadFood = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err) { error.logError(req, res, err); return (res.status(500).send(err)); }
        return getUserPreferences(req, res, Ingredient, user.badFood);
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

exports.update = function(req, res) {
    User.findById(req.params.id, function(err, user) {
        if (err) {
            error.logError(req, res, err);
            return (res.status(500).end());
        }
        //cette condition sert a rien : si findByid ne trouve pas l'user il renvoit 500
        //verifier cote client que err=null et !res
        if (user == null)
            return res.status(204).end();

        user.email = req.body.email;
        user.followings = req.body.followings;
        user.likes = req.body.likes;
        user.dislikes = req.body.dislikes;
        user.favoriteFood = req.body.favoriteFood;
        user.restrictedFood = req.body.restrictedFood;
        user.badFood = req.body.badFood;
        user.save(function(err) {
            if (err) {
                error.logError(req, res, err);
                return (res.send(err));
            }
            res.status(200).json(user);
        });
    });
};

exports.delete = function(req, res, next) {
    User.remove({ _id : req.params.id }, function(err) {
        if (err) {
            error.logError(req, res, err);
            return (res.status(500).send(err));
        }
        res.status(200).json({ message: "Successfully deleted" });
    });
};