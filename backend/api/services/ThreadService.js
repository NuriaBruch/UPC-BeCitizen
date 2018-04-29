function destroyThread(threadId,callback){
    var response = {
        status: "Ok",
        errors: []
     };
    Thread.destroy({id: threadId}).exec(function(err2){
        if(err2 !== undefined && err2){
            response.status = "E2";
            response.errors.push(err2);
            callback(response);
        }
        else callback(response);
    });
}

function reportAndEvaluateThread(threadId,email,callback){
    var response = {
        status: "Ok",
        errors: []
     };
    Thread.findOne({id: threadId}).populate('reportedBy').exec(function(err6,threadFound){
        if(err6 !== undefined && err6) {
            response.status = "E2"
            response.errors.push(err6);
            callback(response);
        }
        if(threadFound === undefined){
            response.status = "E2";
            response.errors.push("Thread not found");
            callback(response);
        }
        else{
            threadFound.numberReports = threadFound.numberReports + 1;
            threadFound.reportedBy.add(email);
            threadFound.save(function(err){
                if(err){ 
                    response.status = "E2"
                    response.errors.push(err);
                    callback(response);    
                 }
            
                else {
                    if(threadFound.numberReports > 20 && (threadFound.numberReports > threadFound.numberVotes)){
                        return destroyThread(threadId,function(response){
                            callback(response);
                        });
                    }
                    else callback(response);
                    
                }
            });
        }
    });
}



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
            postedBy: userMail
        }).exec(function(err2, newThread){
            if(err2 !== undefined && err2){
                response.status = "E2";
                response.errors.push(err2);
                callback(response);
            }
            else{
                UtilsService.increaseUserKarma(100,userMail, function(result){
                    if(result === null){
                        response.status = 'E2';
                        response.errors.push("Unable to update user karma");
                    }
                });
                callback(response);
            }   
        });
    },
    deleteThread: function(threadId,callback){
         var deleted = destroyThread(threadId,function(response){
             callback(response);
         });
    },
    reportThread: function(id,email,callback){
        var response = {
            status: "E1",
            errors: []
         };
        reportAndEvaluateThread(id,email, function(response){
            callback(response);
        });
    },

    getThread: function(id,email,callback){
        var response = {
            status: "Ok",
            errors: [],
            info:{
                title:"",
                content:"",
                category:"",
                postedBy:"",
                username:"",
                rank:"",
                profilePicture:"",
                createdAt:"",
                votes:"",
                canVote:"false",
                canReport:"false"
            }
         };
        
        Thread.findOne({id:id}).populate('reportedBy',{where:{email:email}}).populate('votedBy',{where:{email:email}}).exec(function(err2,threadFound){
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(threadFound){
                var userHasVoted = _.find(threadFound.votedBy,{email:email});
                if(userHasVoted == undefined) response.info.canVote = "true";
                var userHasReported = _.find(threadFound.reportedBy, {email:email});
                if(userHasReported == undefined) response.info.canReport = "true";
                response.info.postedBy = threadFound.postedBy;
                response.info.votes = threadFound.numberVotes;
                response.info.createdAt = threadFound.createdAt;
                response.info.title = threadFound.title;
                response.info.content = threadFound.content;
                response.info.category = threadFound.category;
                
                User.findOne({email: threadFound.postedBy}).exec(function(err2,userOwner){
                    if(err2 !== undefined && err2) {
                        response.status = "E2";
                        response.errors.push("Unable to find the user owner of the thread");
                    }
                    if(userOwner){
                        response.info.username = userOwner.username;
                        response.info.rank = userOwner.rank;
                        response.info.profilePicture = userOwner.profilePicture;
                    }
                    callback(response);
                });
                
            }
            else{
                response.status = "Error"
                response.errors.push("Couldn't find the thread");
                callback(response);
            }
        });
    },

    voteThread: function(id, email, callback){
        var response = {
            status: "Ok",
            errors: []
         };

         User.findOne({email: email}).populate("votes")
         .then(function(user){
            if(user === undefined){
                response.status = "E2";
                response.errors.push("Couldn't find the user.");
                callback(response);
            }
            else{
                Thread.findOne({id: id}).populate("votedBy")
                .then(function(thread){
                    if(thread === undefined){
                        response.status = "E3";
                        response.errors.push("Couldn't find the thread.");
                        callback(response);
                    }
                    else{
                        var found = _.chain(user.votes).pluck("id").indexOf(id).value();
                        if(found == -1){
                            user.votes.add(id);
                            thread.numberVotes = thread.numberVotes + 1;
                            thread.votedBy.add(user.id);
                            user.save(function(err){
                                thread.save(function(err2){
                                    response.status = "Ok";
                                    callback(response);
                                });
                            });
                        }
                        else{
                            response.status = "E4";
                            response.errors.push("The user has already voted that thread.");
                            callback(response);
                        }
                    }
                })
                .catch(function(error){
                    response.status = "E1";
                    response.errors.push("Server error.");
                    callback(response);
                });
            }
         })
         .catch(function(error){
            response.status = "E1";
            response.errors.push("Server error.");
            callback(response);
         })
    }

}