

module.exports = {
    reportComment: function(commentId, email, callback){
        var response = {
            status: "Ok",
            errors: [],
        };
        User.findOne({email: email}).populate("reportedComments").exec(function (err, user){
            if((err && err !== undefined) || user == undefined) {
                status = "E1";
                response.errors.push("No user");
                callback(response);
            }
            else{
                Comment.findOne({id: commentId}).populate("reportedBy").exec(function(err1, comment){
                    if((err1 && err1 !== undefined) || comment == undefined) {
                        status = "E2";
                        response.errors.push("No comment with that id");
                        callback(response);
                    }
                    else{
                        user.reportedComments.add(commentId);
                        user.save(function(err){
                            comment.reportedBy.add(user.id);
                            comment.save(function(err){
                                callback(response);
                            });
                        });
                    } 
                });
            }
        })
    }
}