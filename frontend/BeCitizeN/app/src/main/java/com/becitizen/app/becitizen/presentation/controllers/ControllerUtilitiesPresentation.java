package com.becitizen.app.becitizen.presentation.controllers;

import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.controllers.ControllerUtilitiesDomain;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class ControllerUtilitiesPresentation {
    private static final ControllerUtilitiesPresentation ourInstance = new ControllerUtilitiesPresentation();
    private ControllerUtilitiesDomain controllerUtilitiesDomain;

    public static ControllerUtilitiesPresentation getInstance() {
        return ourInstance;
    }

    private ControllerUtilitiesPresentation() {
        controllerUtilitiesDomain = ControllerUtilitiesDomain.getInstance();
    }

    public void getCurrencyList(List<String> currencyList) throws NetworkErrorException, ServerException, JSONException {
        controllerUtilitiesDomain.getCurrencyList(currencyList);
    }

    public double getConversion(String currencyFrom, String currencyTo, String amount) throws NetworkErrorException, ServerException, JSONException {
        return controllerUtilitiesDomain.getConversion(currencyFrom,currencyTo,amount);
    }

    public String[] getWordOfTheDay() throws ServerException, JSONException, NetworkErrorException {
        return controllerUtilitiesDomain.getWordOfTheDay();
    }

    public String getTranslation(String translateFrom, String translateTo, String textFrom) throws NetworkErrorException, ServerException, JSONException {
        return controllerUtilitiesDomain.getTranslation(translateFrom, translateTo, textFrom);
    }

    public void getLanguagesList(LinkedList<String> languagesList, LinkedList<String> codesList) throws NetworkErrorException, ServerException, JSONException {
        controllerUtilitiesDomain.getLanguagesList(languagesList, codesList);
    }
}
