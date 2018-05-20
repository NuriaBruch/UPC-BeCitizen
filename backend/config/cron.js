module.exports.cron = {
  tariffsUpdate: {
    schedule: '* * 0 * * *',
    onTick: function () {
      TariffService.updateTariffs();
    }
  }
};