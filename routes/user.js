/**
 * Created by Eric on 31/10/2014.
 */
var user = require("./userHandler");

module.exports = function(app) {
    app.get("/user/:id", user.getById);
    app.get("/user", user.getAll);
    app.get("/user/:id/rfood", user.getRestrictedFood);
    app.post("/user", user.create);
    app.put("/user/:id", user.update);
    app.delete("/user/:id", user.delete);
};