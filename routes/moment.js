/**
 * Created by Eric on 14/01/2015.
 */

var moment = require("./momentHandler");

module.exports = function (app) {
    app.post("/momentByID", moment.getById);
    app.post("/momentByCreator", moment.getByCreator);
    app.post("/momentByArray", moment.getByArray);
    app.post("/moment", moment.create);
};