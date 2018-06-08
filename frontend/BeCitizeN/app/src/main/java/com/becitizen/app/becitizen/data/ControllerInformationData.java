package com.becitizen.app.becitizen.data;

import android.accounts.NetworkErrorException;

import org.json.JSONObject;

public class ControllerInformationData {

    //URIs
    private static final String URI_INFORMATIONS_CATEGORY = "http://becitizen.cf/getAllInfoCategory?category=";
    private static final String URI_INFORMATIONS_CATEGORY_SEARCH = "http://becitizen.cf/getInfoWords?category=";
    private static final String URI_INFORMATION_CONTENT = "http://becitizen.cf/getInfo?infoId=";


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
     * @return La instancia de ControllerForumData
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
    public String getInformationsCategory(String category) throws NetworkErrorException {
        return ServerAdapter.getInstance().doGetRequest(URI_INFORMATIONS_CATEGORY + category);
    }

    /**
     * Metodo que solicita una informacion
     * @param id id de la informacion
     *
     * @return La respuesta de nuestro servidor
     */
    public String getInformation(int id) throws NetworkErrorException{
        return ServerAdapter.getInstance().doGetRequest(URI_INFORMATION_CONTENT + id);
    }

    public String getInformationsCategorySearch(String category, String searchWords)  throws NetworkErrorException {
        return ServerAdapter.getInstance().doGetRequest(URI_INFORMATIONS_CATEGORY_SEARCH + category + "&words="+searchWords);
    }
}
