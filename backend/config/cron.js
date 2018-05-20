module.exports.cron = {
  tariffsUpdate: {
    schedule: '* * */12 * * *',
    onTick: function () {
      new TariffService().updateTariffs(((err, tariffs) => {
        if(err) console.log("Error scrapping or updating");
      }));
    }
  }
};