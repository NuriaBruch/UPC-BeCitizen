
describe('UtilityService', function () {

    describe('#translate()', function () {
        it('should translate', function (done) {
            var service = new UtilityService();
            service.translate("Hello", "en", "es", (response) => {
                console.log(response.info)
                if(response.info == "Hola") done();
                else done("Wrong output.");
            });
        });
    });
});