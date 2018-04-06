/**
 * UserController
 *
 * @description :: Server-side logic for managing users
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {

	registerAll: function(req,res){
        var {username, password, email, name, surname, birthday, country, facebook, google} = req.body;
        
        var hasFace = (facebook==='true');
        var hasGoogle = (google === 'true');
        var register = new Register();

        register.registerAll(username, password, email, name, surname, birthday, country, hasFace, hasGoogle, function(status){
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

    loginFacebook: function(req, res){
        var accessToken = req.param('accessToken');
        FacebookAuthService.loginFace(accessToken, function(status){
            res.send(status);
        });
    }
};

