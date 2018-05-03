package com.becitizen.app.becitizen.domain;


import com.becitizen.app.becitizen.domain.adapters.ControllerThreadData;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.domain.entities.User;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;


public class ControllerThreadDomain {
    private static ControllerThreadDomain uniqueInstance;
    private ControllerThreadData controllerThreadData;
    private User currentUser;

    private String PREFS_KEY = "myPreferences";

    /**
     * Constructora por defecto para evitar que sea instanciado
     *
     **/
    private ControllerThreadDomain() {
        controllerThreadData = ControllerThreadData.getInstance();
        currentUser = new User();
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerThreadDomain
     */
    public static ControllerThreadDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerThreadDomain();
        return uniqueInstance;
    }

    /**
     * Metodo para obtener los posts de una categoria
     *
     * @param category nombre de la categoria
     *
     * @return JSONObject que contiene los posts de una categoria
     */
    public JSONObject getThreadsCategory(String category) {
        try {
            JSONObject response = new JSONObject(controllerThreadData.getThreadsCategory(category));
            return response;
        }
        catch (JSONException e) {
            return null;
        }
    }

    /**
     * Metodo para obtener los nombres de todas las categorias
     *
     * @return JSONObject que contiene los nombres de todas las categorias
     */
    public JSONObject getCategories() {
        try {
            JSONObject response = new JSONObject(controllerThreadData.getCategories());
            return response;
        }
        catch (JSONException e) {
            return null;
        }
    }

    /**
     * Metodo que crea un nuevo thread.
     *
     * @param t el thread que se quiere crear
     *
     * @return
     */
    public boolean newThread(Thread t) throws ServerException {
        return controllerThreadData.newThread(t);
    }
}
