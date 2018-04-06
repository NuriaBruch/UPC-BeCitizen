/**
 * UserController
 *
 * @description :: Server-side logic for managing users
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {

	register: function(req,res){
        var {username, password, email, name, surname, birthday, country, facebook, google} = req.body;
        
        var hasFace = (facebook==='true');
        var hasGoogle = (google === 'true');
        var gestionUser = new GestionUser();

        gestionUser.register(username, password, email, name, surname, birthday, country, hasFace, hasGoogle, function(status){
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
        //var facebookAuth = new FacebookAuth();
        var accessToken = req.param('accessToken');
        FacebookAuthService.loginFace(accessToken, function(status){
            res.send(status);
        });
    },

    existsEmail: function(req,res){
        var{email} = req.query;
        var gestionUser = new GestionUser();
        gestionUser.checkMail(email,function(status){
            res.send(status);
        });
    }
};

