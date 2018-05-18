var bcrypt = require('bcrypt');

module.exports = class GestionUser {

    register(username, pass, email, name, surname, birthday, country, profilePicture, hasFace, hasGoogle, callback){
        var response = {
           status: "Ok",
           errors: []
        };

        if (pass === undefined) pass = UtilsService.getRandomString();
        
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
                    profilePicture: profilePicture,
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
    
    deactivate(userMail,callback){
        var response = {
            status: "Ok",
            errors: []
        }
        User.update({email:userMail},{deactivated:true}).exec(function(err1,userFound){
            if(err1 !== undefined && err1){
                response.status = "Error";
                response.errors.push("Server error");
            }
            callback(response);
        });
    };

    update(req,callback){
        var response = {
            status: "Ok",
            errors: []
        }
        var userMail = UtilsService.getEmailFromHeader(req);
        var{name, surname, biography, birthday, country, profilePicture} = req.body;

        User.update({email:userMail}, 
            {name:name, surname:surname, biography:biography, birthday:birthday,
            country:country, profilePicture}).exec(function(err1,userFound){
                if(err1 !== undefined && err1){
                    response.status = "Error";
                    response.errors.push("Server error");
                }
                callback(response); 
            });
    };

    view(username,callback){
        var response = {
            status: "Ok",
            errors: [],
            info: {
                email: "",
                name: "",
                surname: "",
                biography: "",
                birthday: "",
                profilePicture: "",
                country: "",
                rank: ""
            }
        }
        User.findOne({username:username}).exec(function(err1,userFound){
            if(err1 !== undefined && err1) {
                // DB error
                response.status = "E1";
                response.errors.push(err1);
            }
            else if(userFound === undefined ) {
                response.status = "E2";
                response.errors.push("User not found");
            }
            else if(userFound.deactivated){
                response.status = "E3";
                response.errors.push("User account deactivated");
            }
            else{
                //Status == Ok
                response.info.email = userFound.email;
                response.info.name = userFound.name;
                response.info.surname = userFound.surname;
                response.info.biography = userFound.biography;
                response.info.birthday = userFound.birthday;
                response.info.profilePicture = userFound.profilePicture;
                response.info.country = userFound.country;
                response.info.rank = userFound.rank;
            }
            callback(response);
        });
    };
    
    report(reporterEmail, reportedEmail, callback){
        var response = {
            status: "Ok",
            errors: []
        }
        if(reportedEmail === reporterEmail){
            response.status = "E3";
            response.errors.push("You can't report yourself");
            callback(response);
        }
        else{

            User.findOne({email: reporterEmail}).populate('reportsUser')
            .then(function(userReporter){
            if(userReporter === undefined){
                response.status = "E2";
                response.errors.push("Couldn't find the user.");
                callback(response);
            }
            else{
                User.findOne({email: reportedEmail}).populate("reportedByUser")
                .then(function(userReported){
                    
                    if(userReported === undefined){
                        response.status = "E2";
                        response.errors.push("Couldn't find the user");
                        callback(response);
                    }
                    else{
                        var found = _.chain(userReporter.reportsUser).pluck("email").indexOf(reportedEmail).value();
                        if(found == -1){
                            userReporter.reportsUser.add(reportedEmail);
                            userReported.reportedByUser.add(reporterEmail);
                            userReported.save(function(err){
                                userReporter.save(function(err2){
                                    if(err || err2){
                                        response.status = "E1";
                                        response.push(err);
                                        response.push(err2);
                                    }
                                    callback(response);
                                    });
                                });
                        }
                        else{
                            response.status = "E4";
                            response.errors.push("The user has already reported the user.");
                            callback(response);
                        }
                    }
                })
                .catch(function(error){
                    response.status = "E1";
                    response.errors.push("Fail");
                    callback(response);
                });
            }
            })
            .catch(function(error){
            response.status = "E1";
            response.errors.push("Server error.");
            callback(response);
            });
        }
    };

    unreport(reporterEmail,reportedEmail,callback){
        var response = {
            status: "Ok",
            errors: []
        }
        User.findOne({email: reporterEmail}).populate("reportsUser")
            .then(function(userReporter){
            if(userReporter === undefined){
                response.status = "E2";
                response.errors.push("Couldn't find the user.");
                callback(response);
            }
            else{
                User.findOne({email: reportedEmail}).populate("reportedByUser")
                .then(function(userReported){
                    
                    if(userReported === undefined){
                        response.status = "E2";
                        response.errors.push("Couldn't find the user.");
                        callback(response);
                    }
                    else{
                        var found = _.chain(userReporter.reportsUser).pluck("email").indexOf(reportedEmail).value();
                        if(found != -1){
                            ConversationService.unblockConversation(reporterEmail, reportedEmail, function(satus){
                                
                                userReporter.reportsUser.remove(reportedEmail);
                                userReported.reportedByUser.remove(reporterEmail);
                                userReported.save(function(err){
                                userReporter.save(function(err2){
                                if(err || err2){
                                    response.status = "E1";
                                    response.push(err);
                                    response.push(err2);
                                        
                                }
                                callback(response);
                                });
                            });
                           
                        }); 
                    }
                        else{
                            response.status = "E5";
                            response.errors.push("The user has not been reported.");
                            callback(response);
                        }
                    }
                })
                .catch(function(error){
                    response.status = "E1";
                    response.errors.push(error);
                    callback(response);
                });
            }
            })
    }
};