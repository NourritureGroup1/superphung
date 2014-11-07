/**
 * Created by Tristan on 31/10/2014.
 */

var Recipe = require("../models/recipe.js");

exports.getById = function (req, res) {
    Recipe.findById(req.params.id, function(err, recipe){
        if (err)
            res.send(err);
        res.json(recipe);
    });
};

exports.create = function (req, res) {
    Recipe.findOne({ creator : req.body.creator }, function(err, recipe) {
        if (err) {
            console.log("erreur create recipe");
            res.send(err);
        }
        else if (recipe) {
            console.log("RECIPE EXIST - 2 USERS CANNOT CREATE THE SAME RECIPE");
            res.send("recipe exist deja");
        }
        else {
            console.log("RECIPE CREATION");
            var _recipe = new Recipe();

            _recipe.name = req.body.name;
            _recipe.creator = req.body.creator;
            _recipe.description = req.body.description;
            _recipe.steps = req.body.steps;
            _recipe.ingredients = req.body.ingredients;
            _recipe.creationDate = req.body.creationDate;

            _recipe.save(function(err) {
                if (err)
                    res.send(err);
                res.json({ message : "recipe created"});
            })
        }

    });
};

exports.getAll = function (req, res) {
    Recipe.find(function(err, recipes) {
        if (err)
            res.send(err);
        res.json(recipes);
    });
};

exports.update = function (req, res) {
    Recipe.findById({ _id : req.params.id }, function (err, recipe) {
        if (err)
            return (res.send(err));
        recipe.description = req.body.description;
        recipe.steps = req.body.steps;
        recipe.ingredients = req.body.ingredients;
        recipe.save(function(err) {
            if (err)
                return (res.send(err));
            console.log("RECIPE UPDATE");
            res.json({ message : "recipe update" });
            res.end();
        })
    });
};

exports.delete = function (req, res) {
    Recipe.remove({ _id : req.params.id }, function(err) {
        if (err)
            res.send(err);
        res.json({ message : "Successfully deleted" });
    });
};