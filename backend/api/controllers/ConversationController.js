/**
 * ConversationController
 *
 * @description :: Server-side logic for managing Conversations
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
	newConversation: function(req, res){
        var senderMail = UtilsService.getEmailFromHeader(req);
        var recieverMail = req.body.email;
        ConversationService.createConversation(senderMail, recieverMail, function(status){
            res.send(status);
        });
    },
    
};

