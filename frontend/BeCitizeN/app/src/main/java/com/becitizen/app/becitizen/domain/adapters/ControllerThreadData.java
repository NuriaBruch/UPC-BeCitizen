package com.becitizen.app.becitizen.domain.adapters;

import android.util.Log;

import com.becitizen.app.becitizen.exceptions.ServerException;
import com.facebook.AccessToken;
import com.facebook.FacebookException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerThreadData {

    //URIs
    private static final String URI_THREADS_CATEGORY = "http://10.0.2.2:1337/getAllThreadsCategory?category=";

    private static ControllerThreadData instance = null;

    /**
     * Constructora por defecto para evitar que sea instanciado
     */
    protected ControllerThreadData() {
        // Exists only to defeat instantiation.
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerUserData
     */
    public static ControllerThreadData getInstance() {
        if(instance == null) instance = new ControllerThreadData();
        return instance;
    }

    public String getToken() {
        return ServerAdapter.getInstance().getTOKEN();
    }

    public String getThreadsCategory(String category) {
        return ServerAdapter.getInstance().doGetRequest(URI_THREADS_CATEGORY + category);
    }

    public String getCategories() {
        return ServerAdapter.getInstance().doGetRequest(URI_THREADS_CATEGORY);
    }
}
