package com.becitizen.app.becitizen.data;

import android.accounts.NetworkErrorException;


import com.becitizen.app.becitizen.domain.entities.ForumThread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerForumData {

    //URIs

    private static final String URI_BCN = "https://becitizen.cf";

    private static final String URI_THREADS_CATEGORY = URI_BCN + "/getAllThreadsCategory?category=";
    public static final String URI_CATEGORIES = URI_BCN + "/categories";
    private static final String URI_NEW_THREAD = URI_BCN + "/newThread";
    private static final String URI_THREAD_CONTENT = URI_BCN + "/getThread?threadId=";
    private static final String URI_THREAD_COMMENTS = URI_BCN + "/getThreadComments?&threadId=";
    private static final String URI_NEW_COMMENT = URI_BCN + "/newComment";
    private static final String URI_VOTE_THREAD = URI_BCN + "/voteThread";
    private static final String URI_VOTE_COMMENT = URI_BCN + "/voteComment";
    private static final String URI_REPORT_THREAD = URI_BCN + "/reportThread";
    private static final String URI_REPORT_COMMENT = URI_BCN + "/reportComment";
    private static final String URI_THREADS_CATEGORY_SEARCH = URI_BCN + "/getThreadWords?category=";


    private static ControllerForumData instance = null;

    /**
     * Constructora por defecto para evitar que sea instanciado
     */
    protected ControllerForumData() {
        // Exists only to defeat instantiation.
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerForumData
     */
    public static ControllerForumData getInstance() {
        if(instance == null) instance = new ControllerForumData();
        return instance;
    }

    /**
     * Metodo que solicita los threads de una categoria.
     *
     * @param category nombre de la categoria
     *
     * @return La respuesta de nuestro servidor
     */
    public String getThreadsCategory(String category, int block, boolean sortedByVotes) throws NetworkErrorException {
        return ServerAdapter.getInstance().doGetRequest(URI_THREADS_CATEGORY + category + "&block=" + block + "&sortedByVotes=" + String.valueOf(sortedByVotes));
    }

    /**
     * Metodo que solicita los nombres de todas las categorias.
     *
     * @return La respuesta de nuestro servidor
     */
    public String getCategories() throws NetworkErrorException{
        return ServerAdapter.getInstance().doGetRequest(URI_CATEGORIES);
    }

    /**
     * Metodo que crea un nuevo thread.
     *
     * @param t el thread que se quiere crear
     *
     * @return La respuesta de nuestro servidor
     */
    public boolean newThread(ForumThread t) throws ServerException, NetworkErrorException {
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


    public String getThreadContent(int id) throws NetworkErrorException{
        return ServerAdapter.getInstance().doGetRequest(URI_THREAD_CONTENT + String.valueOf(id));
    }

    public String getThreadComments(int id, boolean sortedByVotes) throws NetworkErrorException{
        return ServerAdapter.getInstance().doGetRequest(URI_THREAD_COMMENTS + String.valueOf(id) + "&sortedByVotes="+ String.valueOf(sortedByVotes));
    }

    public String newComment(String commentText, int threadId) throws NetworkErrorException{
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
    public String voteThread(int threadId) throws NetworkErrorException{
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
    public String reportThread(int threadId) throws NetworkErrorException{
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
    public String voteComment(int commentId) throws NetworkErrorException{
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
    public String reportComment(int commentId) throws NetworkErrorException{
        JSONObject json = new JSONObject();
        String[] dataRequest = {URI_REPORT_COMMENT + "?commentId=" + commentId, json.toString()};
        return ServerAdapter.getInstance().doPutRequest(dataRequest);
    }

    public String getThreadsCategorySearch(String category, int block, boolean sortedByVotes, String searchWords) {
        return ServerAdapter.getInstance().doGetRequest(URI_THREADS_CATEGORY_SEARCH + category + "&block=" + block + "&sortedByVotes=" + String.valueOf(sortedByVotes) + "&words=" + searchWords);
    }
}


