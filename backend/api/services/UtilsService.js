
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

getEmailFromHeader: (req) => jw.decode(req.get("token")).email

}