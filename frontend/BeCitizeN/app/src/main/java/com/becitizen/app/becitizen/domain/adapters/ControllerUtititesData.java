package com.becitizen.app.becitizen.domain.adapters;

import android.util.Log;

import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ControllerUtititesData {
    private static final String URI_GET_ALL_CURRENCIES = "http://becitizen.cf/getAllCurrencies";
    private static final String URI_GET_EXCHANGE = "http://becitizen.cf/getExchange";
    private static final ControllerUtititesData ourInstance = new ControllerUtititesData();

    public static ControllerUtititesData getInstance() {
        return ourInstance;
    }

    private ControllerUtititesData() {
    }

    public void getCurrencyList(List<String> currencyList){
        try {
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doGetRequest(URI_GET_ALL_CURRENCIES));
            if (info.get("status").equals("Ok")) {
                JSONArray currenciesDataArray = (JSONArray)info.get("currencies");
                for(int i = 0; i < currenciesDataArray.length(); i++){
                   currencyList.add(currenciesDataArray.getString(i));
                }
            }
            else Log.d("hald","baia plaia");
        }
        catch (JSONException e) {
            // TODO gestionar errors.
            return;
        }

    }

    public double getConversion(String currencyFrom, String currencyTo, String amount) {
        try{
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doGetRequest(URI_GET_EXCHANGE+"?currencyFrom="+currencyFrom+"&currencyTo="+currencyTo+"&amount="+amount));
            if(info.get("conversion") != null)
                return info.getDouble("conversion");
            else return 0;
        }
        catch (JSONException e){
            return 0;
        }
    }
}
