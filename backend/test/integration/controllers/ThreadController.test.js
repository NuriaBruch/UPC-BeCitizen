var request = require('supertest');

describe('ThreadController', function () {
    var token;
    beforeEach(function(done){
        request(sails.hooks.http.app)
        .post('/register')
        .send({
            "username": "testUser",
            "password": "testPass",
            "email": "test@Email.com"
        })
        .then((res) => {
            request(sails.hooks.http.app)
            .get('/loginMail?email=test@Email.com&password=testPass')
            .then(res => {
                token = res.header.token;
                Thread.create({id: 1, title: "Test", content: "Test"})
                .then(thread => done());
            })
        })
    });

    describe('#voteThread()', function () {
        it('should vote the thread specified', function (done) {
            request(sails.hooks.http.app)
            .put('/voteThread?threadId=1')
            .set("token", token)
            .expect(200)
            .then(res => {
                if(res.body.status == 'Ok') done();
                else done("Error!");
            })
        });
    });
});