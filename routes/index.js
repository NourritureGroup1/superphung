/**
 * Created by Eric on 28/10/2014.
 */

var fs = require("fs");
var path = require("path");

// Load all the routes in the directory
module.exports = function(app) {
    fs.readdirSync("./routes").forEach(function(filename) {
        if (filename === path.basename(__filename) ||
            filename.search("Handler") != -1) {
            return;
        }
        console.log(filename);
        require("./" + filename)(app);
    });
};