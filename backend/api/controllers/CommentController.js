
module.exports = {
    reportComment: function(req, res){
        var id = req.query.id;
        var email = UtilService.getEmailFromHeader(req);
        CommentService.reportComment(id, email, function(status){
            res.send(status);
        });
    }
}