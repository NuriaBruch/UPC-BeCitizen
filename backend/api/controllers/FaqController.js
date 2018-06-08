/**
 * FaqController
 *
 * @description :: Server-side logic for managing Faqs
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
    createFaq: function(req,res){
        var superpass = req.query.superpass;
        if(superpass === 'Bienquisto123H'){
            var {category,question,answer} = req.body;
            FaqService.createFaq(category,question,answer,function(status){
                res.send(status);
            })
        }
        else return res.badRequest();
    },
    deleteFaq: function(req,res){
        var superpass = req.query.superpass;
        if(superpass === 'Bienquisto123H'){
            var faqId = req.query.faqId;
            FaqService.deleteFaq(faqId,function(status){
                res.send(status);
            })
        }
        else return res.badRequest();
    },
    getFaqs: function(req, res){
        FaqService.getFaqs(function(status){
            res.send(status);
        })
    },
    getAllFaqCategory: function(req,res){   
        var category = req.query.category;
        FaqService.getAllFaqCategory(category,function(status){
            res.send(status);
        })
    },
    reportFaq: function(req, res){
        var id = parseInt(req.query.faqId);
        var email = UtilsService.getEmailFromHeader(req);
        FaqService.reportFaq(id, email, function(status){
            res.send(status);
        });
    },

	
};

