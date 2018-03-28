function randomString(length, chars) {
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
}

function getGoogleMail(accessToken,callback){
    const request = require('request');
    const options = {  
        url: 'https://www.googleapis.com/oauth2/v1/tokeninfo?access_token='.concat(accessToken),
        method: 'GET',
    };
    request(options, function(err1, res, body) {
        if(err1 !== undefined && err1) {
            var mail = "badTokenConfirmation";
            callback(mail);
        } 
        else{
            var parsed = JSON.parse(body);
            var mail = parsed["email"];
            if(mail === undefined){
                callback("badTokenConfirmation");
            }
            else callback(mail);
        }
    });
}

function logMail(mail, callback){ 
    var usrPassword = randomString(32, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');
    User.create({email: mail, pasword: usrPassword,tipoLogIn: "google"}).exec(function(err3, newUser){
        if(err3 !== undefined && err3){
            callback(err3);
        }
        else callback(undefined);
    });
}

module.exports = class GoogleAuth{
    loginViaGoogle(accessToken,callback){
        var response = {
            status: "Ok",
            usrname: "",
            rank:"",
            errors: []
        };

        getGoogleMail(accessToken,function(usrMail){
            var mail;
            mail = usrMail;
            if(mail==="badTokenConfirmation"){
                response.status = "Error";
                response.errors.push("Unable to confirm access token");
                callback(response);
            }
            else{
                User.findOne({email: mail}).exec(function(err1, userFound){
                    if(err1 !== undefined && err1) {
                        response.status = "Error";
                        response.errors.push(err1);
                        callback(response);
                    }
                    else if(userFound === undefined ) {
                        logMail(mail,function(err2){
                            if(err2 !== undefined && err2){
                                response.status = "Error";
                                response.errors.push(err2);
                                callback(response);
                            }
                            else callback(response);
                        });
                    }
                    else{
                        if(userFound.tipoLogIn === "google"){
                            response.usrname = userFound.username;
                            response.rank = userFound.rango;
                            callback(response)
                        }
                        else{
                            response.status = "Error";
                            response.errors.push("User not granted via google");
                            callback(response);
                        }
                    }
            });
        }});
    }
}