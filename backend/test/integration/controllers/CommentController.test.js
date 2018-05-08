var request = require('supertest');

describe('CommentController', function () {
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
                Comment.create({id: 1, content: "Test"})
                .then(comment => done());
            })
        })
    });

    describe('#voteComment()', function () {
        it('should vote the comment specified', function (done) {
            request(sails.hooks.http.app)
            .put('/voteComment?commentId=1')
            .set("token", token)
            .expect(200)
            .then(res => {
                if(res.body.status == 'Ok') done();
                else done(res.body);
            })
        });
    });

    describe('#reportComment()', function () {
        it('should report the comment specified', function (done) {
            request(sails.hooks.http.app)
            .put('/reportComment?commentId=1')
            .set("token", token)
            .expect(200)
            .then(res => {
                if(res.body.status == 'Ok') done();
                else done(res.body);
            })
        });
    });
});