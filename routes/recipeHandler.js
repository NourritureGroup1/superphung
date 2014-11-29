/**
 * Created by Tristan on 31/10/2014.
 */

var Ingredient = require("../models/ingredient.js");
var Recipe = require("../models/recipe.js");
var error = require("./errorHandler");

exports.getById = function (req, res) {
    Recipe.findById(req.params.id, function(err, recipe){
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        res.status(200).json(recipe);
    });
};

exports.getAll = function (req, res) {
    Recipe.find(function(err, recipes) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (recipes.length == 0)
            return res.status(204).end();
        res.status(200).json(recipes);
    });
};

exports.getIngredients = function(req, res) {
    Recipe.findById(req.params.id, function(err, recipe) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        Ingredient.find({ _id : { $in : recipe.ingredients }}, function(err, ingredients) {
            if (err) {
                error.logError(req, res, err);
                return res.status(500).send(err);
            }
            if (ingredients.length == 0)
                return res.status(204).end();
            res.status(200).json(ingredients);
        });
    });
};

exports.getByCreator = function(req, res) {
    Recipe.findOne({ name : req.params.name, creator : req.params.creator }, function(err, recipe) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (recipe == null)
            return res.status(204).end();
        res.status(200).json(recipe);
    });
};

exports.create = function (req, res) {

    Recipe.find({ name : req.body.name }, function(err, recipes) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }

        var ret = 0;
        recipes.forEach(function(recipe) {
            if (recipe.creator === req.body.creator) {
                ret = 1;
            }
        });

        if (ret == 1)
            return res.status(409).send("recipe exist");

        var _recipe = new Recipe();

        _recipe.name = req.body.name;
        _recipe.creator = req.body.creator;
        _recipe.description = req.body.description;
        _recipe.steps = req.body.steps;
        _recipe.ingredients = req.body.ingredients;
        _recipe.creationDate = req.body.creationDate;
        _recipe.imgUrl = req.files.name;

        _recipe.save(function(err) {
            if (err) {
                error.logError(req, res, err);
                return res.status(500).send(err);
            }
            res.status(201).json(_recipe);
        });
    });
};

exports.update = function (req, res) {
    Recipe.findById(req.params.id, function (err, recipe) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        recipe.description = req.body.description;
        recipe.steps = req.body.steps;
        recipe.ingredients = req.body.ingredients;
        recipe.creationDate = req.body.creationDate;
        recipe.save(function(err) {
            if (err) {
                error.logError(req, res, err);
                return res.status(500).send(err);
            }
            res.status(200).json(recipe);
        })
    });
};

exports.delete = function (req, res) {
    Recipe.remove({ _id : req.params.id }, function(err) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        res.status(200).json({ message : "Successfully deleted" });
    });
};