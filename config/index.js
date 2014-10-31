/**
 * Created by Eric on 29/10/2014.
 */

var bodyParser = require("body-parser");

module.exports = function(app) {
    app.set("title", "Nourriture");
    app.set("port", process.env.PORT || 8081);
    app.set("views", __dirname + "/views");
    app.set("models", __dirname + "/models");
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(bodyParser.json());
};
