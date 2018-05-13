package com.becitizen.app.becitizen.domain;

import com.becitizen.app.becitizen.domain.adapters.ControllerUtititesData;

import java.util.List;

public class ControllerUtilitiesDomain {
    private static final ControllerUtilitiesDomain ourInstance = new ControllerUtilitiesDomain();

    public static ControllerUtilitiesDomain getInstance() {
        return ourInstance;
    }

    private ControllerUtilitiesDomain() {
    }

    public void getCurrencyList(List<String> currencyList){
        ControllerUtititesData.getInstance().getCurrencyList(currencyList);
    }
}
