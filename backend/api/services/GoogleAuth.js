function randomString(length, chars) {
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
}


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
        var parsed = JSON.parse(body);
        var mail = parsed["email"];
        console.log(mail);
        console.log(body);
        console.log(accessToken);
        var usrPassword = randomString(32, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');
        console.log(usrPassword);
        
        User.create({email: mail, pasword: usrPassword,tipoLogIn: "google"}).exec(function(err2, newUser){
            if(err2 !== undefined && err2){
                callback(err2);
            }
            else callback(undefined);
        });
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
                response.status = "Google registration error";
                response.errors.push(err);
                callback(response);
            }
            else callback(response);
        });
        
    }
}