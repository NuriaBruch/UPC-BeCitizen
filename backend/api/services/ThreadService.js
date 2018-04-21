function evaluateThread(){}
function increaseUserKarma(points,mail){
    User.findOne({email: mail}).exec(function(err1, userFound){
        if(err1 !== undefined && err1) {
            sails.log("err1");
            return null;
        }
        if(userFound === undefined){
            sails.log("err2");
            return null;
        }
        else{
            sails.log("err3");
            userFound.karma = userFound.karma + points;
            userFound.save();
            return userFound.karma;
        }
    });
}
module.exports = {

    createThread: function(userMail, title, content, category,callback){
        var response = {
            status: "Ok",
            errors: []
         };

        Thread.create({
            title: title,
            content:content,
            category:category,
            postedBy: userMail
        }).exec(function(err2, newThread){
            if(err2 !== undefined && err2){
                response.status = "E2";
                response.errors.push(err2);
            }
            else{
                var result = increaseUserKarma(100,userMail);
                if(result === null){
                    response.status = 'E4';
                    response.errors.push("unable to update user karma");
                }
            }
            callback(response);
        })}

}