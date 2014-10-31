/**
 * Created by Tristan on 31/10/2014.
 */

var Recipe = require("../models/recipe.js");

exports.getById = function (req, res) {
    Recipe.findById(req.params.id, function(err, user){
        if (err)
            res.send(err);
        res.json(user);
    });
};

exports.create = function (req, res) {
    var recipe = new Recipe();

    recipe.name = res.body.name;
    recipe.description = res.body.description;
    recipe.creator = res.body.creator;
    recipe.steps = res.body.steps;
    recipe.save(function(err) {
        if (err)
            res.send(err);
        res.json({ message: "recipe created" });
    });
};

exports.getAll = function (req, res) {
    Recipe.find(function(err, users) {
        if (err)
            res.send(err);
        res.json(users);
    });
};