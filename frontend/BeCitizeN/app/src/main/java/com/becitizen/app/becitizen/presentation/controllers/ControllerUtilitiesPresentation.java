package com.becitizen.app.becitizen.presentation.controllers;

import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.controllers.ControllerUtilitiesDomain;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;

import java.util.List;

public class ControllerUtilitiesPresentation {
    private static final ControllerUtilitiesPresentation ourInstance = new ControllerUtilitiesPresentation();

    public static ControllerUtilitiesPresentation getInstance() {
        return ourInstance;
    }

    private ControllerUtilitiesPresentation() {


    }

    public void getCurrencyList(List<String> currencyList) throws NetworkErrorException {
        ControllerUtilitiesDomain.getInstance().getCurrencyList(currencyList);
    }

    public double getConversion(String currencyFrom, String currencyTo, String amount) throws NetworkErrorException {
        return ControllerUtilitiesDomain.getInstance().getConversion(currencyFrom,currencyTo,amount);
    }

    public String[] getWordOfTheDay() throws ServerException, JSONException, NetworkErrorException {
        return ControllerUtilitiesDomain.getInstance().getWordOfTheDay();
    }
}
