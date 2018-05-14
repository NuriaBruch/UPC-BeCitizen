
module.exports = {

    createConversation: function(senderMail, recieverMail, callback){
        var response = {
            status: "OK",
            errors: [],
            conversationId: "",
        }
        User.findOne({email: recieverMail}).exec(function(err1, userFound){
            if(err1 !== undefined && err1){
                response.status = "E1";
                response.errors.push(err1);
                callback(response);
            }
            else if(userFound === undefined){
                response.status = "E2";
                response.errors.push("User not found");
                callback(response);
            }
            else{
                Conversation.findOne({
                or:[
                    {user1: senderMail,
                    user2: recieverMail},
                    {user1: recieverMail,
                    user2: senderMail}
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
                        user2: recieverMail
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
            
        });
       
    },

    getAll: function(userMail, callback){
        var response = {
            status: "Ok",
            errors: [],
            conversations:[]
         };
        var order = 'lastMessageTime DESC'
        Conversation.find({
            or:[
                {user1: userMail},
                {user2: userMail}
            ]
        }).sort(order).populate('user1').populate('user2').exec(function(err1, conversations){
            if(err1 !== undefined && err1){
                response.status = "E1";
                response.errors.push(err1);
                callback(response);
            }
            else if(conversations){
                conversations.forEach(conver => {
                    var converInfo = {
                        id: "",
                        username: "",
                        newMessage: "",
                        lastMessageTime: ""
                    };
                    converInfo.id = conver.id;
                    converInfo.lastMessageTime = conver.lastMessageTime;
                    if(conver.user1.email !== userMail){
                        converInfo.username = conver.user1.username;
                        converInfo.newMessage = conver.newMessage1;
                    }
                    else if(conver.user2 !== userMail){
                        converInfo.username = conver.user2.username;
                        converInfo.newMessage = conver.newMessage2;
                    }
                    response.conversations.push(converInfo);
                });
            }
            else{
                response.status = "E2";
                response.errors.push("There are no conversations");
            }
            callback(response);
        });
    }
}