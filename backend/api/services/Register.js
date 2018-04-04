var bcrypt = require('bcrypt');

module.exports = class Register {

    registerAll(user, pass, email, name, surname, birthday, country, hasFace, hasGoogle, callback){
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
                User.create({
                    username: user, 
                    password: pass, 
                    email: email,
                    name: name,
                    surname: surname,
                    birthday: birthday,
                    country: country,
                    hasFace: hasFace,
                    hasGoogle: hasGoogle
                }).exec(function(err2, newUser){
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