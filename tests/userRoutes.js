/**
 * Created by Eric on 10/11/2014.
 */

var request = require("supertest");
var expect = require("chai").expect;

var app = require("../app.js");


describe("GET /user", function() {

    var log = console.log;

    beforeEach(function() {
        console.log = function(){};
    });

    it("should get all the user", function(done) {
        request(app)
            .get("/user")
            .end(function(err, res) {
                console.log = log;
                var data = JSON.parse(res.text);
                expect(err).to.be.null;
                done();
            });
    });
});