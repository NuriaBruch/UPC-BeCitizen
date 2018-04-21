
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
            createdBy: userMail
        }).exec(function(err2, newThread){
            if(err2 !== undefined && err2){
                sails.log(newThread);
                response.status = "E2";
                response.errors.push(err2);
            }
            else{
                //aqui hay que actualizar el carma del usuario pero de momento
                //no se puede hacer por culpa de la foreign key
            }
            callback(response);
        })}

}