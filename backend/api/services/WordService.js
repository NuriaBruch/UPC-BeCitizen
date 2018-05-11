
module.exports = class WordService {

    scrappingWord(callback){
        callback(false, "Pole");
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
        var yesterday = new Date()
        yesterday.setDate(today.getDate() - 1);
        console.log(today);
        console.log(yesterday);
        Word.findOne({createdAt: {">": yesterday}})
        .then(function(word){
            if(word){
                callback(false, false);
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
            sort: 'createdAt ASC'
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