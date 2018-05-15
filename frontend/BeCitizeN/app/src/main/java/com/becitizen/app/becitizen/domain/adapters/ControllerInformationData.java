package com.becitizen.app.becitizen.domain.adapters;

import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerInformationData {

    //URIs
    private static final String URI_CATEGORIES = "http://becitizen.cf/categories";


    private static ControllerInformationData instance = null;

    /**
     * Constructora por defecto para evitar que sea instanciado
     */
    protected ControllerInformationData() {
        // Exists only to defeat instantiation.
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerThreadData
     */
    public static ControllerInformationData getInstance() {
        if(instance == null) instance = new ControllerInformationData();
        return instance;
    }

    /**
     * Metodo que solicita los nombres de todas las categorias.
     *
     * @return La respuesta de nuestro servidor
     */
    public String getCategories() {
        return ServerAdapter.getInstance().doGetRequest(URI_CATEGORIES);
    }
}
