/**
 * Created by Tristan on 31/10/2014.
 */

var Ingredient = require("../models/ingredient.js");
var Recipe = require("../models/recipe.js");

exports.getById = function (req, res) {
    Recipe.findById(req.params.id, function(err, recipe){
        if (err)
            res.send(err);
        res.json(recipe);
    });
};

exports.getAll = function (req, res) {
    Recipe.find(function(err, recipes) {
        if (err)
            return (res.send(err));
        res.json(recipes);
    });
};

exports.getIngredients = function(req, res) {
    Recipe.findById(req.params.id, function(err, recipe) {
        if (err)
            return (res.send(err));
        Ingredient.find({ _id : { $in : recipe.ingredients }}, function(err, ingredients) {
            if (err)
                return (res.send(err));
            res.json(ingredients);
        });
    });
};

exports.create = function (req, res, next) {

    Recipe.find({ name : req.body.name }, function(err, recipes) {
        if (err) {
            console.log("erreur create recipe");
            return (res.send(err));
        }

        var res = 0;
        recipes.forEach(function(recipe) {
            if (recipe.creator === req.body.creator) {
                console.log("RECIPE EXIST - 1 USER CANNOT CREATE A RECIPE TWICE"); res = 1;
            }
        });
        if (res == 1)
            return (res.status(500).end());

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
                return (res.send(err));
            res.json(_recipe);
        });
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