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
      res.view("main", {
        layout: 'defaultLayout'
      });
  },

  renderLoginPage: function(req, res){
    if(req.session.authenticated) res.redirect("/main");
    else res.view("login", {
      layout: 'loginLayout'
    });
  },

  renderAllInfoPage: function(req, res){
    Information.find({}).sort("category")
    .then(infos => {
      res.view("allInfo", {
        infos: infos,
        layout: 'defaultLayout'
      })
    })
  },

  renderNewInfoPage: function(req, res){
    res.view("newInfo", {
      layout: 'defaultLayout'
    });
  },

  deleteInfos: function(req, res){
    let ids = req.body.id;
    let infos;
    if(typeof(ids) == "string"){
      infos = ids;
    }
    else{
      infos = ids.map(i => {
        return parseInt(i)
      })
    }
    
    Information.destroy(infos)
    .then(() => {
      res.redirect("/AllInfo");
    })
    .catch(e => {
      console.log(e);
      res.send(500);
    })
    
  }
};

