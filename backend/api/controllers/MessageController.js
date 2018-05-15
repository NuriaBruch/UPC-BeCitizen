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

        MessageService.newMessage(mail,content,function(status){
            res.send(status);
        });
    }
};

