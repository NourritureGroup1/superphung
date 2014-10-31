/**
 * Created by Eric on 28/10/2014.
 */
var hello = require("./helloHandler")

module.exports = function (app) {
    app.get("/hello", hello.home);
};