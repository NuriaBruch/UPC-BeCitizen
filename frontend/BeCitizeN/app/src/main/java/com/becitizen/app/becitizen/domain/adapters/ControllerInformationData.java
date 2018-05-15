package com.becitizen.app.becitizen.domain.adapters;

import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerInformationData {

    //URIs
    //TODO: change back to server
    private static final String URI_INFORMATIONS_CATEGORY = "http://10.0.2.2:1337/getAllInfoCategory?category=";


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
     * Metodo que solicita las informaciones de una categoria.
     *
     * @param category nombre de la categoria
     *
     * @return La respuesta de nuestro servidor
     */
    public String getInformationsCategory(String category) {
        return ServerAdapter.getInstance().doGetRequest(URI_INFORMATIONS_CATEGORY + category);
    }
}
