
function recalculatePuntuation(faqId, callback){
  var response = {
    status: "Ok",
    errors: []
  }

  Faq.findOne({id: faqId}).exec(function(err, FAQFound){
    if(err && err !== undefined){
      response.status = "E1";
      response.errors.push("Server error");
      callback(response);
    }
    else if(FAQFound === undefined){
      response.status = "E2";
      response.errors.push("There's no FAQ with the faqID given");
      callback(response);
    }
    else{

    }
  })

  }

}

module.exports = {

    createFaq(category,question,answer,callback){
        var response = {
            status: "Ok",
            errors: []
         };
        Faq.create({
            category: category,
            question: question,
            answer: answer
        }).exec(function(err2, newFaq){
            if(err2 !== undefined && err2){
                response.status = "E2";
                response.errors.push(err2);
            }
            callback(response);
            }
        );
    },
    deleteFaq(faqId,callback){
        var response = {
            status: "Ok",
            errors: []
         };
         Faq.destroy({id: faqId}).exec(function (err2,destroyedFaq) {
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(destroyedFaq<=0){
                response.status = "Error";
                response.errors.push("No FAQ matching the given id");
            }
            callback(response);
        });
    },
    getFaqs(callback){
        var response = {
            status: "Ok",
            errors: [],
            info: []
        };

        Faq.find({})
        .then(faqs => {
            console.log(faqs);
            response.info = faqs;
            callback(response);
        })
        .catch(err => {
            response.status = "E0";
            errors.push("Server error");
            callback(response);
        })
    },

    getAllFaqCategory(category,callback){
        var response = {
            status: "Ok",
            errors: [],
            faqs:[]
         };
        Faq.find({category: category}).sort('question ASC').exec(function(err2,faqsFound){
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(faqsFound.length>0){
                faqsFound.forEach(faq => {
                    var faqBrief= {
                        question:"",
                        answer:"",
                        puntuation: "",
                        id:""
                    };
                    faqBrief.question = faq.question;
                    faqBrief.answer = faq.answer;
                    faqBrief.puntuation = faq.puntuation;
                    faqBrief.id = faq.id;
                    response.faqs.push(faqBrief);
                });
            }
            else{
                response.status = "Error";
                response.errors.push("No faq found for the given category");
            }
            callback(response);
        });
    },

    reportFaq: function(faqId, email, callback){
        var response = {
            status: "Ok",
            errors: [],
        };
        User.findOne({email: email}).populate("reportedFaq").exec(function (err, user){
            if((err && err !== undefined) || user == undefined) {
                response.status = "E1";
                response.errors.push("There is no user with that id.");
                callback(response);
            }
            else{
                Faq.findOne({id: faqId}).populate("reportedBy").exec(function(err1, faq){
                    if((err1 && err1 !== undefined) || faq == undefined) {
                        response.status = "E2";
                        response.errors.push("There is no faq with that id.");
                        callback(response);
                    }
                    else{
                        var found = _.chain(user.reportedFaq).pluck("id").indexOf(faqId).value();
                        if(found === -1){
                            const MAX_REPORTS = 10;
                            if(_.size(faq.reportedBy) + 1 >= MAX_REPORTS){
                                faq.content = "This faq has been deleted.";
                            }
                            user.reportedFaq.add(faqId);
                            user.save(function(err){
                                faq.reportedBy.add(user.id);
                                faq.numberReports = faq.numberReports + 1;
                                faq.save(function(err){
                                    callback(response);
                                });
                            });
                        }
                        else{
                            response.status = "E3";
                            response.errors.push("The user has already reported that faq.");
                            callback(response);
                        }
                    }
                });
            }
        })
    },

    valorateFaq: function(faqId, email, valoration, callback){
      var response = {
        status: "Ok",
        errors: ""
      };
      var recalculate = false;
      valorationsFaq.findOne({user: email, faq: faqId}).exec(function(err, valorationFound){
        if(err && err !== undefined){
          response.status = "E1";
          response.errors.push("Server error");
          callback(response);
        }
        else if(valorationFound === undefined){
          valorationsFaq.create({faq: faqId, user: email, valoration: valoration}).exec(function(err1,valorationCreated){
            if(err1 && err1 !== undefined){
              response.status = "E1";
              response.errors.push("Server error");
              callback(response);
            }
            else{
              recalculate = true;
              callback(response);
            }
          });
        }
        else{
          valorationFound.valoration = valoration;
          valorationFound.save(function(err2){
            if(err2 && err2 !== undefined){
              response.status = "E1";
              response.errors.push("Server error");
            }
            else{
              recalculate = true;
            }
            callback(response);
          });
        }
      });
      if(recalculate){

      }
    }

}
