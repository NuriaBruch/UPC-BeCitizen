
module.exports = class WordService {

    scrappingWord(callback){
        callback(false, "Pole");
    }

    prettyWord(word){
        return {
            word: word.word,
            definition: definition.definition
        }
    }

    needUpdateServer(callback){
        // callback(error, need) where error && need are booleans

        var today = new Date();
        Word.findOne({createdAt: today})
        .then(function(word){
            if(word){
                callback(false, false);
            }
            else{
                callback(false, true);
            }
        })
        .catch(function(error){
            callback(true, true)
        })

        callback(false, true);
    }

    getLastWord(callback){
        // callback(error, found, word

        Word.findOne({
            limit: 1,
            sort: 'createdAt ASC'
        })
        .then(function(word){
            if(word)
                callback(false, true, word);
            else 
                callback(false, false, null);
        })
        .catch(function(err){
            callback(true, false, null);
        })
    }

    getWord(callback) {
        var response = {
            status: "Ok",
            errors: [],
            info: null
        }

        needUpdateServer(function(err, need){
            if(err){
                response.status = "E1"
                response.errors.push("Server error");
                callback(response);
            }
            else if(need){
                scrappingWord(function(err2, word){
                    if(!err2){
                        response.info = word;
                        callback(response)
                    }
                    else{
                        response.status = "E2";
                        response.errors.push("Scrapping error");
                        getLastWord(function(err3, found, word){
                            if(err3){
                                response.info = "E1";
                                response.errors.push("Server error")
                            }
                            else if(!found){
                                response.info = "E23";
                                response.errors.push("Scrapping error and no words in db.")
                            }
                            else{
                                response.info = prettyWord(word);
                                callback(response);
                            }
                        }); 
                    }
                })
            }
            else{
                getLastWord(function(err3, found, word){
                    if(err3){
                        response.info = "E1";
                        response.errors.push("Server error")
                    }
                    else if(!found){
                        response.info = "E3";
                        response.errors.push("No words in db.")
                    }
                    else{
                        response.info = prettyWord(word);
                        callback(response);
                    }
                }); 
            }
        });
    }
}