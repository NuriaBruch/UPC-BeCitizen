/**
 * MessageController
 *
 * @description :: Server-side logic for managing Messages
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
    
    newMessage: function(req,res){
        var mail = UtilsService.getEmailFromHeader(req);
        var content = req.body.content;
        var idConver = req.body.idConver;

        MessageService.newMessage(mail,content,idConver,function(status){
            res.send(status);
        });
    }
};

