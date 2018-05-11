
module.exports = {

    createInfo: function(category,title,content,url,callback){
        var response = {
            status: "Ok",
            errors: []
         };
        Information.create({
            category: category,
            title: title,
            content: content,
            url: url
        }).exec(function(err2, newInfo){
            if(err2 !== undefined && err2){
                response.status = "E2";
                response.errors.push(err2);
            }
            callback(response);
            }   
        );
    },

    getAllInfoCategory: function(category,callback){
        var response = {
            status: "Ok",
            errors: [],
            infos:[]
         };
        Information.find({category: category}).sort('title ASC').exec(function(err2,infosFound){
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(infosFound.length>0){
                infosFound.forEach(info => {
                    var informationInfo = {
                        title:"",
                        id:""
                    };
                    informationInfo.title = info.title;
                    informationInfo.id = info.id;
                    response.infos.push(informationInfo);
                });
            }
            else{
                response.status = "Error";
                response.errors.push("No information for the given category");
            }
            callback(response);
        });
    }
}