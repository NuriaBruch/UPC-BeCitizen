

module.exports = {
    reportComment: function(commentId, email, callback){
        var response = {
            status: "Ok",
            errors: [],
        };
        User.findOne({email: email}).populate("reportedComments").exec(function (err, user){
            if((err && err !== undefined) || user == undefined) {
                response.status = "E1";
                response.errors.push("No user");
                callback(response);
            }
            else{
                Comment.findOne({id: commentId}).populate("reportedBy").exec(function(err1, comment){
                    if((err1 && err1 !== undefined) || comment == undefined) {
                        response.status = "E2";
                        response.errors.push("No comment with that id");
                        callback(response);
                    }
                    else{
                        megaUser = _.chain(user.toJSON());
                        var found = megaUser.pluck('reportedComments').indexOf(commentId).value();
                        console.log(found);
                        //found = 0;
                        if(found === -1){
                            if(comment.reportedBy.lenght > 0){ // <--
                                comment.content = "This comment has been removed";
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
                            response.errors.push("The user has reported that comment");
                            callback(response);
                        }
                    
                    } 
                });
            }
        })
    }
}