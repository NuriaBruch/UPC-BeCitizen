package com.becitizen.app.becitizen.domain.controllers;


import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.data.ControllerFaqData;
import com.becitizen.app.becitizen.data.ControllerInformationData;
import com.becitizen.app.becitizen.domain.entities.FaqEntry;
import com.becitizen.app.becitizen.domain.entities.Information;
import com.becitizen.app.becitizen.domain.entities.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ControllerFaqDomain {
    private static ControllerFaqDomain uniqueInstance;
    private ControllerFaqData controllerFaqData;

    private String PREFS_KEY = "myPreferences";

    /**
     * Constructora por defecto para evitar que sea instanciado
     **/
    private ControllerFaqDomain() {
        controllerFaqData = ControllerFaqData.getInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     * @return La instancia de ControllerForumDomain
     */
    public static ControllerFaqDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerFaqDomain();
        return uniqueInstance;
    }

    /**
     * Metodo para obtener las faqs de una categoria
     * @param category nombre de la categoria
     * @return ArrayList<FaqEntry> que contiene las faqs de una categoria
     */
    public ArrayList<FaqEntry> getFaqsCategory(String category) throws NetworkErrorException {

        return null;
    }

    /**
     * Metodo para valorar una faq
     * @param id de la faq
     * @param rating valoracion del usuario
     * @return false si ha ocurrido algun error
     */
    public boolean rateFaq(int id, float rating) throws NetworkErrorException{

        return true;
    }
}
