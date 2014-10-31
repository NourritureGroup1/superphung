/**
 * Created by Tristan on 31/10/2014.
 */

exports.getById = function (req, res) {
    Ingredient.findById(req.params.id, function(err, user){
        if (err)
            res.send(err);
        res.json(user);
    });
};

exports.create = function (req, res) {
    var ingredient = new Ingredient();

    ingredient.name = res.body.name;
    ingredient.description = res.body.description;
    ingredient.category = res.body.category;
    ingredient.save(function(err) {
        if (err)
            res.send(err);
        res.json({ message: "ingredient created" });
    });
};

exports.getAll = function (req, res) {
    Ingredient.find(function(err, users) {
        if (err)
            res.send(err);
        res.json(users);
    });
};