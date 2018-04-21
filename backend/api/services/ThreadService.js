
module.exports = {

    createThread: function(userMail, title, content, category,callback){
        var response = {
            status: "Ok",
            errors: []
         };
         sails.log(userMail);
        Thread.create({
            title: title,
            content:content,
            category:category,
            createdBy: userMail
        }).exec(function(err2, newThread){
            if(err2 !== undefined && err2){
                sails.log(newThread);
                response.status = "E2";
                response.errors.push(err2);
            }
            callback(response);
        })}

}