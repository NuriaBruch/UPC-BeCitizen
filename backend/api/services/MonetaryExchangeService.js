function getExchanges(query, callback){
    var response = {
        status: "Ok",
        errors: [] ,
        conversion: ""
    };
    const request = require('request');
    const options = {  
        url: ' http://free.currencyconverterapi.com/api/v5/convert?q='.concat(query).concat("&compact=ultra"),
        method: 'GET',
    };

    
    request(options, function(err1, res, body) {
        if(err1 !== undefined && err1) {
            response.status = "E1";
            response.errors.push(err1);
            callback(response);
        } 
        else{
            
            var parsed = JSON.parse(body);
            var conversion = parsed[query];
            if(conversion === undefined){
                response.status = "E1";
                response.errors.push("Bad conversion")
            }
            else{
                response.conversion = conversion
                callback(response)
            } 
        }
    });
}

module.exports = {

    exchange:function(currencyFrom,currencyTo,amount,callback){
        var response = {
            status: "Ok",
            errors: [] ,
            conversion: ""
        };
        Currency.findOne({name: currencyFrom}).exec(function(err, currencyFromDB){
            if(err !== undefined && err) {
                response.status = "E1";
                response.errors.push(err);
                callback(resposne);
            }
            else if(currencyFromDB){
                Currency.findOne({name: currencyTo}).exec(function(err, currencyToDB){
                    if(err !== undefined && err) {
                        response.status = "E1";
                        response.errors.push(err);
                        callback(resposne);
                    }
                    else if(currencyFromDB){
                        
                        fromToEur = parseFloat(currencyFromDB['rateToEur']) * parseFloat(amount);
                        eurToTo = fromToEur/parseFloat(currencyToDB['rateToEur']);
                        response.conversion = eurToTo;
                        callback(response);
                    }
                    else{
                        response.status = "E2";
                        response.errors.push("One currency was not found");
                        callback(response);
                    }
                });
                
            }
            else{
                response.status = "E2";
                response.errors.push("One currency was not found");
                callback(response);
            }
        });
        /*query = currencyFrom.concat("_").concat(currencyTo)
        getExchanges(query,function(response){
            if(response.status != "Ok"){
                callback(response)
            }
            else{
                conversion = response.conversion;
                conversion1 = conversion*amount;
                response.conversion = conversion1;
                callback(response);
            }
        });*/
    },

    getAllCurrencies:function(){
        const request = require('request');
        const options = {  
            url: ' https://free.currencyconverterapi.com/api/v5/currencies',
            method: 'GET',
        };
        
        request(options, function(err1, res, body) {
            if(err1 !== undefined && err1) {
                
            } 
            else{
                
                var parsed = JSON.parse(body);
                var currencies = parsed['results'];
                
                if(currencies === undefined){
                   
                }
                else{
                    Object.keys(currencies).forEach(function(key) {
                       
                        Currency.findOne({name: key}).exec(function(err, currencyFound){
                            if(err !== undefined && err) {
                                sails.log("server error")
                            }
                            else if(currencyFound){
                                lastUpdate = currencyFound['updatedAt']
                                currentDate = new Date()
                                hoursOfDifference = (Math.abs(currentDate.getTime() - lastUpdate.getTime()) / 3600000)
                                if(hoursOfDifference > 24){
                                    query = currencyFound['name'].concat("_EUR")
                                    getExchanges(query,function(response){
                                        if(response.status != "Ok"){
                                            sails.log("incorrect exchange")
                                        }
                                        else{
                                            currencyFound['rateToEur'] = response.conversion;
                                            currencyFound.save(function(error) {
                                                if(error) {
                                                    sails.log(error);
                                                } else {
                                                }
                                            });
                                            
                                        }

                                    });

                                }
                                
                            }
                            else{
                                query = key.concat("_EUR")
                                getExchanges(query,function(response){
                                    if(response.status != "Ok"){
                                    }
                                    else{
                                        Currency.create({name: key, rateToEur: response.conversion}).exec(function(err2, newCurrency){
                                            if(err2 !== undefined && err2){
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    });
                } 
            }
        });
    },
    
    returnAllCurrencies:function(callback){
        var response = {
            status: "Ok",
            errors: [] ,
            currencies: []
        };

        Currency.find().exec(function(err,currencyFound){
            if(err !== undefined && err) {
                response.status = "E1";
                response.errors.push(err);
                callback(response);
            }
            else {
                Object.keys(currencyFound).forEach(function(key) {
                    response.currencies.push(currencyFound[key]['name']);
                });
                callback(response);
            }
        });
        
    }
}
