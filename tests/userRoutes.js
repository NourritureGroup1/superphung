/**
 * Created by Eric on 10/11/2014.
 */

var request = require("supertest");
var expect = require("chai").expect;

var app = require("../app.js");


describe("REST API /user", function() {

    var log = console.log;

    /*beforeEach(function() {
      console.log = function(){};
    });*/

    var userTest = {
        username : "lolibar",
        email : "lolivache@gmail.com",
        likes : [
            "545f3b5dc55b49d80a941978",
            "545f3bb4c55b49d80a941979"
        ],
        restrictedFood : [
            "545a0b0dd9087e0424db9e9b"
        ]
    };
    var id;

    it("POST /user", function(done) {
        request(app)
            .post("/user")
            .send(userTest)
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(201);
                var data = JSON.parse(res.text);
                expect(userTest.username).to.equal(data.username);
                id = data._id;
                done();
            });
    });

    it ("GET /user/:id", function(done) {
        request(app)
            .get("/user/" + id)
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(userTest.username).to.equal(data.username);
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

    it("GET /user/:id/likes", function(done) {
        request(app)
            .get("/user/" + id + "/likes")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(userTest.likes[0]).to.equal(data[0]._id);
                expect(userTest.likes[1]).to.equal(data[1]._id);
                done();
            });
    });

    it("GET /user/:id/dislikes", function(done) {
        request(app)
            .get("/user/" + id + "/dislikes")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(204);
                done();
            });
    });

    it("GET /user/:id/rfood", function(done) {
        request(app)
            .get("/user/" + id + "/rfood")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                var data = JSON.parse(res.text);
                expect(res.status).to.equal(200);
                expect(userTest.restrictedFood[0]).to.equal(data[0]._id);
                done();
            });
    });

    it("GET /user/:id/ffood", function(done) {
        request(app)
            .get("/user/" + id + "/ffood")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(204);
                done();
            });
    });

    it("GET /user/:id/bfood", function(done) {
        request(app)
            .get("/user/" + id + "/bfood")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(204);
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
            });
    });
});