/**
 * UserController
 *
 * @description :: Server-side logic for managing users
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {

	register: function(req,res){
        var {username, password, email, name, surname, birthday, country, profilePicture, facebook, google} = req.body;
        
        var hasFace = (facebook ==='true');
        var hasGoogle = (google === 'true');
        var gestionUser = new GestionUser();

        gestionUser.register(username, password, email, name, surname, birthday, country, profilePicture, hasFace, hasGoogle, function(status){
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
                var jwt = require('jsonwebtoken');
                jwt.sign({ email: status.info.email }, "bienquesito", function(err, token) {
                    res.set("token", token);
                    res.send(status);
                });
            }
            else res.send(status);
        });
    },

    loginFacebook: function(req, res){
        var accessToken = req.param('accessToken');
        FacebookAuthService.loginFace(accessToken, function(status){
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

    existsEmail: function(req,res){
        var{email} = req.query;
        var gestionUser = new GestionUser();
        gestionUser.checkMail(email,function(status){
            res.send(status);
        });
    },

    deactivateAccount: function(req,res){
        var gestionUser = new GestionUser();
        var userMail = UtilsService.getEmailFromHeader(req);

        gestionUser.deactivate(userMail,function(status){
            res.send(status);
        });
    },

    updateProfile: function(req,res){
        var gestionUser = new GestionUser();
        
        gestionUser.update(req,function(status){
            res.send(status);
        });
    },

    viewProfile: function(req, res){
        var gestionUser = new GestionUser();

        var username = req.query.username;
        
        gestionUser.view(username,function(status){
            res.send(status);
        });
    },
    reportUser: function(req, res){
        var reporter = req.body.reporterEmail;
        var reported = req.body.reportedEmail;
        sails.log(reporter);
        sails.log(reported);
        var gestionUser = new GestionUser();
        gestionUser.report(reporter,reported,function(status){
            res.send(status);
        });
    }
};

