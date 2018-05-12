module.exports = {

    getExchanges:function(query, callback){
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
            sails.log(query)
            sails.log(body)
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
                    respnse.errors.push("Bad conversion")
                }
                else{
                    response.conversion = conversion
                    callback(response)
                } 
            }
        });
    },

    exchange:function(currencyFrom,currencyTo,amount,callback){
        query = currencyFrom.concat("_").concat(currencyTo)
        this.getExchanges(query,function(response){
            if(response.status != "Ok"){
                callback(response)
            }
            else{
                sails.log(amount)
                conversion = response.conversion;
                conversion1 = conversion*amount;
                response.conversion = conversion1;
                callback(response);
            }
        });
    }
}
