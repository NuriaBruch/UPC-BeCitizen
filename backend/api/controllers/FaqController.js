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
    }
	
};

