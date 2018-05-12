module.exports.cron = {
    exchangeActualization: {
      schedule: '* * 0 * * *',
      onTick: function () {
        MonetaryExchangeService.getAllCurrencies();
      }
    }
  };