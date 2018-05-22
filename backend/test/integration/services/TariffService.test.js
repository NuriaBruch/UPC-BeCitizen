

describe('TariffService', function () {

    describe('#updateTariffs()', function () {
        it('should return tariffs', function (done) {
            var service = new TariffService();
            service.updateTariffs(tariffs => {
                if(tariffs == undefined) done("No tariffs");
                else done();
            });
        });
    });
});