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
                        var userMail = threadFound.postedBy;
                        UtilsService.increaseUserKarma(-100,userMail, function(result){
                            if(result === null){
                                response.status = 'E2';
                                response.errors.push("Unable to update user karma");
                            }
                            else {
                                return destroyThread(threadId,function(response){
                                    callback(response);
                                });
                            }
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
        
         Thread.findOne({id:id}).populate('reportedBy',{where:{email:email}}).populate('postedBy').populate('votedBy',{where:{email:email}}).exec(function(err2,threadFound){
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(threadFound){
                var userHasVoted = _.find(threadFound.votedBy,{email:email});
                if(userHasVoted == undefined) response.info.canVote = "true";
                var userHasReported = _.find(threadFound.reportedBy, {email:email});
                if(userHasReported == undefined) response.info.canReport = "true";
                response.info.postedBy = threadFound.postedBy.email;
                response.info.votes = threadFound.numberVotes;
                response.info.createdAt = threadFound.createdAt;
                response.info.title = threadFound.title;
                response.info.content = threadFound.content;
                response.info.category = threadFound.category;
                response.info.username = threadFound.postedBy.username;
                response.info.rank =  threadFound.postedBy.rank;
                response.info.profilePicture =  threadFound.postedBy.profilePicture;
                callback(response);
            }
            else{
                response.status = "Error";
                response.errors.push("Couldn't find the thread");
                callback(response);
            }
        });
    },

    getThreadWords: function(words,email,block,category,sortedByVotes,callback){
        var limit = 10;
        block++; // los blocks 0 y 1 son los mismos, dejemos que front empieze a iterar con block=0
        var response = {
            status: "Ok",
            errors: [],
            threads:[]
         };
        if(UtilsService.isEmptyOrBlank(words)){
            response.status="E1"
            response.errors.push("No words for search in threads titles");
            callback(response);
        }
        Thread.count({category: category,title:{ contains: words}}).exec(function(err,numThreads){
            if(numThreads==0){
                response.status = "E4";
                response.errors.push("There aren't threads matching");
                callback(response);
            }else
            if(block >Math.ceil(numThreads/limit)){
                response.status = "E1";
                response.errors.push("Block out of bound");
                callback(response);
            }
            else if( block<=0){
                response.status = "E3";
                response.errors.push("Block should be positive, like you when reading this error");
                callback(response);
            }
            else{
            var orderBy;
            if(sortedByVotes === 'true') orderBy= 'numberVotes DESC';
            else orderBy='createdAt DESC';
            words.split('+').join(' ');
            Thread.find({category: category,title:{ contains: words}}).sort(orderBy).populate('reportedBy',{where:{email:email}}).populate('postedBy').populate('votedBy',{where:{email:email}}).exec(function(err2,threadsFound){
                if(err2 !== undefined && err2) {
                    response.status = "E2";
                    response.errors.push(err2);
                }
                if(threadsFound.length>0){
                    threadsFound.forEach(thread => {
                        var threadInfo = {
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
                        };
                        var userHasVoted = _.find(thread.votedBy,{email:email});
                        if(userHasVoted == undefined) threadInfo.canVote = "true";
                        var userHasReported = _.find(thread.reportedBy, {email:email});
                        if(userHasReported == undefined) threadInfo.canReport = "true";
                        threadInfo.title = thread.title;
                        threadInfo.content = thread.content;
                        threadInfo.category = thread.category;
                        threadInfo.postedBy = thread.postedBy.email;
                        threadInfo.username = thread.postedBy.username;
                        threadInfo.rank = thread.postedBy.rank;
                        threadInfo.profilePicture = thread.postedBy.profilePicture;
                        threadInfo.createdAt = thread.createdAt;
                        threadInfo.votes = thread.numberVotes;
                        response.threads.push(threadInfo);
                    });
                    callback(response);
                }
                else{
                    response.status = "Error";
                    response.errors.push("Couldn't find the thread");
                    callback(response);
                }
            });
            }
        });

    },

    getAllThreadsCategory: function(block,category,sortedByVotes,callback){
        var limit = 10;
        block++; // los blocks 0 y 1 son los mismos, dejemos que front empieze a iterar con block=0
        var response = {
            status: "Ok",
            errors: [],
            threads:[]
         };
        Thread.count({category: category}).exec(function(err,numThreads){
            if(numThreads==0){
                response.status = "E4";
                response.errors.push("There aren't threads for this category");
                callback(response);
            }else
            if(block >Math.ceil(numThreads/limit)){
                response.status = "E1";
                response.errors.push("Block out of bound");
                callback(response);
            }
            else if( block<=0){
                response.status = "E3";
                response.errors.push("Block should be positive, like you when reading this error");
                callback(response);
            }
            else{
                var orderBy;
                if(sortedByVotes === 'true') orderBy= 'numberVotes DESC';
                else orderBy='createdAt DESC';
                Thread.find({category: category}).sort(orderBy).populate('postedBy').paginate({page: block, limit: limit}).exec(function(err2,threadsFound){
                    if(err2 !== undefined && err2) {
                        response.status = "E2";
                        response.errors.push(err2);
                    }
                    if(threadsFound.length >0){
                        threadsFound.forEach(thread => {
                            var threadInfo = {
                                title:"",
                                votes:"",
                                id:"",
                                username:"",
                                createdAt: ""
                            };
                            threadInfo.createdAt = thread.createdAt;
                            if(thread.postedBy)
                            threadInfo.username = thread.postedBy.username;
                            threadInfo.id = thread.id;
                            threadInfo.title = thread.title;
                            threadInfo.votes = thread.numberVotes; 
                            response.threads.push(threadInfo);
                        });
                    }
                    else{
                        response.status = "Error";
                        response.errors.push("There aren't more threads");
                    }
                    callback(response);
                });
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
         });
    }

}