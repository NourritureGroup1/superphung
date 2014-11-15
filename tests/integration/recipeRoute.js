/**
 * Created by Eric on 12/11/2014.
 */

var request = require("supertest");
var expect = require("chai").expect;
var moment = require("moment");

var app = require("../../app.js");

describe("REST API /recipe", function() {

    var log = console.log;

    var recipeTest1 = {
        _id : "",
        name : "nems",
        creator : "eric",
        ingredients : [
        ],
        creationDate : ""
    };

    var recipeRes = 0;

    it("SETUP /recipe", function(done) {
        request(app)
            .get("/recipe/nems/eric")
            .end(function(err, res) {
                if (res.status == 200) {
                    recipeRes = 1;
                    var data = JSON.parse(res.text);
                    recipeTest1 = data;
                }
                done();
            });
    });

    it("SETUP /recipe", function(done) {
        request(app)
            .get("/ingredient/pomme/ssearch")
            .end(function(err, res) {
                var data = JSON.parse(res.text);
                recipeTest1.ingredients.push(data._id);
                done();
            });
    });

    it("POST /recipe", function(done) {
        if (recipeRes == 0) {
            request(app)
                .post("/recipe")
                .send(recipeTest1)
                .end(function(err, res) {
                    console.log = log;
                    expect(err).to.be.null;
                    expect(res.status).to.equal(201);
                    var data = JSON.parse(res.text);
                    expect(recipeTest1.name).to.equal(data.name);
                    recipeTest1._id = data._id;
                    done();
                })
        }
        else
            done();
    });

    it("GET /recipe/:id", function(done) {
        request(app)
            .get("/recipe/" + recipeTest1._id)
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(recipeTest1.name).to.equal(data.name);
                expect(recipeTest1.creator).to.equal(data.creator);
                done();
            });
    });

    it("GET /recipe", function(done) {
        request(app)
            .get("/recipe")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(data.length).to.be.at.least(1);
                done();
            })
    })

    it("GET /recipe/:id/ingredient", function(done) {
        request(app)
            .get("/recipe/" + recipeTest1._id + "/ingredient")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(recipeTest1.ingredients[0]).to.equal(data[0]._id);
                done();
            })
    });

    it ("GET /recipe/:name/:creator", function(done) {
        request(app)
            .get("/recipe/nems/eric")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                done();
            });
    });

    it("PUT /recipe/:id", function(done) {
        recipeTest1.creationDate = moment().format("MM/DD/YYYY h:mm:ss a");
        request(app)
            .put("/recipe/" + recipeTest1._id)
            .send(recipeTest1)
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(recipeTest1.creationDate).to.equal(data.creationDate);
                done();
            });
    });

    //delete recipe in userRoutes.js
});