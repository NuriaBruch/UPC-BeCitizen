
module.exports = {
    reportComment: function(req, res){
        var id = parseInt(req.query.commentId);
        var email = UtilsService.getEmailFromHeader(req);
        CommentService.reportComment(id, email, function(status){
            res.send(status);
        });
    },

    createComment: function(req,res){
        var email = UtilsService.getEmailFromHeader(req);
        var {content,commentReplyId,threadId} = req.body;
        CommentService.createComment(email, content, commentReplyId, threadId,function(status){
            res.send(status);
        })
    },

    getThreadComments: function(req,res){
        var id = req.query.threadId;
        var sortedByVotes = req.query.sortedByVotes;
        var email = UtilsService.getEmailFromHeader(req);
        CommentService.getThreadComments(id,email,sortedByVotes,function(status){
            res.send(status);
        });
    },

    voteComment: function(req, res){
        var id = parseInt(req.query.commentId);
        var email = UtilsService.getEmailFromHeader(req);
        CommentService.voteComment(id, email,function(status){
            res.send(status);
        });
    }
}