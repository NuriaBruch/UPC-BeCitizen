
module.exports = class WordService {

    scrappingWord(callback){
        //callback(err, word)
        var word = {
            word: "",
            definition: ""
        }
        const request = require('request');
        const cheerio = require("cheerio");

        request("http://rodamots.cat/", function(err, res, html) {
            if(!err){
                let $ = cheerio.load(html);
                let main = $("#main");
                let article = main.find("article").eq(0); // We got the last one
                let gold = article.find("h2 > a");

                word.word = gold.text();
                var wordUrl = gold.attr("href");
                request(wordUrl, function(err2, res2, html2){
                    if(!err2){
                        let $ = cheerio.load(html2);
                        let innerDef = $(".innerdef").eq(0);
                        let gold = innerDef.find("p").eq(0);
                        word.definition = gold.text();

                        if(word.word == "" || word.definition == "") callback(true, null);
                        else callback(false, word);
                    }
                    else {
                        callback(true, null);
                    }
                });
            }
            else{
                callback(true, null);
            }
        });
    }

    prettyWord(word){
        return {
            word: word.word,
            definition: word.definition
        }
    }

    needUpdateServer(callback){
        // callback(error, need) where error && need are booleans

        var today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        today.setMilliseconds(1);
        
        Word.find({
            createdAt: {">=": today}, 
            limit: 1,
            sort: 'createdAt DESC'
        })
        .then(function(word){
            if(word){
                callback(false, false); // from false to true just for test scrapping
            }
            else{
                callback(false, true);
            }
        })
        .catch(function(error){
            console.log(error);
            callback(true, true)
        });
    }

    getLastWord(callback){
        // callback(error, found, word

        Word.find({
            limit: 1,
            sort: 'createdAt DESC'
        })
        .then(function(word){
            console.log(word);
            if(word)
                callback(false, true, word[0]);
            else 
                callback(false, false, null);
        })
        .catch(function(err){
            console.log(err);
            callback(true, false, null);
        })
    }

    getWord(callback) {
        var response = {
            status: "Ok",
            errors: [],
            info: null
        }

        this.needUpdateServer((err, need) => {
            if(err){
                response.status = "E1"
                response.errors.push("Server error");
                callback(response);
            }
            else if(need){
                this.scrappingWord((err2, word) => {
                    if(!err2){
                        response.info = word;
                        callback(response)
                    }
                    else{
                        response.status = "E2";
                        response.errors.push("Scrapping error");
                        this.getLastWord((err3, found, word) => {
                            if(err3){
                                response.info = "E1";
                                response.errors.push("Server error")
                            }
                            else if(!found){
                                response.info = "E23";
                                response.errors.push("Scrapping error and no words in db.")
                            }
                            else{
                                response.info = this.prettyWord(word);
                                callback(response);
                            }
                        }); 
                    }
                })
            }
            else{
                this.getLastWord((err3, found, word) => {
                    if(err3){
                        response.info = "E1";
                        response.errors.push("Server error")
                    }
                    else if(!found){
                        response.info = "E3";
                        response.errors.push("No words in db.")
                    }
                    else{
                        response.info = this.prettyWord(word);
                    }
                    callback(response);
                });
            }
        });
    }
}