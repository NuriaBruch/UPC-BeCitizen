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
            response.info = await LanguageCode.find().limit(1);
        }
        catch (e) {
            console.log(e);
            response.status = "E0";
        }
    }

    async updateLanguageCodes(){
        console.log("mock");
    }
}