package com.becitizen.app.becitizen.domain;


import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.adapters.ControllerThreadData;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Comment;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ControllerThreadDomain {
    private static ControllerThreadDomain uniqueInstance;
    private ControllerThreadData controllerThreadData;

    private String PREFS_KEY = "myPreferences";

    /**
     * Constructora por defecto para evitar que sea instanciado
     **/
    private ControllerThreadDomain() {
        controllerThreadData = ControllerThreadData.getInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     * @return La instancia de ControllerThreadDomain
     */
    public static ControllerThreadDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerThreadDomain();
        return uniqueInstance;
    }

    /**
     * Metodo para obtener los posts de una categoria
     * @param category nombre de la categoria
     * @param block number of info block (each block contains x threads)
     * @return JSONObject que contiene los posts de una categoria
     */
    public ArrayList<CategoryThread> getThreadsCategory(String category, int block, boolean sortedByVotes) throws NetworkErrorException {
        try {
            JSONObject data = new JSONObject(controllerThreadData.getThreadsCategory(category.replace(" ", "%20"), block, sortedByVotes));
            ArrayList<CategoryThread> threads = new ArrayList<>();
            JSONArray array = (JSONArray)data.get("threads");
            for(int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                threads.add(new CategoryThread(object.getString("title"), object.getString("username"), object.getString("createdAt"), object.getInt("votes"), object.getInt("id")));
                object.get("title");
            }
            return threads;
        }
        catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Metodo para obtener los nombres de todas las categorias
     *
     * @return JSONObject que contiene los nombres de todas las categorias
     */
    public ArrayList<String> getCategories() throws NetworkErrorException{
        try {
            JSONObject response = new JSONObject(controllerThreadData.getCategories());
            ArrayList<String> categories = new ArrayList<String>();
            JSONArray array = (JSONArray)response.get("categories");
            for(int i = 0; i < array.length(); i++)
            {
                categories.add(array.getString(i));
            }
            return categories;
        }
        catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Metodo que crea un nuevo thread.
     *
     * @param t el thread que se quiere crear
     *
     * @return
     */
    public boolean newThread(Thread t) throws ServerException, NetworkErrorException {
        return controllerThreadData.newThread(t);
    }


    public Thread getThreadContent(int id) throws JSONException, ServerException, NetworkErrorException {
        JSONObject info = new JSONObject(controllerThreadData.getThreadContent(id));
        Thread thread = new Thread();
        if (info.get("status").equals("Ok")) {
            JSONObject threadData = info.getJSONObject("info");
            thread.setTitle(threadData.getString("title"));
            thread.setContent(threadData.getString("content"));
            thread.setCategory(threadData.getString("category"));
            thread.setAuthor(threadData.getString("username"));
            thread.setAuthorRank(threadData.getString("rank"));
            thread.setAuthorImage(Integer.valueOf(threadData.getString("profilePicture")));
            thread.setCreatedAt(threadData.getString("createdAt"));
            thread.setVotes(threadData.getInt("votes"));
            thread.setCanVote(threadData.getBoolean("canVote"));
            thread.setCanReport(threadData.getBoolean("canReport"));
        }
        else {
            if (info.get("status").equals("E2")) throw new ServerException("DB error");
        }
        return thread;
    }

    public List<Comment> getThreadComments(int id, boolean sortedByVotes) throws JSONException, ServerException, NetworkErrorException {
        JSONObject info = new JSONObject(controllerThreadData.getThreadComments(id, sortedByVotes));
        List<Comment> commentList = new ArrayList<>();
        if (info.get("status").equals("Ok")) {
            JSONArray commentsDataArray = (JSONArray)info.get("comments");
            for(int i = 0; i < commentsDataArray.length(); i++) {
                JSONObject commentData = commentsDataArray.getJSONObject(i);

                Comment comment = new Comment();
                comment.setContent(commentData.getString("content"));
                comment.setAuthor(commentData.getString("username"));
                comment.setAuthorRank(commentData.getString("rank"));
                comment.setAuthorImage(commentData.getInt("profilePicture"));
                comment.setCreatedAt(commentData.getString("createdAt"));
                comment.setVotes(commentData.getInt("votes"));
                comment.setVotable(commentData.getBoolean("canVote"));
                comment.setReportable(commentData.getBoolean("canReport"));
                comment.setId(commentData.getInt("id"));
                comment.setThreadId(id);
                commentList.add(comment);
            }
        }
        else if (info.get("status").equals("E2")) throw new ServerException("DB error");

        return commentList;
    }

    public void newComment(String commentText, int threadId) throws JSONException, ServerException, NetworkErrorException {
        JSONObject info = new JSONObject(controllerThreadData.newComment(commentText, threadId));
        if (info.get("status").equals("E2")) throw new ServerException("DB error");
        else if (info.get("status").equals("E1")) throw new ServerException("server error");
    }

    /**
     * Metodo que otorga un voto un thread por el usuario identificado.
     *
     * @param threadId  Identificador del thread que se quiere votar
     * @throws ServerException Si el servidor devuelve algún error
     * @throws JSONException Si se produce algún error al crear o leer JSONs
     */
    public void voteThread(int threadId) throws JSONException, ServerException, NetworkErrorException {
        JSONObject info = new JSONObject(controllerThreadData.voteThread(threadId));

        if (info.get("status").equals("E1")) throw new ServerException("server error");
        else if (info.get("status").equals("E2")) throw new ServerException("token error");
        else if (info.get("status").equals("E3")) throw new ServerException("thread not found");
        else if (info.get("status").equals("E4")) throw new ServerException("already voted");

    }

    /**
     * Metodo que reporta un thread por el usuario identificado.
     *
     * @param threadId  Identificador del thread que se quiere reportar
     * @throws ServerException Si el servidor devuelve algún error
     * @throws JSONException Si se produce algún error al crear o leer JSONs
     */
    public void reportThread(int threadId) throws ServerException, JSONException, NetworkErrorException {
        JSONObject info = new JSONObject(controllerThreadData.reportThread(threadId));

        if (info.get("status").equals("E1")) throw new ServerException("server error");
        else if (info.get("status").equals("E2")) throw new ServerException("token error");
        else if (info.get("status").equals("E3")) throw new ServerException("thread not found");
        else if (info.get("status").equals("E4")) throw new ServerException("already reported");
    }

    /**
     * Metodo que otorga un voto a un comentario por el usuario identificado.
     *
     * @param commentId  Identificador del comentario que se quiere votar
     * @throws ServerException Si el servidor devuelve algún error
     * @throws JSONException Si se produce algún error al crear o leer JSONs
     */
    public void voteComment(int commentId) throws ServerException, JSONException, NetworkErrorException {
        JSONObject info = new JSONObject(controllerThreadData.voteComment(commentId));

        if (info.get("status").equals("E1")) throw new ServerException("server error");
        else if (info.get("status").equals("E2")) throw new ServerException("token error");
        else if (info.get("status").equals("E3")) throw new ServerException("comment not found");
        else if (info.get("status").equals("E4")) throw new ServerException("already voted");
    }

    /**
     * Metodo que reporta un comentario por el usuario identificado.
     *
     * @param commentId  Identificador del comentario que se quiere reportar
     * @throws ServerException Si el servidor devuelve algún error
     * @throws JSONException Si se produce algún error al crear o leer JSONs
     */
    public void reportComment(int commentId) throws ServerException, JSONException, NetworkErrorException {
        JSONObject info = new JSONObject(controllerThreadData.reportComment(commentId));

        if (info.get("status").equals("E1")) throw new ServerException("server error");
        else if (info.get("status").equals("E2")) throw new ServerException("token error");
        else if (info.get("status").equals("E3")) throw new ServerException("comment not found");
        else if (info.get("status").equals("E4")) throw new ServerException("already reported");
    }


    public ArrayList<CategoryThread> getThreadsCategorySearch(String category, int block, boolean sortedByVotes, String searchWords) {
        try {
            JSONObject data = new JSONObject(controllerThreadData.getThreadsCategorySearch(category.replace(" ", "%20"), block, sortedByVotes, searchWords.replace(" ", "+")));
            ArrayList<CategoryThread> threads = new ArrayList<>();
            JSONArray array = (JSONArray)data.get("threads");
            for(int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                threads.add(new CategoryThread(object.getString("title"), object.getString("username"), object.getString("createdAt"), object.getInt("votes"), object.getInt("id")));
                object.get("title");
            }
            return threads;
        }
        catch (JSONException e) {
            return new ArrayList<>();
        }
    }
}
