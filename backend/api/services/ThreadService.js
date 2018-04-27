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
                        response.errors.push(err2);
                    }
                    if(userOwner){
                        response.info.username = userOwner.username;
                        response.info.rank = userOwner.rank;
                        callback(response);
                    }
                });
                
            }
        });
    }

}