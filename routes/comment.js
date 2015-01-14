/**
 * Created by Eric on 14/01/2015.
 */

var comment = require("./commentHandler");

module.exports = function (app) {
    app.post("/commentByRecipe", comment.getByRecipe);
    app.post("/commentByCreator", comment.getByCreator);
    app.post("/comment", comment.create);
};