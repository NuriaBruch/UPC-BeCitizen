
module.exports = {

    createInfo: function(category,title,content,url,type,callback){
        var response = {
            status: "Ok",
            errors: []
         };
        Information.create({
            category: category,
            title: title,
            content: content,
            url: url,
            type: type
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
                category: "",
                type: ""
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
               response.info.type = infoFound.type;
            }
            else{
                response.status = "Error";
                response.errors.push("No information for the given id");
            }
            callback(response);
        });
    },

    deleteInfo: function(infoId,callback){
        var response = {
            status: "Ok",
            errors: []
         };
         Information.destroy({id: infoId}).exec(function (err2,destroyedInfo) {
            if(err2 !== undefined && err2) {
                response.status = "E2";
                response.errors.push(err2);
            }
            if(destroyedInfo<=0){
                response.status = "Error";
                response.errors.push("No information for the given id");
            }
            callback(response);
        });
    },

    getInfoWords: function(words,category,callback){
      var response = {
            status: "Ok",
            errors: [],
            infos:[]
         };
        if(UtilsService.isEmptyOrBlank(words)){
            response.status="E1"
            response.errors.push("No words for search in information titles");
            callback(response);
        }
        else if(UtilsService.isEmptyOrBlank(category)){
            response.status="E2"
            response.errors.push("Category is needed for searching infromation");
            callback(response);
        }
        else{
            words.split('+').join(' ');
            Information.find({category: category,title:{ contains: words}}).exec(function(err2,infosFound){
                if(err2 !== undefined && err2) {
                    response.status = "E3";
                    response.errors.push(err2);
                    callback(response);
                }
                else if(infosFound.length>0){
                    infosFound.forEach(info => {
                        var informationInfo = {
                            title:"",
                            id:""
                        };
                        informationInfo.title = info.title;
                        informationInfo.id = info.id;
                        response.infos.push(informationInfo);
                    });
                    callback(response);
                }
                else{
                    response.status = "E4";
                    response.errors.push("No information found");
                    callback(response);
                }
            });
        }

    },
}   