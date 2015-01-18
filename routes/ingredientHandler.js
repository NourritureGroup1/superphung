/**
 * Created by Tristan on 31/10/2014.
 */

var Ingredient = require("../models/ingredient.js");
var error = require("./errorHandler");

exports.getById = function (req, res) {
    Ingredient.findById(req.params.id, function(err, ingredient){
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        res.status(200).json(ingredient);
    });
};

exports.getAll = function (req, res) {
    Ingredient.find(function(err, ingredients) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (ingredients.length == 0)
            return res.status(204).end();
        res.status(200).json(ingredients);
    });
};

exports.getByStrictName = function(req, res) {
    Ingredient.findOne({ name : req.params.name }, function(err, ingredient) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (ingredient == null)
            return (res.status(204).end());
        res.status(200).json(ingredient);
    });
};

exports.getByPartialName = function (req, res) {
    Ingredient.find({ name : { $regex: req.params.name, $options : "i" } }, function(err, ingredients) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (ingredients.length == 0)
            return res.status(204).end();
        res.status(200).json(ingredients);
    });
};

exports.create = function (req, res) {

    Ingredient.findOne({ name: req.body.name }, function(err, ingredient) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        else if (ingredient) {
            return res.status(409).send("ingredient exist");
        }
        else {
            var _ingredient = new Ingredient();

            _ingredient.name = req.body.name;
            _ingredient.description = req.body.description;
            _ingredient.category = stringToArray(req.body.category);
            _ingredient.nutrients = stringToArray(req.body.nutrients);
			try {
                _ingredient.imgUrl = "/uploads/" + req.files.file.name;
				}
				catch(err) {
				            _ingredient.imgUrl = "/uploads/" + req.body.imgUrl;
				}
         /*  if (req.body.imgUrl == undefined) {
                _ingredient.imgUrl = "/uploads/" + req.files.file.name;
            }*/

            _ingredient.save(function(err) {
                if (err) {
                    error.logError(req, res, err);
                    return res.status(500).send(err);
                }
                res.status(201).json(_ingredient);
            });
        }
    });
};

exports.update = function(req, res) {
    Ingredient.findById(req.params.id, function(err, ingredient) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        ingredient._id = req.body._id;
        ingredient.name = req.body.name;
        ingredient.description = req.body.description;
        ingredient.category = stringToArray(req.body.category);
        ingredient.nutrients = stringToArray(req.body.nutrients);
        ingredient.save(function(err) {
            if (err) {
                error.logError(req, res, err);
                return res.status(500).send(err);
            }
            res.status(200).json(ingredient);
        });
    });
};

exports.delete = function(req, res) {
    Ingredient.remove({ _id : req.params.id }, function(err) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        res.status(200).json({ message: "Successfully deleted" });
    });
};

exports.upload = function(req, res) {
    Ingredient.findById(req.body.idCreation, function(err, ingredient) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        ingredient.imgUrl = "/uploads/" + req.files.file.name;
        ingredient.save(function(err) {
            if (err) {
                error.logError(req, res, err);
                return res.status(500).send(err);
            }
        });
    });
};

function stringToArray(str) {
    if (typeof str === 'string')
        return str.split(',');
    return str;
}