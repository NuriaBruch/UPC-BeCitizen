function getGoogleMail(accessToken,callback){
    const request = require('request');
    const options = {  
        url: 'https://www.googleapis.com/oauth2/v3/tokeninfo?id_token='.concat(accessToken),
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
            var name = parsed["name"];
            if(mail === undefined || name === undefined){
                callback("badTokenConfirmation",undefined);
            }
            else callback(mail,name);
        }
    });
}

module.exports = class GoogleAuth{
    loginViaGoogle(accessToken,callback){
        var response = {
            status: "Ok",
            errors: [] ,
            loggedIn: "",
            info:{
                email:"",
                username: "",
                name:"",
                surname:"",
                biography: "",
                birthday:"",
                country:"",
                rank:"",
            }
        };

        getGoogleMail(accessToken,function(usrMail,usrName){
            var mail;
            var name;
            mail = usrMail;
            name = usrName;
            if(mail==="badTokenConfirmation"){
                response.status = "E1";
                response.errors.push("Unable to confirm access token");
                callback(response);
            }
            else{
                User.findOne({email: mail}).exec(function(err1, userFound){
                    if(err1 !== undefined && err1) {
                        response.status = "E2";
                        response.errors.push(err1);
                        callback(response);
                    }
                    else if(userFound === undefined ) {
                        UtilsService.getUsrInfo(name,function(usrInfo){
                        response.info.name = usrInfo.name;
                        response.info.surname = usrInfo.surname;
                        response.loggedIn = "False";
                        response.info.email = mail;
                        callback(response);
                        });
                        
                    }
                    else{
                        if(userFound.tipoLogIn === "google"){
                            response.loggedIn = "True";
                            response.info.username = userFound.username;
                            response.info.email = userFound.email;
                            response.info.name = userFound.name;
                            response.info.surname = userFound.surname;
                            response.info.biography = userFound.biography;
                            response.info.birthday = userFound.birthday;
                            response.info.country = userFound.country;
                            response.info.rank = userFound.rank;
                            callback(response)
                        }
                        else{
                            response.status = "E3";
                            response.errors.push("User not granted via google");
                            callback(response);
                        }
                    }
            });
        }});
    }
}