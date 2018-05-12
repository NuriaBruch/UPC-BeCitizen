
module.exports = {

    createConversation: function(senderMail, recieverMail, callback){
        var response = {
            status: "OK",
            errors: [],
            conversationId: "",
        }
        Conversation.findOne({
            or:[
                {email1: senderMail,
                email2: recieverMail},
                {email1: recieverMail,
                email2: senderMail}
            ]
        }).exec(function(err1, conversationFound){
            if(err1 !== undefined && err1){
                response.status = "E1";
                response.errors.push(err1);
                callback(response);
            }
            else if(conversationFound){
                response.conversationId = conversationFound.id;
                callback(response);
            }
            else{
                Conversation.create({
                    user1: senderMail,
                    email1: senderMail,
                    user2: recieverMail,
                    email2: recieverMail
                }).exec(function(err1,newConversation){
                    if(err1 !== undefined && err1){
                        response.status = "E1";
                        response.errors.push(err1);
                        callback(response);
                    }
                    else{
                        response.conversationId = newConversation.id;
                        callback(response);
                    }
                });
            }
        });
    }

}