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
  },
  languageCodesUpdate: {
    schedule: '* * 2 * * *',
    onTick: function(){
      new UtilityService().updateLanguageCodes()
      .catch(() => {
        console.log("Error trying yo update language codes");
      })
    }
  }
};
