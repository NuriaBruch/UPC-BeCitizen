package com.becitizen.app.becitizen.domain.controllers;

import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.data.ControllerUtititesData;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class ControllerUtilitiesDomain {
    private static final ControllerUtilitiesDomain ourInstance = new ControllerUtilitiesDomain();

    private ControllerUtititesData controllerUtititesData;

    public static ControllerUtilitiesDomain getInstance() {
        return ourInstance;
    }

    private ControllerUtilitiesDomain() {
        controllerUtititesData = ControllerUtititesData.getInstance();
    }

    public void getCurrencyList(List<String> currencyList) throws NetworkErrorException{
        controllerUtititesData.getCurrencyList(currencyList);
    }

    public double getConversion(String currencyFrom, String currencyTo, String amount) throws NetworkErrorException{
        return controllerUtititesData.getConversion(currencyFrom,currencyTo,amount);
    }

    public String[] getWordOfTheDay() throws ServerException, JSONException, NetworkErrorException {
        return controllerUtititesData.getWordOfTheDay();
    }

    public void getLanguagesList(LinkedList<String> languagesList, LinkedList<String> codesList) {
        controllerUtititesData.getLanguagesList(languagesList, codesList);
    }

    public String getTranslation(String translateFrom, String translateTo, String textFrom) {
        return controllerUtititesData.getTranslation(translateFrom, translateTo, textFrom);
    }
}
