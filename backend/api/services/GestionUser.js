var bcrypt = require('bcrypt');

module.exports = class GestionUser {

    register(username, pass, email, name, surname, birthday, country, hasFace, hasGoogle, callback){
        var response = {
           status: "Ok",
           errors: []
        };
        
        const saltRounds = 10;

        bcrypt.hash(pass, saltRounds, function(err1, hash) {
            if(err1 !== undefined && err1) {
                response.status = "E1";
                response.errors.push("Server error");
                callback(response);
            }
            else{
                User.create({
                    email: email,
                    username: username, 
                    password: hash, 
                    name: name,
                    surname: surname,
                    birthday: birthday,
                    country: country,
                    hasFacebook: hasFace,
                    hasGoogle: hasGoogle
                }).exec(function(err2, newUser){
                    if(err2 !== undefined && err2){
                        response.status = "E2";
                        response.errors.push(err2);
                    }
                    callback(response);
                });
            }
        });
    };
    checkMail(email,callback){
        var response = {
            status: "Ok",
            found:"Yes",
            errors: []
        }
        User.findOne({email:email}).exec(function(err1,userFound){
            if(err1 !== undefined && err1){
                response.status = "Error";
                response.errors.push("Server error");
            }
            else if(userFound === undefined){
                response.found = "No";
                response.errors.push("User not found");
            }
            callback(response);
        }); 
    }; 
    deactivate(username,callback0){
        var response = {
            status: "Ok",
            errors: []
        }
        User.update({username:username},{deactivated:true}).exec(function(err1,userFound){
            if(err1 !== undefined && err1){
                response.status = "Error";
                response.errors.push("Server error");
            }
            callback(response);
        });
    };
};