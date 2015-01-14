/**
 * Created by Eric on 14/01/2015.
 */

var Moment = require("../models/moment.js");
var error = require("./errorHandler");

exports.getById = function (req, res) {
    Moment.findById(req.body.id, function(err, moment) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        res.status(200).json(moment);
    });
};

exports.getByCreator = function (req, res) {
    Moment.find({ creator : req.body.creator }, function(err, moments) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (moments.length == 0) {
            return res.status(204).end();
        }
        return res.status(200).json(moments);
    }).sort({ date : -1});
};

exports.getByArray = function(req, res) {
    var params = JSON.parse(req.body.friendId);
    Moment.find({ creator : params }, function(err, moments) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        if (moments.length == 0) {
            return res.status(204).end();
        }
        return res.status(200).json(moments);
    }).sort({ date: -1 });
};

exports.create = function (req, res) {
    var mom = new Moment();

    mom.date = new Date();
    mom.imgUrl = "/uploads/" + req.files.file.name;
    mom.creator = req.body.creator;
    mom.description = req.body.description;

    mom.save(function(err) {
        if (err) {
            error.logError(req, res, err);
            return res.status(500).send(err);
        }
        res.status(201).json(mom);
    });
};