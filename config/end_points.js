/**
 * Created by Eric on 15/11/2014.
 */

module.exports = function(routes) {

    var Table = require('cli-table');
    var table = new Table({ head : ["", "Path"] });

    console.log('\n********************************************');
    console.log('\t\tNourriture API');
    console.log('********************************************\n');
    for (var key in routes) {
        if (routes.hasOwnProperty(key)) {
            var val = routes[key];
            if (val.route) {
                val = val.route;
                var _o = {};
                _o[val.stack[0].method] = [val.path];
                table.push(_o);
            }
        }
    }
    console.log(table.toString());
    return (table);
};