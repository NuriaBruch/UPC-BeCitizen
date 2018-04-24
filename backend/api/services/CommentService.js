

module.exports = {
    reportComment: function(commentId, email, callback){
        var response = {
            status: "Ok",
            errors: [],
        };
        User.find({email: email}).exec(function (err, user){
            if((err && err !== undefined) || user == undefined) {
                status = "E1";
                response.errors.push("No user");
            }
            Comment.find({id: commentId}).exec(function(err1, comment){
                if((err && err !== undefined) || user == undefined) {
                    status = "E2";
                    response.errors.push("No comment with that id");
                }
                user.reportedComments.add(commentId);
                user.save();
                comment.reportingUsers.add(user.id);
                comment.save();
            });
        })
    }
}