/**
 * UserController
 *
 * @description :: Server-side logic for managing users
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {

	registerAll: function(req,res){
        var {username, password, email, name, surname, birthday, country, facebook, google} = req.query;
        
        var hasFace = (facebook==='true');
        var hasGoogle = (google === 'true');

        Register.register(username, password, email, name, surname, birthday, country, hasFace, hasGoogle, function(status){
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
        var accessToken = req.param('idToken');
        googleAuth.loginViaGoogle(accessToken, function(status){
            res.send(status);
        });
    },

    registerFacebook: function(req, res){
        //var facebookAuth = new FacebookAuth();
        var accessToken = req.param('accessToken');
        FacebookAuthService.facebookRegister(accessToken, function(status){
            res.send(status);
        });
    }
};

