package com.becitizen.app.becitizen.domain;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.becitizen.app.becitizen.domain.adapters.ControllerThreadData;
import com.becitizen.app.becitizen.domain.adapters.ControllerUserData;
import com.becitizen.app.becitizen.domain.entities.User;
import com.becitizen.app.becitizen.domain.enumerations.LoginResponse;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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
     * @return La instancia de ControllerUserDomain
     */
    public static ControllerThreadDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerThreadDomain();
        return uniqueInstance;
    }

    public JSONObject getThreadsCategory(String category) {
        try {
            JSONObject response = new JSONObject(controllerThreadData.getThreadsCategory(category));
            return response;
        }
        catch (JSONException e) {
            return null;
        }
    }

    public JSONObject getCategories() {
        try {
            JSONObject response = new JSONObject(controllerThreadData.getCategories());
            return response;
        }
        catch (JSONException e) {
            return null;
        }
    }
}
