
module.exports = {
    getTranslation: function(req, res){
        let {text, from, to} = req.query;

        new UtilityService().translate(text, from, to, function(status) {
            res.send(status);
        })
    },

    getLanguageCodes: function(req, res){
        new UtilityService().getLanguageCodes()
        .then(status => res.send(status));
    }
}