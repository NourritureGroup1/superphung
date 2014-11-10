/**
 * Created by Eric on 10/11/2014.
 */

module.exports = function(grunt) {

    grunt.loadNpmTasks("grunt-mocha-test");

    grunt.initConfig({
        pkg : grunt.file.readJSON("package.json")
    });

    grunt.config("mochaTest", require("./grunt/mochaTest.js"));

    grunt.registerTask("default", ["mochaTest"]);
};