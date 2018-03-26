module.exports = class GoogleAuth{


    registerViaGoogle(mail,usrname,callback){
        var password = mail + usrname + "LoginViaGoogle";
        var response = {
            status: "Ok",
            errors: []
        };
        User.create({username: username, password: password, email: mail}).exec(function(err2, newUser){
            if(err2 !== undefined && err2){
                response.status = "Register error";
                response.errors.push(err2);
            }
            callback(response);
        });
    }
}