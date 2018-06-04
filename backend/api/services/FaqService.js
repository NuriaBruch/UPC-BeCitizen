
module.exports = {

    createFaq(category,question,answer,callback){
        var response = {
            status: "Ok",
            errors: []
         };
        Faq.create({
            category: category,
            question: question,
            answer: answer
        }).exec(function(err2, newFaq){
            if(err2 !== undefined && err2){
                response.status = "E2";
                response.errors.push(err2);
            }
            callback(response);
            }   
        );
    },
    deleteFaq(faqId,callback){
        var response = {
            status: "Ok",
            errors: []
         };
         Faq.destroy({id: faqId}).exec(function (err2,destroyedFaq) {
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(destroyedFaq<=0){
                response.status = "Error";
                response.errors.push("No FAQ matching the given id");
            }
            callback(response);
        });
    },
    getFaqs(callback){
        var response = {
            status: "Ok",
            errors: [],
            info: []
        };

        Faq.find({})
        .then(faqs => {
            console.log(faqs);
            response.info = faqs;
            callback(response);
        })
        .catch(err => {
            response.status = "E0";
            errors.push("Server error");
            callback(response);
        })
    }

}