/**
 * Created by Eric on 11/11/2014.
 */

var request = require("supertest");
var expect = require("chai").expect;

var app = require("../app.js");

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

    var ingredientRes;
    request(app)
        .get("/ingredient/pomme/ssearch")
        .end(function(err, res) {
            if (res.status == 200) ingredientRes = 1;
            else ingredientRes = 0;
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
                });
        }
        done();
    });
});