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
            JSONObject data = new JSONObject(controllerInformationData.getInformation(id));
            data = data.getJSONObject("info");
            String type = data.getString("type");
            String content;
            ArrayList<Marker> markers = new ArrayList<>();
            if (type.equals("map")) {
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
