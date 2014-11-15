/**
 * Created by Eric on 11/11/2014.
 */

var request = require("supertest");
var expect = require("chai").expect;

var app = require("../../app.js");

describe("REST API /ingredient", function() {

    var log = console.log;

    var ingredientTest = {
        name : "pomme",
        description : "pour superphung",
        nutrients : [
            "vitamine A",
            "vitamine B"
        ],
        category : [
            "fruit",
            "phung"
        ]
    };

    var ingredientRes = 0;
    it("SETUP /ingredient", function(done) {
        request(app)
            .get("/ingredient/pomme/ssearch")
            .end(function(err, res) {
                if (res.status == 200) {
                    ingredientRes = 1;
                    var data = JSON.parse(res.text);
                    ingredientTest = data;
                }
                done();
            });
    });

    it("POST /ingredient", function(done) {
        if (ingredientRes == 0) {
            request(app)
                .post("/ingredient")
                .send(ingredientTest)
                .end(function(err, res) {
                    console.log = log;
                    expect(err).to.be.null;
                    expect(res.status).to.equal(201);
                    var data = JSON.parse(res.text);
                    expect(ingredientTest.name).to.equal(data.name);
                    ingredientTest._id = data._id;
                    done();
                });
        }
        else {
            done();
        }
    });

   it("GET /ingredient/:id", function(done) {
        request(app)
            .get("/ingredient/" + ingredientTest._id)
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(ingredientTest.name).to.equal(data.name);
                done();
            });
    });

    it("GET /ingredient", function(done) {
        request(app)
            .get("/ingredient")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(data.length).to.be.at.least(1);
                done();
            });
    });

    it("GET /ingredient/:name/ssearch", function(done) {
       request(app)
           .get("/ingredient/pomme/ssearch")
           .end(function(err, res) {
               console.log = log;
               expect(err).to.be.null;
               expect(res.status).to.equal(200);
               done();
           });
    });

    it("GET /ingredient/:name/psearch", function(done) {
        request(app)
            .get("/ingredient/pomme/psearch")
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                done();
            });
    });

    it("PUT /ingredient/:id", function(done) {
        ingredientTest.description = "pour megaphung";
        request(app)
            .put("/ingredient/" + ingredientTest._id)
            .send(ingredientTest)
            .end(function(err, res) {
                console.log = log;
                expect(err).to.be.null;
                expect(res.status).to.equal(200);
                var data = JSON.parse(res.text);
                expect(ingredientTest.description).to.equal(data.description);
                done();
            });
    });

    //delete ingredient in userRoutes.js
});