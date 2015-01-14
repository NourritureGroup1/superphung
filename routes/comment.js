/**
 * Created by Eric on 14/01/2015.
 */

var comment = require("./commentHandler");

module.exports = function (app) {
    app.post("/commentByRecipe", moment.getByRecipe);
    app.post("/commentByCreator", moment.getByCreator);
    app.post("/comment", comment.create);
};