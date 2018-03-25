var bcrypt = require('bcrypt');

module.exports = class MailAuth {

    register(user, pass, email, callback){
        var response = {
            status: "Ok",
            errors: []
        };
        const saltRounds = 10;

        bcrypt.hash(pass, saltRounds, function(err1, hash) {
            if(err1 !== undefined && err1) {
                response.status = "Error";
                response.errors.push("Server error");
                callback(response);
            }
            else{
                User.create({username: user, password: hash, email: email}).exec(function(err2, newUser){
                    if(err2 !== undefined && err2){
                        response.status = "Error";
                        response.errors.push(err2);
                    }
                    callback(response);
                });
            }
        });
    }
};