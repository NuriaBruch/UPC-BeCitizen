module.exports = class TariffService {

    prettyFormat(tariffs){
        var response = 
        `The scope of the Autoritat del Transport Metropolità integrated fare system covers a total of 253 municipalities, divided into six rings and various fare sectors.        
A single ticket permits the use of the transport needed to move from one place to another. It permits the change to the Barcelona suburban railway integrated network, Ferrocarrils de la Generalitat, the metro, bus and tram.        
All tickets, as well as unlimited journey tickets, must be validated before boarding the train. Keep your ticket until you have left the station        
You can use up to four different modes on each transfer, and time for transfers between them are:

1 zona:  1 h 15 min

2 zones: 1 h 30 min

3 zones: 1 h 45min

4 zones: 2 h

5 zones: 2 h 15 min

6 zones: 2 h 30 min
`;

        tariffs.forEach((obj) => {
            let block = 
            `
    ${obj.type}:
    ${obj.definition}
`
            let block2 = "";
            obj.tariffs.forEach((tariff) => {
                block2 += `     Zone ${tariff.zone}: ${tariff.price}€
`

            })
            response += block + block2;
        })

        console.log(response);
        return response;
    }

    updateInfo(tariffs, callback){
        // callback(err);
        var err = false;

        Information.findOne({isTarifa : true})
        .then(info => {
            if(info !== undefined){
                info.content = this.prettyFormat(tariffs);
                info.save(err => callback(err));
            }
            else{
                Information.create({
                    category: "tourism", 
                    title: "Tariffs",
                    content: this.prettyFormat(tariffs),
                    isTarifa: true
                })
                .then(info => {
                    callback(err);
                })
                .catch(err => {
                    err = true;
                    callback(err);
                })
            }
        });
    }

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
            //console.log(p.length);
            if(p.length <= 25 && !isNaN(p[0])){
                let tariff = {
                    zone: 0,
                    price: 0
                }
                let pString = p.replace(/\u00A0/g, '');
                //console.log("*************");
                //console.log(pString);
                let pSplit = pString.split(" ");
                //console.log(pSplit);
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

        request("http://rodalies.gencat.cat/en/tarifes/servei_rodalia_barcelona/servei_integrat_atm/index.html", (err, res, html) => {
            if(!err){
                //console.log("We are in da request");
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
        // callback(err, tariffs)
        this.scrappingTariffs((err, tariffs) => {
            if(!err){
                this.updateInfo(tariffs, (err2) => {
                    if(err2) console.log("Error updating!");
                    callback(err2, tariffs);
                });
            }
            else{
                callback(true, null);
            }
        });
    }
}