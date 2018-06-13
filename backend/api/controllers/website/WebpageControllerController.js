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
      res.forbidden();
    }
  },

  logout: function(req, res){
    req.session.authenticated = false;
    res.redirect("/");
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
    var id = req.query.id;
    if(id != undefined){
      Information.findOne({id: id}).
      then(info => {
        if(info == undefined) res.notFound();
        else{
          ThreadService.getAllCategories(function(status){
            let categories = status.categories;
            res.view("editInfo", {
              layout: 'defaultLayout',
              categories: categories,
              info: info
            });
          });
        }
      })
    }
    else{
      Information.find({}).sort("category")
      .then(infos => {
        res.view("allInfo", {
          infos: infos,
          layout: 'defaultLayout'
        })
      })
    }
  },

  renderAddInfoPage: function(req, res){
    ThreadService.getAllCategories(function(status){
      let categories = status.categories;
      res.view("addInfo", {
        layout: 'defaultLayout',
        categories: categories
      });
    })
    // res.view("AddInfo", {
    //   layout: 'defaultLayout'
    // });
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
  },

  addInfo: function(req, res){
    var {category,title,content,url,type} = req.body;

    InfoService.createInfo(category,title,content,url,type,function(status){
      if(status.status == "Ok")
        res.redirect("/allInfo");
    });
  },

  editInfo: function(req, res){
    var {id,category,title,content,url,type} = req.body;
    Information.update({id: id}, {
      category: category,
      title: title,
      content: content,
      url: url,
      type: type
    })
    .then(info => {
      res.redirect("/allInfo");
    })
    .catch(e => {
      console.log(e)
    });
  }
};

