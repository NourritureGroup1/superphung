/**
 * Created by Eric on 31/10/2014.
 */

var User = require("../models/user.js");

exports.getById = function(req, res) {
    User.findById(req.params.id, function(err, user){
        if (err)
            res.send(err);
        /*for (int i=0; i < user.friends.length; i++)
        {

        }*/
        //res.json(user);
        res.render("user.ejs", {user: user});
    });
};

exports.getAll = function(req, res) {
    User.find(function(err, users) {
        if (err)
            res.send(err);
        res.json(users);
    });
};

exports.create = function(req, res) {
    var user = new User();
    user.username = req.body.username;
    user.password = req.body.password;
    user.email = req.body.email;
    user.save(function(err) {
        if (err)
            res.send(err);
        res.json({ message: "user created" });
    });
};

exports.update = function(req, res) {
    User.findById(req.params.id, function(err, user){
        if (err)
            res.send(err);

        user.email = req.body.email;
        user.friends = req.body.friends;
        user.likes = req.body.likes;
        user.dislikes = req.body.dislikes;
        user.save(function(err) {
            if (err)
                res.send(err);
            res.json({ message: "user updated"})
        });
    });
}