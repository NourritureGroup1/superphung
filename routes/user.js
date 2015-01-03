/**
 * Created by Eric on 31/10/2014.
 */
var user = require("./userHandler");

module.exports = function(app) {
    app.get("/user/:id", user.getById);
    app.get("/user", user.getAll);
    app.get("/user/:id/likes", user.getLikesRecipes);
    app.get("/user/:id/dislikes", user.getDislikesRecipes);
    app.get("/user/:id/rfood", user.getRestrictedFood);
    app.get("/user/:id/ffood", user.getFavoriteFood);
    app.get("/user/:id/bfood", user.getBadFood);
    app.post("/user", user.create);
    app.post("/user/social", user.createSocial);
    app.put("/user/:id", user.update);
    app.delete("/user/:id", user.delete);
};