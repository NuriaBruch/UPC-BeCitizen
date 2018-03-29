const APP_ID = "230715197485988";
const APP_SECRET = "52e3445c32ff62dc31514a6a14e73803";
var request = require("request");



function getFacebookUserInfo(userId,userToken,callback){
   
    request(
        { method: 'GET'
        , uri: 'https://graph.facebook.com/v2.12/'+userId+'?access_token='+userToken+'&fields=name,email,locale'
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
            username: "",
            rank:""
        };


        request(
            { method: 'GET'
            , uri: 'https://graph.facebook.com/debug_token?input_token='+userToken+'&access_token='+APP_ID+'|'+APP_SECRET
            }
          ,function (err, resp, body) {
              if(err !== undefined && err){
                  console.log('Error en la request register: '+err);
                  response.status='Error';
                  response.errors.push(err);
                  callback(response);
              }
              else {
                var info = JSON.parse(body);
                if(info.error){
                    console.log('Error en los parametros: '+info.error.message);
                    response.status='Error';
                    response.errors.push(info.error.message);
                    callback(response);
                }
                else if(info.data.error){
                    console.log('Error al comprovar tokens: '+info.data.error.message);
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
                                    else if(!userFound){
                                        //no ha trobat cap usuari, l'insertem a la db
                                        var password = UtilsService.getRandomString();
                                        User.create({password: password, email: userInfo.email, hasFacebook: true}).exec(
                                            function(err2, newUser){
                                                if(err2 !== undefined && err2){
                                                    response.status = "Error";
                                                    response.errors.push(err2);
                                                }
                                            }
                                        );
                                    }
                                    else{
                                        //usuari ya estaba registrat a la db
                                        response.username = userFound.username;
                                        response.rank = "";
                                        if(!userFound.hasFacebook){
                                            User.update({email:userFound.email}).set({hasFacebook:true});
                                        }
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
