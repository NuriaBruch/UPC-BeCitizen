function destroyThread(threadId){
    Thread.destroy({id: threadId}).exec(function(err5){
        if(err5 !== undefined && err5){
            return false;
        }
        else return true;
    });
}

function reportAndEvaluateThread(threadId){
    Thread.findOne({id: threadId}).exec(function(err6,threadFound){
        if(err6 !== undefined && err6) {
            return false;
        }
        if(threadFound === undefined){
            return false;
        }
        else{
            threadFound.numberReports = threadFound.numberReports + 1;
            threadFound.save();
            if(threadFound.numberReports > 20 && (threadFound.numberReports > threadFound.numberVotes)){
                return destroyThread(threadId); ;
            }
            return false;
        }
    });
}

function increaseUserKarma(points,mail){
    User.findOne({email: mail}).exec(function(err1, userFound){
        if(err1 !== undefined && err1) {
            return null;
        }
        if(userFound === undefined){
            return null;
        }
        else{
            userFound.karma = userFound.karma + points;
            userFound.save();
            return userFound.karma;
        }
    });
}
module.exports = {

    createThread: function(userMail, title, content, category,callback){
        var response = {
            status: "Ok",
            errors: []
         };

        Thread.create({
            title: title,
            content:content,
            category:category,
            postedBy: userMail
        }).exec(function(err2, newThread){
            if(err2 !== undefined && err2){
                response.status = "E2";
                response.errors.push(err2);
            }
            else{
                var result = increaseUserKarma(100,userMail);
                if(result === null){
                    response.status = 'E4';
                    response.errors.push("unable to update user karma");
                }
            }
            callback(response);
        });
    },

    deleteThread: function(threadId,callback){
        var response = {
            status: "Ok",
            errors: []
         };
         var deleted = destroyThread(threadId);
         if(deleted){
            response.status = "E5";
            response.errors.push(err5);
         }
         callback(response);
    },
    reportThread: function(id,callback){
        var response = {
            status: "Ok",
            errors: []
         };
        var evaluation = reportAndEvaluateThread(id);
        if(evaluation){
            response.status = "E5";
            response.errors.push(err5);
        }
        callback(response);
    }

}