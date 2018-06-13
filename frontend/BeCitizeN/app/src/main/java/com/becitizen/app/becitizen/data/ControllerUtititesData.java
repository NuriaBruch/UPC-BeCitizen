package com.becitizen.app.becitizen.data;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.becitizen.app.becitizen.exceptions.ServerException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ControllerUtititesData {
    private static final ControllerUtititesData ourInstance = new ControllerUtititesData();

    private static final String URI_GET_ALL_CURRENCIES = "http://becitizen.cf/getAllCurrencies";
    private static final String URI_GET_EXCHANGE = "http://becitizen.cf/getExchange";
    private static final String URI_GET_WORD = "http://becitizen.cf/getWord";
    private static final String URI_ALL_LANGUAGES = "http://becitizen.cf/getLanguageCodes";
    private static final String URI_TRANSLATION = "http://becitizen.cf/getTranslation";

    public static ControllerUtititesData getInstance() {
        return ourInstance;
    }

    private ControllerUtititesData() {
    }

    public void getCurrencyList(List<String> currencyList) throws NetworkErrorException {
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

    public double getConversion(String currencyFrom, String currencyTo, String amount) throws NetworkErrorException{
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

    public String[] getWordOfTheDay() throws JSONException, ServerException, NetworkErrorException{
        JSONObject info = new JSONObject(ServerAdapter.getInstance().doGetRequest(URI_GET_WORD));
        if (info.get("status").equals("Ok")) {
            JSONObject data = info.getJSONObject("info");
            return new String[]{data.getString("word"), data.getString("definition")};

        }
        else if (info.get("status").equals("E1")) throw new ServerException("Server error");
        else if (info.get("status").equals("E2")) throw new ServerException("Scrapping error");
        else if (info.get("status").equals("E3")) throw new ServerException("Missing words in database");
        else if (info.get("status").equals("E23")) throw new ServerException("Scrapping error and missing words in database");
        else throw new ServerException("Error");
    }

    public void getLanguagesList(LinkedList<String> languagesList, LinkedList<String> codesList) {
        try {
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doGetRequest(URI_ALL_LANGUAGES));
            if (info.get("status").equals("Ok")) {
                JSONArray languagesInfo = (JSONArray)info.get("currencies");
                for(int i = 0; i < languagesInfo.length(); i++){
                    JSONObject language = languagesInfo.getJSONObject(i);
                    languagesList.add(language.getString("name"));
                    codesList.add(language.getString("code"));
                }
            }
            else Log.d("hald","baia plaia");
        }
        catch (JSONException e) {
            // TODO gestionar errors.
        }
    }

    public String getTranslation(String translateFrom, String translateTo, String textFrom) {
        try {
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doGetRequest(URI_TRANSLATION + "?text=" + textFrom + "&from=" + translateFrom + "&to=" + translateTo));
            if(info.get("status").equals("Ok")) {
                if (info.get("info") != null) return info.getString("info");
            }
            return "";
        }

        catch (JSONException e) {
            return "";
        }
    }
}
