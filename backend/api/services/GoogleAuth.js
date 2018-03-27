function getGoogleMail(accessToken, callback){
    const request = require('request');

    const options = {  
        url: 'https://www.googleapis.com/oauth2/v1/tokeninfo?access_token='.concat(accessToken),
        method: 'GET',
    };


    request(options, function(err, res, body) {
        if(err) {
            callback(err);
        } 
        var mail = body.email;
        var usrPassword = Math.random().toString(36).slice(-8);
        
        User.create({email: mail, pasword: usrPassword}).exec(function(err2, newUser){
            if(err2 !== undefined && err2){
                response.status = "Register error";
                response.errors.push(err2);
            }
            callback(response);
        });
        callback(undefined);
    });

}

module.exports = class GoogleAuth{
    registerViaGoogle(IdToken,callback){
        var response = {
            status: "Ok",
            errors: []
        };
        getGoogleMail(IdToken,function(err){
            if(err !== undefined && err){
                response.status = "Google auth error";
                response.errors.push(err);
                callback(response);
            }
            else callback(response);
        });
        
    }
}