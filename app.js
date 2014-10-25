var express = require("express");
var app = express();
var bodyParser = require("body-parser");

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(bodyParser.json({ type: "application/vnd.api+json" }));

var port = process.env.PORT || 8888;

var mongoose = require("mongoose");
mongoose.connect("mongodb://superphung :superphung@novus.modulusmongo.net:27017/y7dysOta");

var User = require("./user");

//ROUTE FOR API
var router = express.Router();

router.use(function(req, res, next) {
	//console.log(req.method, req.url);
	next();
});

/*router.get("/", function(req, res) {
	res.json({ message: "welcome to our api !" });
});*/

router.route("/users")
	.post(function(req, res) {
		var user = new User();
		user.name = req.body.name;
		user.toto = req.body.toto;
		user.save(function(err) {
			if (err)
				res.send(err);
			res.json({ message: "user created"});
		});
	})
	.get(function(req, res) {
		User.find(function(err, users) {
			if (err)
				res.send(err);
			res.json(users);
		});
	});

router.route("/users/:user_id")
	.get(function(req, res) {
		User.findById(req.params.user_id, function(err, user) {
			if (err)
				res.send(err);
			res.json(user);
		});
	})
	.post(function(req, res) {
		User.findById(req.params.user_id, function(err, user) {
			if (err)
				res.send(err);
			user.name = req.body.name;
			user.save(function(err) {
				if (err)
					res.send(err);
				res.json({ message: "User updated" });
			});
		});
	})
	.delete(function(req, res) {
		User.remove({ _id: req.params.user_id }, function(err, user) {
			if (err)
				res.send(err);
			res.json({ message: "Successfully deleted" });
		})
	});


//REGISTER ROUTE
app.use("/api", router);

app.listen(port);

module.exports = app;