const APP_ID = "230715197485988";
const APP_SECRET = "52e3445c32ff62dc31514a6a14e73803";
var request = require("request");



function getFacebookUserInfo(userId,userToken,callback){
   
    request(
        { method: 'GET'
        , uri: 'https://graph.facebook.com/v2.12/'+userId+'?access_token='+userToken+'&fields=name,email,birthday'
        }
      ,function (err, resp, body) {
            if(err !== undefined && err)console.log('Error en la request \n'+err);
            else {
                var userInfo = JSON.parse(body);
                if(userInfo.error) console.log(userInfo.error);
                callback(userInfo);
             }
         }
    );
}

module.exports = {

    facebookRegister: function(userToken,callback){

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


        request(
            { method: 'GET'
            , uri: 'https://graph.facebook.com/debug_token?input_token='+userToken+'&access_token='+APP_ID+'|'+APP_SECRET
            }
          ,function (err, resp, body) {
              if(err !== undefined && err){
                  response.status='Error';
                  response.errors.push(err);
                  callback(response);
              }
              else {
                var info = JSON.parse(body);
                if(info.error){
                    response.status='Error';
                    response.errors.push(info.error.message);
                    callback(response);
                }
                else if(info.data.error){
                    response.status='Error';
                    response.errors.push(info.data.error.message);
                    callback(response);
                }
                else if(String(info.data.app_id).valueOf === String(APP_ID).valueOf && String(info.data.is_valid).valueOf===String("true").valueOf){
                    //usuari autenticat correctament amb facebook
                    
                    getFacebookUserInfo(info.data.user_id,userToken,
                        function(userInfo){
                            if(userInfo){
                                //tenim la informació del usuari
                                User.findOne({email: userInfo.email}).exec(function(err, userFound){
                                    if(err !== undefined && err) {
                                        response.status = "Error";
                                        response.errors.push(err);
                                    }
                                    else if(userFound){
                                        //el usuari ya esta registrat
                                        response.loggedIn = "true"
                                        response.username = userFound.username;
                                        response.name = userFound.name;
                                        response.surname = userFound.surname;
                                        response.biography = userFound.biography;
                                        response.birthday = userFound.birthday;
                                        response.country = userFound.country;
                                        response.rank = userFound.rank;
                                        if(!userFound.hasFacebook){
                                            User.update({email:userFound.email}).set({hasFacebook:true});
                                        }
                                    }
                                    else{
                                        //el usuari no esta registrat, nomes agafem info
                                        
                                        /*--DESCOMENTAR CUANDO ESTEN IMPLEMENTADAS EN UTILSSERVICE--
                                        var JsonName = UtilsService.getJsonName(userInfo.name);
                                        var birthday = UtilsService.getFormattedBirthday(userInfo.birthday);
                                        response.name = JsonName.name;
                                        response.surname = JsonName.surname;
                                        response.birthday = birthday; */
                                        response.name = userInfo.name;
                                        response.birthday = userInfo.birthday;
                                        response.loggedIn = "false";
                                    }
                                    callback(response);
                                });
                            }
                            else{
                                response.status="Error"
                                response.errors.push("Unable to get user info from Facebook, wrong facebookId");
                                callback(response);
                            }
                        }
                    );



                    
                }
                }
            }
        );

    }
}
