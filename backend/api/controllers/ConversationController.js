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
        if(senderMail !== recieverMail){
        ConversationService.createConversation(senderMail, recieverMail, function(status){
            res.send(status);
        });
        }
        else{
            var response = {
                status: "Error",
                errors: "Trying to create a conversation with yourself"
             };
            res.send(response);
        }
    },

    getConversations: function(req, res){
        var userMail = UtilsService.getEmailFromHeader(req);

        ConversationService.getAll(userMail, function(status){
            res.send(status);
        });
    }
    
};

