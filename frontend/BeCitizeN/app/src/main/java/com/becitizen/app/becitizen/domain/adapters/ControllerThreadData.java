package com.becitizen.app.becitizen.domain.adapters;

import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerThreadData {

    //URIs
    private static final String URI_THREADS_CATEGORY = "http://becitizen.cf/getAllThreadsCategory?category=";
    private static final String URI_NEW_THREAD = "http://becitizen.cf/newThread";

    private static ControllerThreadData instance = null;

    /**
     * Constructora por defecto para evitar que sea instanciado
     */
    protected ControllerThreadData() {
        // Exists only to defeat instantiation.
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerThreadData
     */
    public static ControllerThreadData getInstance() {
        if(instance == null) instance = new ControllerThreadData();
        return instance;
    }

    /**
     * Metodo que solicita los threads de una categoria.
     *
     * @param category nombre de la categoria
     *
     * @return La respuesta de nuestro servidor
     */
    public String getThreadsCategory(String category) {
        return ServerAdapter.getInstance().doGetRequest(URI_THREADS_CATEGORY + category);
    }

    /**
     * Metodo que solicita los nombres de todas las categorias.
     *
     * @return La respuesta de nuestro servidor
     */
    public String getCategories() {
        return ServerAdapter.getInstance().doGetRequest(URI_THREADS_CATEGORY);
    }

    /**
     * Metodo que crea un nuevo thread.
     *
     * @param t el thread que se quiere crear
     *
     * @return La respuesta de nuestro servidor
     */
    public boolean newThread(Thread t) throws ServerException {
        JSONObject json = new JSONObject();
        try {
            json.put("title", t.getTitle());
            json.put("content", t.getContent());
            json.put("category", t.getCategory());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] dataRequest = {URI_NEW_THREAD, json.toString()};
        try {
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doPostRequest(dataRequest));
            if (info.get("status").equals("Ok")) {
                return true;
            }
            else if (info.get("status").equals("E1")) throw new ServerException("server error");
            else throw new ServerException("DB error");
        }
        catch (JSONException e) {
            // TODO gestionar errors.
            return false;
        }
    }
}
