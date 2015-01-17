/**
 * Created by Eric on 14/01/2015.
 */

var Comment = require("../models/comment.js");
var error = require("./errorHandler");

exports.getByRecipe = function (req, res) {
    Comment.find({ recipe : req.body.recipe }, function(err, comments) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (comments.length == 0) {
            return res.status(204).end();
        }
        return res.status(200).json(comments);
    }).sort({ date : -1});
};

exports.getByCreator = function(req, res) {
    Comment.find({ creator : req.body.creator }, function(err, comments) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (comments.length == 0) {
            return res.status(204).end();
        }
        return res.status(200).json(comments);
    }).sort({ date: -1 });
};

exports.create = function (req, res) {
    var com = new Comment();

    com.date = new Date();
    com.creator = req.body.creator;
    com.recipe = req.body.recipe;
    com.description = req.body.description;

    com.save(function(err) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        res.status(201).json(com);
    });
};