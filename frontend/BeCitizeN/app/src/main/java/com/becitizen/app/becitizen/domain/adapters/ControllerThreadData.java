package com.becitizen.app.becitizen.domain.adapters;

import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerThreadData {

    //URIs
    private static final String URI_THREADS_CATEGORY = "http://becitizen.cf/getAllThreadsCategory?category=";
    private static final String URI_NEW_THREAD = "http://becitizen.cf/newThread";
    private static final String URI_THREAD_CONTENT = "http://becitizen.cf/getThread?threadId=";
    private static final String URI_THREAD_COMMENTS = "http://becitizen.cf/getThreadComments";
    private static final String URI_NEW_COMMENT = "http://becitizen.cf/newComment";
    private static final String URI_VOTE_THREAD = "http://becitizen.cf/voteThread";
    private static final String URI_VOTE_COMMENT = "http://becitizen.cf/voteComment";
    private static final String URI_REPORT_THREAD = "http://becitizen.cf/reportThread";
    private static final String URI_REPORT_COMMENT = "http://becitizen.cf/reportComment";


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


    public String getThreadContent(int id) {
        return ServerAdapter.getInstance().doGetRequest(URI_THREAD_CONTENT + String.valueOf(id));
    }

    public String getThreadComments(int id) {
        /*
        String threadComments = ServerAdapter.getInstance().doGetRequest(URI_THREAD_COMMENTS + "?email=" + token + "&threadId=" + id);
        return threadComments;
        */

        // TODO UNCOMMENT
        return "test";
    }

    public void newComment(String commentText, int threadId) {
        JSONObject json = new JSONObject();
        try {
            json.put("content", commentText);
            json.put("threadId", threadId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] dataRequest = {URI_NEW_COMMENT, json.toString()};
        ServerAdapter.getInstance().doPostRequest(dataRequest);
    }

    public void voteThread(int threadId) {
        JSONObject json = new JSONObject();
        try {
            json.put("threadId", threadId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] dataRequest = {URI_VOTE_THREAD, json.toString()};
        ServerAdapter.getInstance().doPutRequest(dataRequest);
    }

    public void reportThread(int threadId) {
        JSONObject json = new JSONObject();
        try {
            json.put("threadId", threadId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] dataRequest = {URI_REPORT_THREAD, json.toString()};
        ServerAdapter.getInstance().doPutRequest(dataRequest);
    }

    public void voteComment(int commentId) {
        JSONObject json = new JSONObject();
        try {
            json.put("commentId", commentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] dataRequest = {URI_VOTE_COMMENT, json.toString()};
        ServerAdapter.getInstance().doPutRequest(dataRequest);
    }

}
