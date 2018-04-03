
function register(user, pass, email, name, surname, biography, birthday, country, rank, hasFace, hasGoogle, callback){
    var response = {
        status: "Ok",
        errors: []
    };
    const saltRounds = 10;

    UtilsService.getHashedString(pass,saltRounds,function(err1,hash){
        if(err1 !== undefined && err1) {
            response.status = "Error";
            response.errors.push("Server error");
            callback(response);
        }
        else{
            User.create({
                username: user, 
                password: hash, 
                email: email,
                name: name,
                surname: surname,
                biography: biography,
                birthday: birthday,
                country: country,
                rank: rank,
                hasFace: hasFace,
                hasGoogle: hasGoogle
            }).exec(function(err2, newUser){
                if(err2 !== undefined && err2){
                    response.status = "Error";
                    response.errors.push(err2);
                }
                callback(response);
            });
        }
    });
}