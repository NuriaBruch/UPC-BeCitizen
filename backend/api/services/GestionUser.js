var bcrypt = require('bcrypt');
const nodemailer = require('nodemailer');

function sendNewPass(userFound,callback){
    var response = {
        status: "Ok",
        errors: []
    }
    var randomPass = UtilsService.getRandomString();
    var to = userFound.email;
    var smtpTransport = nodemailer.createTransport({
        service: 'gmail',
        auth: {
               user: '',
               pass: ''
           }
       });
    var mailOptions = {
        from: "Borja Fernández",
        to: userFound.email, 
        subject: 'BeCitizeN password recovery service',
        text: "",
        html: '<div id="main">'+
        '<li#k rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">'+
        '<div class="form-gap"></div>'+
        '<div class="container">'+
        '	<div class="row">'+
        '		<div class="col-md-4 col-md-offset-4">'+
        '            <div class="panel panel-default">'+
        '              <div class="panel-body">'+
        '                <div class="text-center">'+
        '                  <h3><i class="fa fa-lock fa-4x"></i></h3>'+
        '                  <h2 class="text-center">Forgot Password?</h2>'+
        '                  <p>For your security erase this mail as soon as you log into the app. This is your new BeCitizeN password:</p>'+
        '                    GRbrEXhXQBBxol7S34xFtyXo8oz4exp0'+
        '                </div>'+
        '              </div>'+
        '            </div>'+
        '          </div>'+
        '	</div>'+
        '</div>'


            
        
    }
    const saltRounds = 10;

    bcrypt.hash(randomPass, saltRounds, function(err1, hash) {
        if(err1 !== undefined && err1) {
            response.status = "E1";
            response.errors.push("Server error");
            callback(response);
        }
        else{
            var oldPass = userFound.password;
            userFound.password = hash;
            userFound.save(function(error){
                if(error !== undefined && error){
                    response.errors.push(error);
                    response.status = "E1";
                    callback(response);
                }
                else{
                    smtpTransport.sendMail(mailOptions, function(error, response){
                        if(error){
                            var response2 = {
                                status: "E6",
                                errors: []
                            }
                            response2.errors.push(error);
                            userFound.pass = oldPass;
                            userFound.save();
                            callback(response2);
                        }else{
                            callback(response);
                        }
                    });
                }
            });
            
        }
    });
   
};
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

    view(myEmail,username,callback){
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
                rank: "",
                blocked: ""
            }
        }
        User.findOne({username:username}).populate('blockedByUser').exec(function(err1,userFound){
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
                if(_.chain(userFound.blockedByUser).pluck("email").indexOf(myEmail) != -1){
                    response.info.blocked = true;
                }
                else response.info.blocked = false;
            }
            callback(response);
        });
    };

    block(reporterEmail, reportedEmail, callback){
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

            User.findOne({email: reporterEmail}).populate('blocksUser')
            .then(function(userReporter){
            if(userReporter === undefined){
                response.status = "E2";
                response.errors.push("Couldn't find the user.");
                callback(response);
            }
            else{
                User.findOne({email: reportedEmail}).populate("blockedByUser")
                .then(function(userReported){

                    if(userReported === undefined){
                        response.status = "E2";
                        response.errors.push("Couldn't find the user");
                        callback(response);
                    }
                    else{
                        var found = _.chain(userReporter.blocksUser).pluck("email").indexOf(reportedEmail).value();
                        if(found == -1){
                            userReporter.blocksUser.add(reportedEmail);
                            userReported.blockedByUser.add(reporterEmail);
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

    unblock(reporterEmail,reportedEmail,callback){
        var response = {
            status: "Ok",
            errors: []
        }
        User.findOne({email: reporterEmail}).populate("blocksUser")
            .then(function(userReporter){
            if(userReporter === undefined){
                response.status = "E2";
                response.errors.push("Couldn't find the user.");
                callback(response);
            }
            else{
                User.findOne({email: reportedEmail}).populate("blockedByUser")
                .then(function(userReported){

                    if(userReported === undefined){
                        response.status = "E2";
                        response.errors.push("Couldn't find the user.");
                        callback(response);
                    }
                    else{
                        var found = _.chain(userReporter.blocksUser).pluck("email").indexOf(reportedEmail).value();
                        if(found != -1){
                            ConversationService.unblockConversation(reporterEmail, reportedEmail, function(satus){

                                userReporter.blocksUser.remove(reportedEmail);
                                userReported.blockedByUser.remove(reporterEmail);
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
    },
    
    resetPassword(userMail,callback){
        var response = {
            status: "Ok",
            errors: []
        }
        User.findOne({email:userMail}).exec(function(err1,userFound){
            if(err1 !== undefined && err1){
                response.status = "E1";
                response.errors.push(err1);
                callback(response);
            }
            else if(userFound === undefined){
                response.status = "E2";
                response.errors.push("User not found");
                callback(response);
            }
            else{
                sendNewPass(userFound,function(response2){
                    callback(response2);
                })
            }
        }); 
    };
};
