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

    registerGoogle: function(req, res){
        var googleAuth = new GoogleAuth();
        var accessToken = req.param('accessToken');
        googleAuth.registerViaGoogle(accessToken,function(status){
            res.send(status);
        });
    },

    registerFacebook: function(req, res){
        //var facebookAuth = new FacebookAuth();
        res.ok();
    },

     /* He supuesto que en el login nos mandan un id
      que puede ser email o username y la contraseña*/
    findUserMail: function(req,res){
        var id = req.param('id');
        var pass = req.param('password');
        User.find({ 
            or: [
                { email: id, password: pass },
                { username: id, password: pass}
            ]
        }).exec(function(err,userFound){
            res.send({user: userFound});
        });
    }


};

