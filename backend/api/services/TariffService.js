module.exports = class TariffService {
    getInfoFromPanel(panel, $){
        var res = {
            error: false,
            value: {
                type: "",
                definition: "",
                tariffs: [
                ]
            }
        }
        res.value.type = panel.find("a.collapsed").text();
        let paragraphs = panel.find("p");
        res.value.definition = paragraphs.eq(0).text();

        paragraphs = paragraphs.slice(1);
        paragraphs.each((i, p) => {
            p = $(p).text();
            console.log(p.length);
            if(p.length <= 25 && !isNaN(p[0])){
                let tariff = {
                    zone: 0,
                    price: 0
                }
                let pString = p.replace(/\u00A0/g, '');
                console.log("*************");
                console.log(pString);
                let pSplit = pString.split(" ");
                console.log(pSplit);
                tariff.zone = pSplit[0];
                tariff.price = pSplit[pSplit.length - 1];
                res.value.tariffs.push(tariff);
                if(tariff.zone == undefined || tariff.price == undefined) res.error = true;
            }
        })
        return res;
    }

    scrappingTariffs(callback){
        //callback(err, tariffs)
        var tariffs = [
    
        ]
        /*
            {
                type: "",
                definition: "",
                tariffs: [
                    {
                        zone: "",
                        price
                    }
                ]
            }
        */
        const request = require('request');
        const cheerio = require("cheerio");

        request("http://rodalies.gencat.cat/ca/tarifes/servei_rodalia_barcelona/servei_integrat_atm/", (err, res, html) => {
            if(!err){
                console.log("We are in da request");
                let $ = cheerio.load(html);
                let main = $("#acordio_distribuidora");
                let panels = main.find("div.panel.panel-default").slice(0, 7);
                var error = false;

                panels.each((i, panel) => {
                    let response = this.getInfoFromPanel($(panel), $)
                    if(response.error) error = true;
                    else tariffs.push(response.value);
                });
                if(error) {
                    console.log("Algún panel me ha dado error");
                    callback(true, null);
                }
                else callback(false, tariffs);
            }
            else{
                console.log("Error al hacer la request")
                callback(true, null);
            }
        });
        
    
    }

    updateTariffs(callback){
        
        this.scrappingTariffs((err, tariffs) => {
            /*if(!err){
                updateTariffs(tariffs, (err) => {
                    if(err) console.log("Error!");
                    else console.log("No error!");
                });
            }*/
            console.log(err);
            callback(tariffs);
        });
    }
}