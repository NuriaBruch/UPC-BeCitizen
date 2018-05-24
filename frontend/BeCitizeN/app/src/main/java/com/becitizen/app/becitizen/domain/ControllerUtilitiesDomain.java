package com.becitizen.app.becitizen.domain;

import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.adapters.ControllerUtititesData;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;

import java.util.List;

public class ControllerUtilitiesDomain {
    private static final ControllerUtilitiesDomain ourInstance = new ControllerUtilitiesDomain();

    public static ControllerUtilitiesDomain getInstance() {
        return ourInstance;
    }

    private ControllerUtilitiesDomain() {
    }

    public void getCurrencyList(List<String> currencyList) throws NetworkErrorException{
        ControllerUtititesData.getInstance().getCurrencyList(currencyList);
    }

    public double getConversion(String currencyFrom, String currencyTo, String amount) throws NetworkErrorException{
        return ControllerUtititesData.getInstance().getConversion(currencyFrom,currencyTo,amount);
    }

    public String[] getWordOfTheDay() throws ServerException, JSONException, NetworkErrorException {
        return ControllerUtititesData.getInstance().getWordOfTheDay();
    }
}
