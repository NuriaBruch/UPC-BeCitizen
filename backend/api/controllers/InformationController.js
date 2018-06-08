/**
 * InformationController
 *
 * @description :: Server-side logic for managing Information
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
	createInfo: function(req,res){
        var {category,title,content,url,type} = req.body;
        InfoService.createInfo(category,title,content,url,type,function(status){
            res.send(status);
        });
    },
    getAllInfoCategory: function(req,res){
        var category = req.query.category;
        InfoService.getAllInfoCategory(category,function(status){
            res.send(status);
        })
    },
    getInfo: function(req,res){
        var infoId = req.query.infoId;
        InfoService.getInfo(infoId,function(status){
            res.send(status);
        })
    },
    deleteInfo: function(req,res){
        var infoId = req.query.infoId;
        InfoService.deleteInfo(infoId,function(status){
            res.send(status);
        });
    },

    getInfoWords: function(req,res){
        var block = req.query.block;
        var category = req.query.category;
        var words = req.query.words;
        var email = UtilsService.getEmailFromHeader(req);
        InfoService.getInfoWords(words,email,block,category,function(status){
            res.send(status);
        });
    }
    
};

