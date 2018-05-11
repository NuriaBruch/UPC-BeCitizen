

module.exports = {
    reportComment: function(commentId, email, callback){
        var response = {
            status: "Ok",
            errors: [],
        };
        User.findOne({email: email}).populate("reportedComments").exec(function (err, user){
            if((err && err !== undefined) || user == undefined) {
                response.status = "E1";
                response.errors.push("There is no user with that id.");
                callback(response);
            }
            else{
                Comment.findOne({id: commentId}).populate("reportedBy").exec(function(err1, comment){
                    if((err1 && err1 !== undefined) || comment == undefined) {
                        response.status = "E2";
                        response.errors.push("There is no comment with that id.");
                        callback(response);
                    }
                    else{
                        var found = _.chain(user.reportedComments).pluck("id").indexOf(commentId).value();
                        if(found === -1){
                            const MAX_REPORTS = 10;
                            if(_.size(comment.reportedBy) + 1 >= MAX_REPORTS){
                                comment.content = "This comment has been deleted.";
                            }
                            user.reportedComments.add(commentId);
                            user.save(function(err){
                                comment.reportedBy.add(user.id);
                                comment.numberReports = comment.numberReports + 1;
                                comment.save(function(err){
                                    callback(response);
                                });
                            });
                        }
                        else{
                            response.status = "E3";
                            response.errors.push("The user has already reported that comment.");
                            callback(response);
                        }
                    } 
                });
            }
        })
    },

    createComment: function(email, content, commentReplyId, threadId, callback){
        var response = {
            status: "Ok",
            errors: []
         };
        Comment.create({
            content:content,
            postedBy: email,
            repliesTo: commentReplyId,
            belongsTo: threadId
        }).exec(function(err2, newComment){
            if(err2 !== undefined && err2){
                response.status = "E2";
                response.errors.push(err2);
                callback(response);
            }
            else{
                UtilsService.increaseUserKarma(10,email, function(result){
                    if(result === null){
                        response.status = 'E2';
                        response.errors.push("Unable to update user karma");
                    }
                });
                callback(response);
            }   
        });
    },
    
    getThreadComments: function(threadId,email,sortedByVotes,callback){
        var response = {
            status: "Ok",
            errors: [],
            comments: []
        };
        Comment.find({belongsTo:threadId}).populate('reportedBy',{where:{email:email}}).populate('votedBy',{where:{email:email}}).exec(function(err2,commentsFound){
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
                callback(response);
            }
            if(commentsFound.length>0){
                async.each(commentsFound,function(comment,eachCb){
                    var commentInfo = {
                        content:"",
                        postedBy:"",
                        username:"",
                        rank:"",
                        profilePicture:"",
                        createdAt:"",
                        votes:"",
                        canVote:"false",
                        canReport:"false",
                        id: ""
                    };
                    var userHasVoted = _.find(comment.votedBy,{email:email});
                            if(userHasVoted == undefined) commentInfo.canVote = "true";
                            var userHasReported = _.find(comment.reportedBy, {email:email});
                            if(userHasReported == undefined) commentInfo.canReport = "true";
                            commentInfo.postedBy = comment.postedBy;
                            commentInfo.votes = comment.numberVotes;
                            commentInfo.createdAt = comment.createdAt;
                            commentInfo.content = comment.content;
                            commentInfo.id = comment.id;
                    User.findOne({email: comment.postedBy}).exec(function(err2,userOwner){
                        if(err2 !== undefined && err2) {
                            response.status = "E2";
                            response.errors.push(err2);
                            callback(response); //error en 1 comment ya es un error final;
                        }
                        if(userOwner){
                            commentInfo.username = userOwner.username;
                            commentInfo.rank = userOwner.rank;
                            commentInfo.profilePicture = userOwner.profilePicture;
                            response.comments.push(commentInfo);
                            eachCb();
                        }
                        else{
                            /*para devolver comments aunq no econtremos su usuario(eliminado o algo)
                            simplemente cambiar callback por eachCb y gestionarlo en el tercer param*/
                            response.status = "Error"
                            response.errors.push("Couldn't find the owner of a comment");
                            callback(response);
                        }
                    });
                },function(){
                    var sortedList  = _.sortBy(response.comments, function(l){
                        if(sortedByVotes === 'true') return -l.votes;
                        return -l.createdAt;
                    });
                    response.comments = sortedList;
                    callback(response);
                });
            }
            else{
                response.status = "Error"
                response.errors.push("Couldn't find comments for this thread");
                callback(response);
            }
        });
    },

    voteComment: function(id, email, callback){
        var response = {
            status: "Ok",
            errors: []
         };

         User.findOne({email: email}).populate("votedComments")
         .then(function(user){
            if(user === undefined){
                response.status = "E2";
                response.errors.push("Couldn't find the user.");
                callback(response);
            }
            else{
                Comment.findOne({id: id}).populate("votedBy")
                .then(function(comment){
                    if(comment === undefined){
                        response.status = "E3";
                        response.errors.push("Couldn't find the comment.");
                        callback(response);
                    }
                    else{
                        var found = _.chain(user.votedComments).pluck("id").indexOf(id).value();
                        if(found == -1){
                            user.votedComments.add(id);
                            comment.numberVotes = comment.numberVotes + 1;
                            comment.votedBy.add(user.id);
                            user.save(function(err){
                                comment.save(function(err2){
                                    response.status = "Ok";
                                    callback(response);
                                });
                            });
                        }
                        else{
                            response.status = "E4";
                            response.errors.push("The user has already voted that comment.");
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