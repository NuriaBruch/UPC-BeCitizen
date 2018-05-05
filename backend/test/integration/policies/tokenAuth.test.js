var request = require('supertest');

describe('tokenAuthPolice', function () {
    describe('#voteThread()', function () {
        before(function(done){
            Thread.create({id: 1, title: "Test", content: "Test"})
            .then(thread => done());
        })

        it('should get error', function (done) {
            request(sails.hooks.http.app)
            .put('/voteThread?threadId=1')
            .set("token", null)
            .expect(200, {
                status: "Error",
                errors: ["User not logged in"]
            }, done);
        });
    });

    describe('#voteComment()', function () {
        before(function(done){
            Comment.create({id: 1, content: "Test"})
            .then(comment => done());
        })

        it('should get error', function (done) {
            request(sails.hooks.http.app)
            .put('/voteComment?commentId=1')
            .set("token", null)
            .expect(200, {
                status: "Error",
                errors: ["User not logged in"]
            }, done);
        });
    });

    describe('#reportComment()', function () {
        before(function(done){
            Comment.create({id: 1, content: "Test"})
            .then(comment => done());
        })

        it('should get error', function (done) {
            request(sails.hooks.http.app)
            .put('/reportComment?commentId=1')
            .set("token", null)
            .expect(200, {
                status: "Error",
                errors: ["User not logged in"]
            }, done);
        });
    });

    describe('#createThread()', function () {
        it('should get error', function (done) {
            request(sails.hooks.http.app)
            .post('/newThread')
            .send({
                title: "testTitle",
                content: "test",
                category: "culture"
            })
            .set("token", null)
            .expect(200, {
                status: "Error",
                errors: ["User not logged in"]
            }, done);
        });
    });
});