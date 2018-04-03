
module.exports = {

getRandomString: function (){
    var chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var length = 32;
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
},

getHashedString: function(pass,saltRounds,callback){
    var bcrypt = require('bcrypt');
    bcrypt.hash(pass, saltRounds, function(err1, hash) {
        callback(err1,hash);
    });
},

getUsrInfo: function(name,callback){
    answer = {
        namne: "",
        surname: ""
    }
    var result = name.split(" ", 2);
    answer.name = result[0];
    answer.surname = result[1];
    callback(answer);
}




}