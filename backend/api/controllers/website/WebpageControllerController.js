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
    Faq.find({}).populate("reportedBy")
    .then(faqs => {
      let faqsWithReports = faqs.map(f => {
        return {
          question: f.question,
          reports: f.numberReports,
          id: f.id
        }
      })
      .sort((a, b) => b.reports - a.reports)
      .slice(0, 6);

      console.log(faqsWithReports);
      res.view("main", {
        layout: 'defaultLayout',
        faqsWithReports: JSON.stringify(faqsWithReports)
      });
    })
    .catch(e => {
      console.log(e);
      res.view("main", {
        layout: 'defaultLayout',
        faqsWithReports: undefined
      });
    })

    
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

  renderAllFaqsPage: function(req, res){
    var id = req.query.id;
    if(id != undefined){
      Faq.findOne({id: id}).
      then(faq => {
        if(faq == undefined) res.notFound();
        else{
          ThreadService.getAllCategories(function(status){
            let categories = status.categories;
            res.view("editFaq", {
              layout: 'defaultLayout',
              categories: categories,
              faq: faq
            });
          });
        }
      })
    }
    else{
      Faq.find({}).sort("category")
      .then(faqs => {
        res.view("allFaqs", {
          faqs: faqs,
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
  },

  renderAddFaqPage: function(req, res){
    ThreadService.getAllCategories(function(status){
      let categories = status.categories;
      res.view("addFaq", {
        layout: 'defaultLayout',
        categories: categories
      });
    })
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

  deleteFaqs: function(req, res){
    let ids = req.body.id;
    let faqs;
    if(typeof(ids) == "string"){
      faqs = ids;
    }
    else{
      faqs = ids.map(i => {
        return parseInt(i)
      })
    }
    
    Faq.destroy(faqs)
    .then(() => {
      res.redirect("/AllFaqs");
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

  addFaq: function(req, res){
    var {category,question,answer} = req.body;

    FaqService.createFaq(category,question,answer,function(status){
      if(status.status == "Ok")
        res.redirect("/allFaqs");
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
  },

  editFaq: function(req, res){
    var {id,category,question, answer} = req.body;
    Faq.update({id: id}, {
      category: category,
      question: question,
      answer: answer
    })
    .then(faq => {
      res.redirect("/allFaqs");
    })
    .catch(e => {
      console.log(e)
    });
  }
};

