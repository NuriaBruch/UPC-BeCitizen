/**
 * ThreadController
 *
 * @description :: Server-side logic for managing Threads
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
    
    getAllCategories: function(req,res){
        var response = {
            status: "Ok",
            errors: [],
            categories: [ "culture", "education and formation",
        "emergencies", "language", "justice", "public administration", "housing",
        "health", "work", "tourism", "off topic"]
        }

        res.send(response);
    },

    createThread: function(req,res){
        var userMail = UtilsService.getEmailFromHeader(req);
        var {title, content, category} = req.body;
        ThreadService.createThread(userMail,title,content,category,function(status){
            res.send(status);
        });
    },

    deleteThread: function(req,res){
        var id = req.query.threadId;
        ThreadService.deleteThread(id,function(status){
            res.send(status);
        });
    },

    reportThread: function(req,res){
        var id = req.query.threadId;
        var email = UtilsService.getEmailFromHeader(req);
        ThreadService.reportThread(id,email,function(status){
            res.send(status);
        });
    },

    getThread: function(req,res){
        var id = req.query.threadId;
        var email = UtilsService.getEmailFromHeader(req);
        ThreadService.getThread(id,email,function(status){
            res.send(status);
        });
    },

    getAllThreadsCategory: function(req,res){
        var block = req.query.block;
        var category = req.query.category;
        var sortedByVotes = req.query.sortedByVotes; 
        ThreadService.getAllThreadsCategory(block,category,sortedByVotes,function(status){
            res.send(status);
        });
    },
    
    voteThread: function(req, res){
        var id = parseInt(req.query.threadId);
        var email = UtilsService.getEmailFromHeader(req);
        ThreadService.voteThread(id, email,function(status){
            res.send(status);
        });
    }
};

