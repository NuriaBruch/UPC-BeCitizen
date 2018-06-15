/**
 * ThreadController
 *
 * @description :: Server-side logic for managing Threads
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {

    getAllCategories: function(req,res){
        ThreadService.getAllCategories(function(status){
            res.send(status);
        });
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
        if(email == undefined){
          ThreadService.getThreadGuest(id,function(status){
            res.send(status);
          });
        }
        else{
          ThreadService.getThread(id,email,function(status){
            res.send(status);
          });
        }

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
    },

    getThreadWords: function(req,res){
        var block = req.query.block;
        var category = req.query.category;
        var sortedByVotes = req.query.sortedByVotes;
        var words = req.query.words;
        var email = UtilsService.getEmailFromHeader(req);
        ThreadService.getThreadWords(words,email,block,category,sortedByVotes,function(status){
            res.send(status);
        });
    }

};

