package com.becitizen.app.becitizen.domain.adapters;

import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerThreadData {

    //URIs
    private static final String URI_THREADS_CATEGORY = "http://becitizen.cf/getAllThreadsCategory?category=";
    private static final String URI_CATEGORIES = "http://becitizen.cf/categories";
    private static final String URI_NEW_THREAD = "http://becitizen.cf/newThread";
    private static final String URI_THREAD_CONTENT = "http://becitizen.cf/getThread?threadId=";
    private static final String URI_THREAD_COMMENTS = "http://becitizen.cf/getThreadComments?&threadId=";
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
        return ServerAdapter.getInstance().doGetRequest(URI_CATEGORIES);
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
        return ServerAdapter.getInstance().doGetRequest(URI_THREAD_COMMENTS + String.valueOf(id));
    }

    public String newComment(String commentText, int threadId) {
        JSONObject json = new JSONObject();
        try {
            json.put("content", commentText);
            json.put("threadId", threadId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] dataRequest = {URI_NEW_COMMENT, json.toString()};
        return ServerAdapter.getInstance().doPostRequest(dataRequest);
    }

    /**
     * Metodo que otorga un voto un thread por el usuario identificado.
     *
     * @param threadId  Identificador del thread que se quiere votar
     * @return Server response
     */
    public String voteThread(int threadId) {
        JSONObject json = new JSONObject();
        String[] dataRequest = {URI_VOTE_THREAD + "?threadId=" + threadId, json.toString()};
        return ServerAdapter.getInstance().doPutRequest(dataRequest);
    }

    /**
     * Metodo que reporta un thread por el usuario identificado.
     *
     * @param threadId  Identificador del thread que se quiere reportar
     * @return Server response
     */
    public String reportThread(int threadId) {
        JSONObject json = new JSONObject();
        String[] dataRequest = {URI_REPORT_THREAD + "?threadId=" + threadId, json.toString()};
        return ServerAdapter.getInstance().doPutRequest(dataRequest);
    }

    /**
     * Metodo que otorga un voto a un comentario por el usuario identificado.
     *
     * @param commentId  Identificador del comentario que se quiere votar
     * @return Server response
     */
    public String voteComment(int commentId) {
        JSONObject json = new JSONObject();
        String[] dataRequest = {URI_VOTE_COMMENT + "?commentId=" + commentId, json.toString()};
        return ServerAdapter.getInstance().doPutRequest(dataRequest);
    }

    /**
     * Metodo que reporta un comentario por el usuario identificado.
     *
     * @param commentId  Identificador del comentario que se quiere reportar
     * @return Server response
     */
    public String reportComment(int commentId) {
        JSONObject json = new JSONObject();
        String[] dataRequest = {URI_REPORT_COMMENT + "?commentId=" + commentId, json.toString()};
        return ServerAdapter.getInstance().doPutRequest(dataRequest);
    }

}
