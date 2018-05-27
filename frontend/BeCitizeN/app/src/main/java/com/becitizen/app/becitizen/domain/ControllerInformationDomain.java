package com.becitizen.app.becitizen.domain;


import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.adapters.ControllerInformationData;
import com.becitizen.app.becitizen.domain.adapters.ControllerThreadData;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Comment;
import com.becitizen.app.becitizen.domain.entities.Information;
import com.becitizen.app.becitizen.domain.entities.Marker;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ControllerInformationDomain {
    private static ControllerInformationDomain uniqueInstance;
    private ControllerInformationData controllerInformationData;

    private String PREFS_KEY = "myPreferences";

    /**
     * Constructora por defecto para evitar que sea instanciado
     **/
    private ControllerInformationDomain() {
        controllerInformationData = ControllerInformationData.getInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     * @return La instancia de ControllerThreadDomain
     */
    public static ControllerInformationDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerInformationDomain();
        return uniqueInstance;
    }

    /**
     * Metodo para obtener las informaciones de una categoria
     * @param category nombre de la categoria
     * @return JSONObject que contiene las informaciones de una categoria
     */
    public ArrayList<Information> getInformationsCategory(String category) throws NetworkErrorException {
        try {
            JSONObject data = new JSONObject(controllerInformationData.getInformationsCategory(category.replace(" ", "%20")));
            ArrayList<Information> informations = new ArrayList<>();
            JSONArray array = (JSONArray)data.get("infos");
            for(int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                informations.add(new Information(object.getInt("id"), object.getString("title")));
            }
            return informations;
        }
        catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Metodo para obtener la una informacion
     * @param id de la informacion
     * @return null si ha ocurrido algun error
     */
    public Information getInformation(int id) throws NetworkErrorException{
        try {
            String response = controllerInformationData.getInformation(id);
            JSONObject data = null;
            try {
                data = new JSONObject(response.replace("\\" + '"', "\"").replace("\"[", "[").replace("]\"", "]"));
            } catch (JSONException e) {
                data = new JSONObject(response);
            }

            data = data.getJSONObject("info");
            String type = data.getString("type");
            String content;
            ArrayList<Marker> markers = new ArrayList<>();
            if (type.equals("map")) {
                /*JSONArray markersArray = new JSONObject("{\n" +
                        "    \"status\": \"Ok\",\n" +
                        "    \"errors\": [],\n" +
                        "    \"info\": {\n" +
                        "    \"title\": \"Map with the markets in the city\",\n" +
                        "    \"content\":[{ \"tooltip\": \"Mercat de Sant Gervasi\",\"latitude\": \"41.405663\",\"longitude\": \"2.138554\"},{ \"tooltip\": \"Mercat de l'Abaceria Central\",\"latitude\": \"41.40224\",\"longitude\": \"2.159387\"},{ \"tooltip\": \"Mercat del Carmel\",\"latitude\": \"41.42374\",\"longitude\": \"2.155818\"},{ \"tooltip\": \"Mercat de La Mercè\",\"latitude\": \"41.430483\",\"longitude\": \"2.173608\"},{ \"tooltip\":\"Mercat d'Horta\",\"latitude\": \"41.429625\",\"longitude\": \"2.159617\"},{ \"tooltip\": \"Mercat del Guinardó\",\"latitude\": \"41.419151\",\"longitude\": \"2.179729\"},{ \"tooltip\": \"Mercat d'Hostafrancs\",\"latitude\": \"41.375162\",\"longitude\": \"2.143862\"},{ \"tooltip\": \"Mercat Encants Vells Fira de Bellcaire\",\"latitude\": \"41.401063\",\"longitude\": \"2.185677\"},{ \"tooltip\": \"Mercat del Fort Pienc\",\"latitude\": \"41.395773\",\"longitude\": \"2.182549\"},{ \"tooltip\": \"Mercat de Sarrià\",\"latitude\": \"41.399835\",\"longitude\": \"2.120731\"},{ \"tooltip\": \"Mercat de La Concepció\",\"latitude\": \"41.395591\",\"longitude\": \"2.168974\"},{ \"tooltip\": \"Mercat de Les Tres Torres\",\"latitude\": \"41.399944\",\"longitude\": \"2.131289\"},{ \"tooltip\": \"Mercat de Les Corts\",\"latitude\": \"41.384078\",\"longitude\": \"2.129813\"},{ \"tooltip\": \"Mercat Encants Vells Fira de Bellcaire\",\"latitude\": \"41.40131\",\"longitude\": \"2.186752\"},{ \"tooltip\": \"Mercat de la Sagrada Família\",\"latitude\": \"41.405718\",\"longitude\": \"2.177037\"},{ \"tooltip\": \"Mercat del Poblenou - Unió\",\"latitude\": \"41.401021\",\"longitude\": \"2.205437\"},{ \"tooltip\": \"Mercat de Canyelles\",\"latitude\": \"41.441988\",\"longitude\": \"2.164435\"},{ \"tooltip\": \"Mercat del Clot\",\"latitude\": \"41.407616\",\"longitude\": \"2.188652\"},{ \"tooltip\": \"Mercat del Ninot\",\"latitude\": \"41.387857\",\"longitude\": \"2.15442\"},{ \"tooltip\":\"Mercat de La Trinitat\",\"latitude\": \"41.448692\",\"longitude\": \"2.184241\"},{ \"tooltip\": \"Mercat Dominical de Sant Antoni\",\"latitude\": \"41.379822\",\"longitude\": \"2.161426\"},{ \"tooltip\": \"Mercat de Santa Caterina\",\"latitude\": \"41.386394\",\"longitude\": \"2.178185\"},{ \"tooltip\": \"Mercat Encants de Sant Antoni\",\"latitude\": \"41.381301\",\"longitude\": \"2.163459\"},{ \"tooltip\": \"Mercat de La Guineueta\",\"latitude\": \"41.438652\",\"longitude\": \"2.170254\"},{ \"tooltip\": \"Mercat de la Llibertat\",\"latitude\": \"41.399737\",\"longitude\": \"2.153756\"},{ \"tooltip\": \"Mercat de Ciutat Meridiana\",\"latitude\": \"41.461717\",\"longitude\": \"2.17782\"},{ \"tooltip\": \"Mercat de l'Estrella\",\"latitude\": \"41.409911\",\"longitude\": \"2.164001\"},{ \"tooltip\":\"Mercat de Galvany\",\"latitude\": \"41.396682\",\"longitude\": \"2.143687\"},{ \"tooltip\": \"Mercat del Besòs\",\"latitude\": \"41.419232\",\"longitude\": \"2.215137\"},{ \"tooltip\": \"Mercat de Provençals\",\"latitude\": \"41.419457\",\"longitude\": \"2.196493\"},{ \"tooltip\": \"Mercat de les Flors *Les Rambles\",\"latitude\": \"41.382986\",\"longitude\": \"2.171956\"},{ \"tooltip\": \"Mercat de Sant Martí\",\"latitude\": \"41.418656\",\"longitude\": \"2.205331\"},{ \"tooltip\": \"Mercat de La Barceloneta\",\"latitude\": \"41.380431\",\"longitude\": \"2.189132\"},{ \"tooltip\": \"Mercat de Sant Antoni\",\"latitude\": \"41.379777\",\"longitude\": \"2.163244\"},{ \"tooltip\": \"Mercat de Sant Josep - La Boqueria\",\"latitude\": \"41.382048\",\"longitude\": \"2.172279\"},{ \"tooltip\": \"Mercat del Bon Pastor\",\"latitude\": \"41.436245\",\"longitude\": \"2.207849\"},{ \"tooltip\": \"Mercat de Felip II\",\"latitude\": \"41.422026\",\"longitude\": \"2.185101\"},{ \"tooltip\": \"Mercat de Sant Andreu\",\"latitude\": \"41.433957\",\"longitude\": \"2.191032\"},{ \"tooltip\": \"Mercat de Lesseps\",\"latitude\": \"41.409132\",\"longitude\": \"2.152066\"},{ \"tooltip\": \"Mercat de Sants\",\"latitude\":\"41.374741\",\"longitude\": \"2.133763\"},{ \"tooltip\": \"Mercat de Montserrat\",\"latitude\": \"41.446325\",\"longitude\": \"2.178797\"},{ \"tooltip\": \"Mercat de La Marina\",\"latitude\": \"41.360378\",\"longitude\": \"2.139198\"},{ \"tooltip\": \"Mercat de Vall d'Hebron -Teixonera\",\"latitude\": \"41.424062\",\"longitude\": \"2.14246\"}],\n" +
                        "    \"url\": null,\n" +
                        "    \"category\": \"tourism\",\n" +
                        "    \"type\": \"map\"\n" +
                        "    }\n" +
                        "}").getJSONObject("info").getJSONArray("content"); //UNCOMMENT THIS AND COMMENT NEXT LINE IF MAPS DO NOT WORK*/
                JSONArray markersArray = data.getJSONArray("content");
                for (int i = 0; i < markersArray.length(); ++i) {
                    JSONObject marker = markersArray.getJSONObject(i);
                    markers.add(new Marker(marker.getString("tooltip"), marker.getDouble("latitude"), marker.getDouble("longitude")));
                }
                content = "";
            } else {
                content = data.getString("content");
            }

            return new Information(id, data.getString("title"), content,
                    data.getString("url"), type, markers);
        }
        catch (JSONException e) {
            return null;
        }
    }
}
