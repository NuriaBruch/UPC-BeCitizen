package com.becitizen.app.becitizen.data;

import android.accounts.NetworkErrorException;

public class ControllerFaqData {

    //URIs
    private static final String URI_INFORMATIONS_CATEGORY = "http://becitizen.cf/getAllInfoCategory?category=";
    private static final String URI_INFORMATION_CONTENT = "http://becitizen.cf/getInfo?infoId=";


    private static ControllerFaqData instance = null;

    /**
     * Constructora por defecto para evitar que sea instanciado
     */
    protected ControllerFaqData() {
        // Exists only to defeat instantiation.
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerForumData
     */
    public static ControllerFaqData getInstance() {
        if(instance == null) instance = new ControllerFaqData();
        return instance;
    }

    /**
     * Metodo que solicita las faqs de una categoria.
     *
     * @param category nombre de la categoria
     *
     * @return La respuesta de nuestro servidor
     */
    public String getFaqCategory(String category) throws NetworkErrorException {
        return ServerAdapter.getInstance().doGetRequest(URI_INFORMATIONS_CATEGORY + category);
    }
}
