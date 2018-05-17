
module.exports = {

    newMessage: function(email, content, idConver, callback){
        var response = {
            status: "OK",
            errors: []
        }
        
        Conversation.findOne({id: idConver}).populate('user1').populate('user2').exec(function(err,conversationFound){
            if(err && err !== undefined){
                response.status = "E1";
                response.errors.push("Server error");
            }
            else if(conversationFound === undefined){
                response.status = "E2";
                response.errors.push("Conversation with the specified id doesn't exist");
            }
            else{
                if(conversationFound.user1.email !== email && conversationFound.user2.email !== email){
                    response.status = "E3";
                    response.errors.push("User doesn't belong to the conversation");
                }
                else {
                    var blocked = false;
                    var nUser;
                    if(conversationFound.user1.email === email){
                        blocked = conversationFound.blockedByUser2;
                        nUser = 1;
                    } 
                    else{
                        blocked = conversationFound.blockedByUser1;
                        nUser = 2;
                    }

                    if(blocked){
                        response.status = "E4";
                        response.errors.push("Blocked conversation");
                    }
                    else{
                        Message.create(
                            {content: content, user: email, belongsTo: idConver}
                            ).exec(function(err,newMessage){
                                if(err && err !== undefined){
                                    response.status = "E1";
                                    response.errors.push("Server error");
                                }
                                else{
                                    if(nUser === 1){
                                        conversationFound.newMessage2 = true;
                                    }
                                    else{
                                        conversationFound.newMessage1 = true;
                                    }
                                    conversationFound.lastMessageTime = newMessage.createdAt;
                                    conversationFound.save();
                                }
                        });
                    }
                }
            }
            callback(response);
        });
    }
}