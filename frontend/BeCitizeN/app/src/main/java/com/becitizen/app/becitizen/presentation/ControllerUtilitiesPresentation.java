package com.becitizen.app.becitizen.presentation;

import com.becitizen.app.becitizen.domain.ControllerUtilitiesDomain;

import java.util.List;

public class ControllerUtilitiesPresentation {
    private static final ControllerUtilitiesPresentation ourInstance = new ControllerUtilitiesPresentation();

    public static ControllerUtilitiesPresentation getInstance() {
        return ourInstance;
    }

    private ControllerUtilitiesPresentation() {


    }

    public void getCurrencyList(List<String> currencyList){
        ControllerUtilitiesDomain.getInstance().getCurrencyList(currencyList);
    }
}
