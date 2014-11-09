/**
 * Created by Tristan on 31/10/2014.
 */

var Ingredient = require("../models/ingredient.js");

exports.getById = function (req, res) {
    Ingredient.findById(req.params.id, function(err, ingredient){
        if (err)
            res.status(500).send(err);
        res.status(200);
        res.json(ingredient);
    });
};

exports.getAll = function (req, res) {
    Ingredient.find(function(err, ingredients) {
        if (err)
            res.send(err);
        res.json(ingredients);
        //res.render("ingredient.ejs", { ingredients : ingredients });
    });
};

exports.create = function (req, res) {

    Ingredient.findOne({ name: req.body.name }, function(err, ingredient) {
        if (err) {
            console.log("ERREUR CREATION INGREDIENT");
            res.send(err);
        }
        else if (ingredient) {
            console.log("INGREDIENT EXIST");
            res.send("ingredient exist deja");
        }
        else {
            console.log("INGREDIENT CREATION");
            var _ingredient = new Ingredient();

            _ingredient.name = req.body.name;
            _ingredient.description = req.body.description;
            _ingredient.category = req.body.category;
            _ingredient.nutrients = req.body.nutrients;

            _ingredient.save(function(err) {
                if (err)
                    res.send(err);
                res.json({ message: "ingredient created" });
            });
        }
    });
};

exports.getByPartialName = function (req, res) {
    Ingredient.find({ name: { $regex: req.params.name, $options: "i" } }, function(err, ingredients) {
        if (err)
            res.send(err);
        res.json(ingredients);
    });
};

exports.update = function(req, res) {
    Ingredient.findById(req.params.id, function(err, ingredient) {
        if (err)
            return (res.send(err));
        ingredient.description = req.body.description;
        ingredient.category = req.body.category;
        ingredient.nutrients = req.body.nutrients;
        ingredient.save(function(err) {
            if (err) {
                res.send(err);
            }
            console.log("INGREDIENT UPDATE");
            res.json({ message : "ingredient update" });
            res.end();
        });
    });
};

exports.delete = function(req, res) {
    Ingredient.remove({ _id : req.params.id }, function(err) {
        if (err)
            res.send(err);
        res.json({ message: "Successfully deleted" });
    });
};