package com.becitizen.app.becitizen.presentation;

import com.becitizen.app.becitizen.domain.ControllerThreadDomain;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ControllerThreadPresentation {

    static ControllerThreadPresentation uniqueInstance;

    ControllerThreadDomain controllerThreadDomain;

    /**
     * Constructora privada para que no se instancie
     * y inicializar valores
     */
    private ControllerThreadPresentation() {
        controllerThreadDomain = ControllerThreadDomain.getUniqueInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return Instancia
     */
    public static ControllerThreadPresentation getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ControllerThreadPresentation();
        }

        return uniqueInstance;
    }

    /**
     * Metodo que retorna todos los threads de una categoria
     *
     * @param category name of the category
     * @return empty arraylist si ha ocurrido algun error
     */
    public ArrayList<CategoryThread> getThreadsCategory(String category) {
        JSONObject data = controllerThreadDomain.getThreadsCategory(category);
        ArrayList<CategoryThread> threads = new ArrayList<>();
        try {
            JSONArray array = (JSONArray)data.get("threads");
            for(int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                threads.add(new CategoryThread(object.getString("title"), "test", "27-04-2018 09:20:54", object.getInt("votes"), object.getInt("id")));
                object.get("title");
            }
            return threads;
        } catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Metodo que retorna todas las categorias
     *
     * @return empty arraylist si ha ocurrido algun error
     */
    public String[] getCategories() {
        JSONObject data = controllerThreadDomain.getCategories();
        String[] threads = {};
        //try {

            return threads;
        //} catch (JSONException e) {
        //    return threads;
        //}
    }

    /**
     * Metodo que crea un nuevo thread
     *
     * @return empty arraylist si ha ocurrido algun error
     */
    public boolean newThread(Thread t) throws ServerException {
        //TODO: delete line below
        //t = new Thread("hi","test", "justice");
        return controllerThreadDomain.newThread(t);
    }
}
