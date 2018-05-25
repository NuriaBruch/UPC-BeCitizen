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
    },

  tariffsUpdate: {
    schedule: '* * 1 * * *',
    onTick: function () {
      new TariffService().updateTariffs(((err, tariffs) => {
        if(err) console.log("Error scrapping or updating");
      }));
    }
  }
};
