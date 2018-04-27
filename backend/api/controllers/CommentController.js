
module.exports = {
    reportComment: function(req, res){
        var id = req.body.id;
        var email = UtilsService.getEmailFromHeader(req);
        CommentService.reportComment(id, email, function(status){
            res.send(status);
        });
    }
}