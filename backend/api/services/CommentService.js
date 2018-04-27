

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
    }
}