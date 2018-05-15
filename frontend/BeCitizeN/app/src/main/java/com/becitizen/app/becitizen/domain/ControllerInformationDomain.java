package com.becitizen.app.becitizen.domain;


import com.becitizen.app.becitizen.domain.adapters.ControllerInformationData;
import com.becitizen.app.becitizen.domain.adapters.ControllerThreadData;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Comment;
import com.becitizen.app.becitizen.domain.entities.Information;
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
     * @param block numero de bloque (cada bloque contiene x informaciones)
     * @return JSONObject que contiene las informaciones de una categoria
     */
    public ArrayList<Information> getInformationsCategory(String category, int block) {
        try {
            JSONObject data = new JSONObject(controllerInformationData.getInformationsCategory(category.replace(" ", "%20"), block));
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

}
