/**
 * WebpageControllerController
 *
 * @description :: Server-side actions for handling incoming requests.
 * @help        :: See https://sailsjs.com/docs/concepts/actions
 */

module.exports = {
  login: function(req, res){
    var {password} = req.body;
    if(password == "Bienquisto123H"){
      req.session.authenticated = true;
      res.redirect("/main");
    }
    else{
      res.view(403);
    }
  },

  renderMainPage: function(req,res){
      res.view("main");
  },

  renderLoginPage: function(req, res){
    if(req.session.authenticated) res.redirect("/main");
    else res.view("login");
  }

};

