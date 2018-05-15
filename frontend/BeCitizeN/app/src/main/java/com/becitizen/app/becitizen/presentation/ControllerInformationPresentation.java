package com.becitizen.app.becitizen.presentation;

import android.content.Context;
import android.os.Bundle;

import com.becitizen.app.becitizen.domain.ControllerInformationDomain;
import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Information;
import com.becitizen.app.becitizen.domain.enumerations.LoginResponse;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ControllerInformationPresentation {

    static ControllerInformationPresentation uniqueInstance;

    ControllerInformationDomain controllerInformationDomain;

    /**
     * Constructora privada para que no se instancie
     * y inicializar valores
     */
    private ControllerInformationPresentation() {
        controllerInformationDomain = ControllerInformationDomain.getUniqueInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return Instancia
     */
    public static ControllerInformationPresentation getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ControllerInformationPresentation();
        }

        return uniqueInstance;
    }

    /**
     * Metodo que retorna todas las informaciones de una categoria
     * @param category name of the category
     * @return empty arraylist si ha ocurrido algun error
     */
    public ArrayList<Information> getInformationsCategory(String category, int block) {
        return controllerInformationDomain.getInformationsCategory(category, block);
    }
}
