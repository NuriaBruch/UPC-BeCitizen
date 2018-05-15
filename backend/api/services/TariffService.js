module.exports = class WordService {
    scrappingTariffs(){
        
    }

    updateTariffs(callback){
        
        this.scrappingTariffs((err, tariffs) => {
            if(!err){
                updateTariffs(tariffs, (err) => {
                    if(err) console.log("Error!");
                    else console.log("No error!");
                });
            }
        });
    }
}