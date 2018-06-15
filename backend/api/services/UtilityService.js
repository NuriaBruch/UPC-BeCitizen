module.exports = class UtilityService{
    translate(text, from, to, callback) {
        // callback(response)
        var response = {
            status: "Ok",
            errors: [],
            info: ""
        };

        UtilsService.translate(text, from, to)
        .then(res => {
            response.info = res;
            callback(response);
        })
        .catch(err => {
            response.status = "Error";
            response.errors.push("Server error");
            callback(response);
        });
    }

    async getLanguageCodes(){
        var response = {
            status: "Ok",
            errors: [],
            info: []
        };

        try {
            let lcodes = await LanguageCode.find({})
            response.info = lcodes.map(entry => {
                return {name: entry.name, code: entry.code}
            });
        }
        catch (e) {
            console.log(e);
            response.status = "Error";
        }
        return response;
    }

    async updateLanguageCodes(){
        const fs = require("fs");
        const util = require('util');
        const parser = require('xml2json');

        const readFile = util.promisify(fs.readFile);
        try{
            let file = await readFile("data/languageCodes.xml", "utf8");
            let jsonFile = parser.toJson(file);

            let pairs = JSON.parse(jsonFile).tbody.tr
            .map(entry => entry.td)
            .filter((entry, index) => index != 0)
            .map(entry =>{
                return [
                    {
                        name: entry[0], 
                        code: entry[1]
                    },
                    {
                        name: entry[2],
                        code: entry[3]
                    }
                ];
            })
            .reduce((ac, entry) => ac.concat(entry));
            
            await LanguageCode.destroy({});
            await LanguageCode.create(pairs);
        }
        catch(e){
            console.log(e);
            throw e;
        }
    }

    
}