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

function reportAndEvaluateThread(threadId,callback){
    var response = {
        status: "Ok",
        errors: []
     };
    Thread.findOne({id: threadId}).exec(function(err6,threadFound){
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

function increaseUserKarma(points,mail,callback){
    User.findOne({email: mail}).exec(function(err1, userFound){
        if(err1 !== undefined && err1) {
            callback(null);
        }
        if(userFound === undefined){
            callback(null);
        }
        else{
            userFound.karma = userFound.karma + points;
            userFound.save();
            callback(userFound.karma);
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
                increaseUserKarma(100,userMail, function(result){
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
    reportThread: function(id,callback){
        var response = {
            status: "E1",
            errors: []
         };
        reportAndEvaluateThread(id, function(response){
            callback(response);
        });
    },

    getThread: function(id,email,callback){
        var response = {
            status: "Ok",
            errors: [],
            info:{
                postedBy:"",
                createdAt:"",
                votes:"",
                canVote:"false",
                canReport:"false"
            }
         };
        
        Thread.find({id:id}).exec(function(err2,threadFound){
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(threadFound){
                threadFound.votedBy.find({email:email}).exec(function(err2,userVoted){
                    if(err2 !== undefined && err2) {
                        response.status = "E2";
                        response.errors.push(err2);
                    }
                    if(!userVoted) response.info.canVote = "true";
                    threadFound.postedBy.find({email:email}).exec(function(err2,userReported){
                        if(err2 !== undefined && err2) {
                            response.status = "E2";
                            response.errors.push(err2);
                        }
                        if(!userReported) response.info.canReport = "true";
                        response.info.postedBy = threadFound.postedBy;
                        response.info.votes = threadFound.numberVotes;
                    });
                });
                
            }
        });
    }

}