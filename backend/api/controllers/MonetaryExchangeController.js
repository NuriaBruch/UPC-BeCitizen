/**
 * MonetaryExchangeController
 *
 * @description :: Server-side logic for managing monetaryexchanges
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
	getExchange:function(req,res){
        var currencyFrom = req.query.currencyFrom;
        var currencyTo = req.query.currencyTo;
        var amount = req.query.amount;
        MonetaryExchangeService.exchange(currencyFrom,currencyTo,amount,function(response){
            res.send(response);
        });
    },
    getAllCurrencies: function(req, res){
        MonetaryExchangeService.returnAllCurrencies(function(response){
            res.send(response);
        })
       
    }

};

