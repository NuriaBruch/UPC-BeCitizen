package com.becitizen.app.becitizen.domain.controllers;


import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.data.ControllerFaqData;
import com.becitizen.app.becitizen.domain.MySharedPreferences;
import com.becitizen.app.becitizen.domain.entities.FaqEntry;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;


public class ControllerFaqDomain {
    private static ControllerFaqDomain uniqueInstance;
    private ControllerFaqData controllerFaqData;
    private Context context;

    private String PREFS_KEY = "myPreferences";

    /**
     * Constructora por defecto para evitar que sea instanciado
     **/
    private ControllerFaqDomain() {
        controllerFaqData = ControllerFaqData.getInstance();
    }

    public void setContext(Context context) {
        this.context = context;
        controllerFaqData.setContext(context);
    }

    /**
     * Metodo para obtener la instancia del singleton
     * @return La instancia de ControllerForumDomain
     */
    public static ControllerFaqDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerFaqDomain();
        return uniqueInstance;
    }

    /**
     * Metodo para obtener las faqs de una categoria
     * @param category nombre de la categoria
     * @return ArrayList<FaqEntry> que contiene las faqs de una categoria
     */
    public ArrayList<FaqEntry> getFaqsCategory(String category) {
        return (ArrayList<FaqEntry>)controllerFaqData.getFaqByCategoryFromDB(category);
    }

    /**
     * Metodo para obtener las faqs de una categoria
     * @param category nombre de la categoria
     * @return ArrayList<FaqEntry> que contiene las faqs de una categoria
     */
    public ArrayList<FaqEntry> getFaqsCategoryWord(String category, String word) {
        return (ArrayList<FaqEntry>)controllerFaqData.getFaqByKeyWordAndCategory(category, '%'+word+'%');
    }

    /**
     * Metodo para obtener los nombres de todas las categorias de la base de datos
     *
     * @return JSONObject que contiene los nombres de todas las categorias
     */
    public ArrayList<String> getCategoriesDB(){
        return (ArrayList<String>) controllerFaqData.getFaqCategoriesFromDB();
    }

    /**
     * Metodo para obtener los nombres de todas las categorias del servidor
     *
     * @return JSONObject que contiene los nombres de todas las categorias
     */
    public ArrayList<String> getCategoriesServer() {
        try {
            JSONObject response = new JSONObject(controllerFaqData.getCategories());
            ArrayList<String> categories = new ArrayList<String>();
            JSONArray array = (JSONArray)response.get("categories");
            for(int i = 0; i < array.length(); i++)
            {
                categories.add(array.getString(i));
            }
            return categories;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Metodo para obtener los nombres de todas las categorias
     *
     * @return JSONObject que contiene los nombres de todas las categorias
     */
    public ArrayList<String> getCategories() {
        try {
            MySharedPreferences preferences = MySharedPreferences.getInstance();
            if (preferences.getDate(PREFS_KEY, "lastRefresh") == null || preferences.getDate(PREFS_KEY, "lastRefresh").isBefore(DateTime.now().minusHours(12))) {
                refreshData();
                preferences.saveDate(PREFS_KEY, "lastRefresh", DateTime.now());
            }
        } catch (Exception e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, context.getResources().getString(R.string.usingOffline), Toast.LENGTH_SHORT).show();
                }
            }, 100 );
        }
        return getCategoriesDB();
    }

    /**
     * Metodo para valorar una faq
     * @param id de la faq
     * @param rating valoracion del usuario
     * @return false si ha ocurrido algun error
     */
    public boolean rateFaq(int id, int rating) throws NetworkErrorException{
        try {
            JSONObject data = new JSONObject(controllerFaqData.rateFaq(id, rating));
            if (data.getString("status").equals("Ok")) return true;
            else if (data.getString("status").equals("E3")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, context.getResources().getString(R.string.alreadyReported), Toast.LENGTH_SHORT).show();
                    }
                }, 100 );
                return true;
            }
            else return false;
        } catch (JSONException e) {
            return false;
        }
    }

    public void refreshData() throws Exception{
        for (String cat : getCategoriesServer()) {
            ArrayList<FaqEntry> entries = new ArrayList<>();
            JSONObject data = new JSONObject(controllerFaqData.getFaqCategory((cat.replace(" ", "%20"))));
            JSONArray array = (JSONArray)data.get("faqs");
            for(int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                entries.add(new FaqEntry(object.getInt("id"), object.getString("question"), object.getString("answer"), BigDecimal.valueOf(object.getDouble("puntuation")).floatValue(), cat));
            }
            controllerFaqData.insertMultiplesFaqsOnDB(entries);
        }
    }

    /**
     * Metodo para reportar una faq
     * @param id de la faq
     * @return false si ha ocurrido algun error
     */
    public boolean reportFaq(int id) {
        try {
            JSONObject data = new JSONObject(controllerFaqData.reportFaq(id));
            if (data.getString("status").equals("Ok")) return true;
            else if (data.getString("status").equals("E3")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, context.getResources().getString(R.string.alreadyReported), Toast.LENGTH_SHORT).show();
                    }
                }, 100 );
                return true;
            }
            else return false;
        } catch (JSONException e) {
            return false;
        }
    }
}
