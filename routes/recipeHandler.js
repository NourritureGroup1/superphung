/**
 * Created by Tristan on 31/10/2014.
 */

var Ingredient = require("../models/ingredient.js");
var Recipe = require("../models/recipe.js");
var User = require("../models/user.js");
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
        _recipe.imgUrl = "";
        //_recipe.imgUrl = req.files.name;

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
        recipe._id = req.body._id;
        recipe.name = req.body.name;
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

exports.upload = function (req, res) {
    console.log("post - upload req.body : ", req.body);
    console.log("post - file upload : " + JSON.stringify(req.files));
    console.log("id recipe : " + req.body.idCreation);
    console.log("id user : " + req.body.idUser);
    Recipe.findById(req.body.idCreation, function(err, recipe) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        recipe.imgUrl = "/uploads/" + req.files.file.name;
        recipe.save(function(err) {
            if (err) {
                error.logError(req, res, err);
                return res.status(500).send(err);
            }
        });
    });
    User.findById(req.body.idUser, function(err, user) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        user.recipesCreated.push(req.body.idCreation);
        user.save(function(err) {
            if (err) {
                error.logError(req, res, err);
                return res.status(500).send(err);
            }
        });
    });
    //res.status(200).json(req.files);

};