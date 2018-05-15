
var jw = require('jsonwebtoken');
module.exports = {

    getRandomString: function (){
        var chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        var length = 32;
        var result = '';
        for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
        return result;
    },

    getUserName: function(name){
        answer = {
            namne: "",
            surname: ""
        }
        var result = name.split(" ", 2);
        answer.name = result[0];
        answer.surname = result[1];
        return answer;
    },

    getFormattedBirthday: function(birthday){
        var result = birthday.split("/", 3);
        return result[1]+"/"+result[0]+"/"+result[2];
    },

    getEmailFromHeader: (req) => jw.decode(req.get("token")).email,


    update_deactivated: function(userFound, callback){
        User.update({email:userFound.email},{deactivated:false}).exec(function(err1,userFound){
            callback(err1);
        });
    },

    updateRank: function(points){
        if(points <= 500) return "coal";
        else if(points <= 1000) return "bronze";
        else if(points <= 2000) return "silver";
        else if(points <= 4000) return "gold";
        else if(poits <= 8000) return "platinum";
        else return "diamond";
    },

    increaseUserKarma: function(points,mail,callback){
        User.findOne({email: mail}).exec(function(err1, userFound){
            if(err1 !== undefined && err1) {
                callback(null);
            }
            if(userFound === undefined){
                callback(null);
            }
            else{
                userFound.karma = userFound.karma + points;
                userFound.rank = UtilsService.updateRank(userFound.karma + points);
                userFound.save();
                callback(userFound.karma);
            }
        });
    },

    convertUTCDateToLocalDate: function (date) {
        var newDate = new Date(date);
        newDate.setMinutes(date.getMinutes() - date.getTimezoneOffset());
        return newDate;
    },

    isEmptyOrBlank: function(str){
       return (!str || 0 === str.length || !str.trim());
    }
}