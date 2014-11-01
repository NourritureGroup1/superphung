/**
 * Created by Tristan on 01/11/2014.
 */

var login = require("./loginHandler");

/* TODO */

module.exports = function(app) {
    app.get("/login", login.connect);
    app.post("/login", login.authentification);
};

