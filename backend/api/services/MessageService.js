
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
                    var nUser;
                    if(conversationFound.user1.email === email) nUser = 1;
                    else nUser = 2;
                    
                    if(conversationFound.blockedByUser1 || conversationFound.blockedByUser2){
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
                                    conversationFound.save(function(err){
                                        if(err && err !== undefined){
                                            response.status = "E1";
                                            response.errors.push("Server error");
                                        }
                                    });
                                }
                        });
                    }
                }
            }
            callback(response);
        });
    },

    getMessages: function(mail, idConver, callback){
        var response = {
            status: "OK",
            errors: [],
            messages: []
        }

        var order = 'createdAt DESC'

        Conversation.findOne({
            or:[
                {user1: mail,
                id: idConver},
                {user2: mail,
                id: idConver}
            ]
        }).populate('user1').populate('messages', {sort: order}).exec(function(err, conversationFound){
            if(err && err !== undefined){
                response.status = "E1";
                response.errors.push("Server error");
            }
            else if(conversationFound === undefined){
                response.status = "E2";
                response.errors.push("There's no conversation belonging to the user for the given id");
            }
            else{
                conversationFound.messages.forEach( message => {
                    var infoMessage = {
                        sendedByMe: "",
                        content: "",
                        date: ""
                    }
                    infoMessage.content = message.content;
                    infoMessage.date = message.createdAt;
                    if(mail === message.user) infoMessage.sendedByMe = true;
                    else infoMessage.sendedByMe = false;
                    response.messages.push(infoMessage);
                });
                if(conversationFound.user1.email === mail) conversationFound.newMessage1 = false;
                else{
                    conversationFound.newMessage2 = false;
                } 
                conversationFound.save(function(err){
                    if(err && err !== undefined){
                        response.status = "E1";
                        response.errors.push("Server error");
                    }
                });
            }
            callback(response);
        });
    }
}