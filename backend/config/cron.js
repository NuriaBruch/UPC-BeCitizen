module.exports.cron = {
    exchangeActualization: {
      schedule: '* * 0 * * *',
      onTick: function () {
        MonetaryExchangeService.getAllCurrencies();
      }
    },
    exchangeActualization2: {
        schedule: '* * 12 * * *',
        onTick: function () {
          MonetaryExchangeService.getAllCurrencies();
        }
      }
  };