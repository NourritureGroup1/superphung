/**
 * Created by Eric on 10/11/2014.
 */

var request = require("supertest");
var expect = require("chai").expect;

var app = require("../app.js");


describe("REST API /user", function() {

    var log = console.log;

    beforeEach(function() {
      console.log = function(){};
    });

    var userTest = {
        username : "lolibar",
        email : "lolivache@gmail.com"
    };
    var id;

    it("POST /user", function(done) {
        request(app)
            .post("/user")
            .send(userTest)
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                var data = JSON.parse(res.text);
                expect(res.status).to.equal(201);
                expect(userTest.username).to.equal(data.username);
                id = data._id;
                done();
            });
    });

    it("GET /user", function(done) {
        request(app)
            .get("/user")
            .end(function(err, res) {
                //var data = JSON.parse(res.text);
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                done();
            });
    });

    it("DELETE /user", function(done) {
        request(app)
            .delete("/user/" + id)
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                done();
            })
    });
});