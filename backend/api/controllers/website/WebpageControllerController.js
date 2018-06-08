/**
 * WebpageControllerController
 *
 * @description :: Server-side actions for handling incoming requests.
 * @help        :: See https://sailsjs.com/docs/concepts/actions
 */

module.exports = {
  login: function(req, res){
    console.log("PASO");
    var {password} = req.body;
    if(password == "Bienquisto123H"){
      req.session.authenticated = true;
      res.view("adminWebpage");
    }
    else{
      res.view(403);
    }
  },

  renderMainPage: function(req,res){
      res.view("adminWebpage");
  },

  renderLoginPage: function(req, res){
    res.view("login");
  }

};

