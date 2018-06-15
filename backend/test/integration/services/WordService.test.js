

describe('WordService', function () {

    describe('#getWord()', function () {
        it('should a new word with its definition everyday', function (done) {
            var service = new WordService();
            service.getWord(response => {
                //console.log(response.word === undefined);
                if(response !== undefined && response.status == "Ok")
                    done();
                else done("We didnt get response");
            });
        });
    });

    describe("#traslate()", function() {
        it("should translate the input", function(done){
            const translate = require('google-translate-api');
            let textToTranslate = "Bulb; òrgan, ordinàriament subterrani, format per una tija molt curta i engruixida, que treu arrels per la part inferior, sovint recoberta d’un embolcall de fulles membranoses o carnoses.";
            translate(textToTranslate, {to: 'en'}).then(res => {
                //console.log(res.text);
                //=> I speak English
                //console.log(res.from.language.iso);
                //=> nl
                done();
            }).catch(err => {
                //console.error(err);
                done(err);
            });
        });
    });
    
});