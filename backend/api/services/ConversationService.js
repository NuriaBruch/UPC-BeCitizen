
module.exports = {

    createConversation: function(senderMail, recieverMail, callback){
        var response = {
            status: "OK",
            errors: [],
            conversationId: "",
        }
        User.findOne({email: recieverMail}).populate('blocksUser').populate('blockedByUser').exec(function(err1, userFound){
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
            else if(_.chain(userFound.blocksUser).pluck("email").indexOf(senderMail) != -1 ||
                    _.chain(userFound.blockedByUser).pluck("email").indexOf(senderMail) != -1){
                response.status = "E3";
                response.errors.push("You have blocked this user or been blocked by this user");
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
                        profilePicture: "",
                        newMessage: "",
                        lastMessageTime: "",
                        lastMessageContent: ""
                    };
                    converInfo.id = conver.id;
                    converInfo.lastMessageTime = conver.lastMessageTime;
                    converInfo.lastMessageContent = conver.lastMessageContent;
                    if(conver.user1.email === userMail){
                        converInfo.username = conver.user2.username;
                        converInfo.newMessage = conver.newMessage1;
                        converInfo.profilePicture = conver.user2.profilePicture;
                    }
                    else if(conver.user2.email === userMail){
                        converInfo.username = conver.user1.username;
                        converInfo.newMessage = conver.newMessage2;
                        converInfo.profilePicture = conver.user1.profilePicture;
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
    },

    blockConversation: function(userMail, blockedMail, callback){
        var response = {
            status: "OK",
            errors: []
        }
        Conversation.findOne({
            or:[
                {user1: userMail,
                user2: blockedMail},
                {user1: blockedMail,
                user2: userMail}
            ]
        }).populate('user1').exec(function(err1, conversationFound){
            if(err1 && err1 !== undefined){
                response.status = "E1";
                response.errors.push(err1);
                callback(response);
            }
            else if(conversationFound !== undefined){
                if(conversationFound.user1.email == userMail){
                    conversationFound.blockedByUser1 = true;
                }
                else{
                    conversationFound.blockedByUser2 = true;
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
    },

    unblockConversation: function(userMail, blockedMail, callback){
        var response = {
            status: "OK",
            errors: []
        }
        Conversation.findOne({
            or:[
                {user1: userMail,
                user2: blockedMail},
                {user1: blockedMail,
                user2: userMail}
            ]
        }).populate('user1').populate('user2').exec(function(err1, conversationFound){
            if(err1 && err1 !== undefined){
                response.status = "E1";
                response.errors.push("Server error");
                callback(response);
            }
            else if(conversationFound){
                if(conversationFound.user1.email == userMail){
                    conversationFound.blockedByUser1 = false;
                }
                else{
                    conversationFound.blockedByUser2 = false;
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
    },
}
