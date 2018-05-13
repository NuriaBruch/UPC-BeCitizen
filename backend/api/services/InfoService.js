
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
    },


    getInfo: function(infoId,callback){
        var response = {
            status: "Ok",
            errors: [],
            info:{
                title: "",
                content: "",
                url: "",
                category: ""
            }
         };
        Information.findOne({id: infoId}).exec(function(err2,infoFound){
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(infoFound){
               response.info.title = infoFound.title;
               response.info.content = infoFound.content;
               response.info.url = infoFound.url;
               response.info.category = infoFound.category;
            }
            else{
                response.status = "Error";
                response.errors.push("No information for the given id");
            }
            callback(response);
        });
    }
}