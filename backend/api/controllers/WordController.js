
module.exports = {
    getWord: function(req, res){
        new WordService().getWord(function(status) {
            res.send(status);
        })
    }
}