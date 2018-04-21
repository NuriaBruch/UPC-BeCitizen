/**
 * UserController
 *
 * @description :: Server-side logic for managing users
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {

	register: function(req,res){
        var {username, password, email, name, surname, birthday, country, facebook, google} = req.body;
        
        var hasFace = (facebook ==='true');
        var hasGoogle = (google === 'true');
        var gestionUser = new GestionUser();

        gestionUser.register(username, password, email, name, surname, birthday, country, hasFace, hasGoogle, function(status){
            res.send(status);
        });
    },

    loginMail: function(req, res){
        var id = req.query.email;
        var password = req.query.password;

        var mailAuth = new MailAuth();
        mailAuth.login(id, password, function(status){
            if(status.status === 'Ok') {
                var jwt = require('jsonwebtoken');
                jwt.sign({ email: status.info.email }, "bienquesito", function(err, token) {
                    res.set("token", token);
                    res.send(status);
                });

            }
            else res.send(status);
        });
    },
    loginGoogle: function(req,res){
        var googleAuth = new GoogleAuth();
        var accessToken = req.param('idToken');
        googleAuth.loginViaGoogle(accessToken, function(status){
            if(status.status === 'Ok') {
                req.session.authenticated = true;
                req.session.userEmail = status.info.email;
            }
            res.send(status);
        });
    },

    loginFacebook: function(req, res){
        var accessToken = req.param('accessToken');
        FacebookAuthService.loginFace(accessToken, function(status){
            if(status.status === 'Ok') {
                req.session.authenticated = true;
                req.session.userEmail = status.info.email;
            }
            res.send(status);
        });
    },

    existsEmail: function(req,res){
        var{email} = req.query;
        var gestionUser = new GestionUser();
        gestionUser.checkMail(email,function(status){
            res.send(status);
        });
    },

    deactivateAccount: function(req,res){
        var username = req.body.username;
        var gestionUser = new GestionUser();
        gestionUser.deactivate(username,function(status){
            res.send(status);
        });
    }
};

