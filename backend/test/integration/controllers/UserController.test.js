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
        before(function(done){
            User.create({
                "username": "testUser",
                "password": "testPass",
                "email": "test@Email.com"
              })
            .exec(function (err, userCreated){
                if(!err) {  
                    done();
                }
            });
        });

        it('should log in the user created', function (done) {
            request(sails.hooks.http.app)
            .get('/loginMail?email=test@Email.com&password=testPass')
            .expect(200)
            .then(res => {
                if(res.body.status == 'Ok') done();
                else done(res.body);
            })
            
        });
    });
});