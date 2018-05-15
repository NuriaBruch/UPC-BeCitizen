package com.becitizen.app.becitizen.domain;


import com.becitizen.app.becitizen.domain.adapters.ControllerInformationData;
import com.becitizen.app.becitizen.domain.adapters.ControllerThreadData;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Comment;
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
     * Metodo para obtener los nombres de todas las categorias
     *
     * @return JSONObject que contiene los nombres de todas las categorias
     */
    public ArrayList<String> getCategories() {
        try {
            JSONObject response = new JSONObject(controllerInformationData.getCategories());
            ArrayList<String> categories = new ArrayList<String>();
            JSONArray array = (JSONArray)response.get("categories");
            for(int i = 0; i < array.length(); i++)
            {
                categories.add(array.getString(i));
            }
            return categories;
        }
        catch (JSONException e) {
            return new ArrayList<>();
        }
    }

}
