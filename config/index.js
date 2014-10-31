/**
 * Created by Eric on 29/10/2014.
 */

module.exports = function(app) {
    app.set("title", "Nourriture");
    app.set("port", process.env.PORT);
    app.set("views", __dirname + "/views");
    app.set("models", __dirname + "/models");
};