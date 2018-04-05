var bcrypt = require('bcrypt');

module.exports = class MailAuth {

    login(email, pass, callback){
        var response = {
            status: "Ok",
            errors: [],
            info:{
                username: "",
                name:"",
                surname:"",
                biography: "",
                birthday:"",
                country:"",
                rank:"",
            }
        };

        User.findOne({email: email}).exec(function(err1, userFound){
            if(err1 !== undefined && err1) {
                // DB error
                response.status = "E1";
                response.errors.push(err1);
                callback(response);
            }
            else if(userFound === undefined ) {
                response.status = "E2";
                response.errors.push("User not found");
                callback(response);
            }
            else{
                bcrypt.compare(pass, userFound.password, function(err2, result) {
                    if(err1 !== undefined && err1){
                        response.status = "E3";
                        response.errors.push("Server error");
                    }
                    else if(result == false){
                        response.status = "E4";
                        response.errors.push("Incorrect password");
                    }
                    else{
                        //Status == Ok
                        response.info.username = userFound.username;
                        response.info.name = userFound.name;
                        response.info.surname = userFound.surname;
                        response.info.biography = userFound.biography;
                        response.info.birthday = userFound.birthday;
                        response.info.country = userFound.country;
                        response.info.rank = userFound.rank;
                    }
                    callback(response);
                });
            }
        });
    }
};