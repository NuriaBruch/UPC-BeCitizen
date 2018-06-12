var bcrypt = require('bcrypt');

module.exports = class MailAuth {

    login(id, pass, callback){
        var response = {
            status: "Ok",
            errors: [],
            info:{
                username: "",
                email: "",
                name:"",
                surname:"",
                biography: "",
                birthday:"",
                profilePicture: "",
                country:"",
                rank:"",
            }
        };

        User.findOne({
            or:[
                {email: id},
                {username: id}
            ]
            }).exec(function(err1, userFound){
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
                    if(err2 !== undefined && err1){
                        response.status = "E1";
                        response.errors.push("Server error");
                    }
                    else if(result == false){
                        response.status = "E4";
                        response.errors.push("Incorrect password");
                    }
                    else{
                        //Status == Ok
                        response.info.username = userFound.username;
                        response.info.email = userFound.email;
                        response.info.name = userFound.name;
                        response.info.surname = userFound.surname;
                        response.info.biography = userFound.biography;
                        response.info.birthday = userFound.birthday;
                        response.info.profilePicture = userFound.profilePicture;
                        response.info.country = userFound.country;
                        response.info.rank = userFound.rank;

                        if(userFound.deactivated){
                            UtilsService.update_deactivated(userFound,function(err3){
                                if(err3 !== undefined && err3) {
                                    response.status = "E1";
                                    response.errors.push(err1);
                                }
                            })
                        }
                    }
                    callback(response);
                });
            }
        });
    };


};
