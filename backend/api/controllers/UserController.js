/**
 * UserController
 *
 * @description :: Server-side logic for managing users
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
	registerMail: function(req,res){
        var name = req.param('username');
        var pass = req.param('password');
        var mail = req.param('email');
        User.create({
            email: mail,
            username: name,
            password: pass,
        }).exec(function(err,user){
            if(!err) res.ok();
        });
    },

     /* He supuesto que en el login nos pueden mandar una pareja
           de username y pass o email y pass*/
    findUser: function(req,res){
        var mail = req.param('email');
        var pass = req.param('password');
        var name = req.param('username');
        User.find({ 
            or: [
                { email: mail, password: pass },
                { username: name, password: pass}
            ]
        }).exec(function(err,userFound){
            res.send({user: userFound});
        });
    }


};

