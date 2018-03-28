/**
 * UserController
 *
 * @description :: Server-side logic for managing users
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
	registerMail: function(req,res){
        var {username, password, email} = req.query;
        var mailAuth = new MailAuth();
        mailAuth.register(username, password, email, function(status){
            res.send(status);
        });
    },

    loginMail: function(req, res){
        var {email, password} = req.query;
        var mailAuth = new MailAuth();
        mailAuth.login(email, password, function(status){
            res.send(status);
        });
    },
    loginGoogle: function(req,res){
        var googleAuth = new GoogleAuth();
        var accessToken = req.param('accessToken');
        googleAuth.loginViaGoogle(accessToken, function(status){
            res.send(status);
        });
    },

    registerFacebook: function(req, res){
        //var facebookAuth = new FacebookAuth();
        res.ok();
    }
};

