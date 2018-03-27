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

    login(email, pass, callback){
        var response = {
            status: "Ok",
            errors: []
        };

        User.findOne({email: email}).exec(function(err1, userFound){
            if(err1 !== undefined && err1) {
                response.status = "Error";
                response.errors.push(err1);
                callback(response);
            }
            else if(userFound === undefined ) {
                response.status = "Error";
                response.errors.push("User not found");
                callback(response);
            }
            else{
                bcrypt.compare(pass, userFound.password, function(err2, result) {
                    if(err1 !== undefined && err1){
                        response.status = "Error";
                        response.errors.push("Server error");
                    }
                    else if(!result){
                        response.status = "Error";
                        response.errors.push("Incorrect password");
                    }
                    callback(response);
                });
            }
        });
    }
};