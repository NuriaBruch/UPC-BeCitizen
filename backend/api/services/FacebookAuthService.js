const APP_ID = "1386882294791151"; 
const APP_SECRET = "d05d76e60a6abaa2dc862accf94e9197";
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

    loginFace: function(userToken,callback){

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
                  response.status='E1';
                  response.errors.push(err);
                  callback(response);
              }
              else {
                var info = JSON.parse(body);
                if(info.error){
                    response.status='E1';
                    response.errors.push(info.error.message);
                    callback(response);
                }
                else if(info.data.error){
                    response.status='E1';
                    response.errors.push(info.data.error.message);
                    callback(response);
                }
                else if(String(info.data.app_id).valueOf === String(APP_ID).valueOf && String(info.data.is_valid).valueOf===String("true").valueOf){
                    //usuari autenticat correctament amb facebook
                    
                    getFacebookUserInfo(info.data.user_id,userToken,
                        function(userInfo){
                            if(userInfo){
                                //tenim la informació del usuari
                                response.info.email = userInfo.email;
                                User.findOne({email: userInfo.email}).exec(function(err, userFound){
                                    if(err !== undefined && err) {
                                        response.status = "E2";
                                        response.errors.push(err);
                                    }
                                    else if(userFound){
                                        //el usuari ya esta registrat
                                        response.loggedIn = "true";
                                        response.info.username = userFound.username;
                                        response.info.name = userFound.name;
                                        response.info.surname = userFound.surname;
                                        response.info.biography = userFound.biography;
                                        response.info.birthday = userFound.birthday;
                                        response.info.country = userFound.country;
                                        response.info.rank = userFound.rank;
                                        if(!userFound.hasFacebook){
                                            User.update({email:userFound.email}).set({hasFacebook:true});
                                        }
                                    }
                                    else{
                                        //el usuari no esta registrat, nomes agafem info
                                        
                                        if(userInfo.birthday != undefined){
                                            var birthday = UtilsService.getFormattedBirthday(userInfo.birthday);
                                            response.info.birthday = birthday;
                                        }
                                        var JsonName = UtilsService.getUserName(userInfo.name);
                                        response.info.name = JsonName.name;
                                        response.info.surname = JsonName.surname;
                                        response.loggedIn = "false";
                                    }
                                    callback(response);
                                });
                            }
                            else{
                                response.status="E3"
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
