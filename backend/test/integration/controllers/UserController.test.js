var request = require('supertest');

describe('UserController', function () {

    describe('#register()', function () {
        it('should register an user', function (done) {
            request(sails.hooks.http.app)
                .post('/register')
                .send({
                    "username": "testUser",
                    "password": "testPass",
                    "email": "test@Email.com"
                  })
                .expect(200, {
                    status: 'Ok',
                    errors: []
                }, done);
        });
    });

    describe('#loginMail()', function () {
        it('should log in the user created', function (done) {
            request(sails.hooks.http.app)
                .get('/loginMail')
                .send({
                    "password": "Polencio",
                    "email": "omars"
                  })
                .expect(200, {
                    status: 'Ok',
                    errors: []
                }, done);
        });
    });
});