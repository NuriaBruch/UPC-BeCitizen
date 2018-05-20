

describe('WordService', function () {

    describe('#getWord()', function () {
        it('should a new word with its definition everyday', function (done) {
            var service = new WordService();
            service.getWord(response => {
                console.log(response.word === undefined);
                if(response !== undefined && response.status == "Ok")
                    done();
                else done("We didnt get response");
            });
        });
    });
});