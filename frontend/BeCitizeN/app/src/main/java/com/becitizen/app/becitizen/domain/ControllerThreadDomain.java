package com.becitizen.app.becitizen.domain;


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
     *
     **/
    private ControllerThreadDomain() {
        controllerThreadData = ControllerThreadData.getInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerThreadDomain
     */
    public static ControllerThreadDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerThreadDomain();
        return uniqueInstance;
    }

    /**
     * Metodo para obtener los posts de una categoria
     *
     * @param category nombre de la categoria
     *
     * @return JSONObject que contiene los posts de una categoria
     */
    public ArrayList<CategoryThread> getThreadsCategory(String category) {
        try {
            JSONObject data = new JSONObject(controllerThreadData.getThreadsCategory(category));
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
    public JSONObject getCategories() {
        try {
            JSONObject response = new JSONObject(controllerThreadData.getCategories());
            return response;
        }
        catch (JSONException e) {
            return null;
        }
    }

    /**
     * Metodo que crea un nuevo thread.
     *
     * @param t el thread que se quiere crear
     *
     * @return
     */
    public boolean newThread(Thread t) throws ServerException {
        return controllerThreadData.newThread(t);
    }

    public Thread getThreadContent(int id) throws JSONException {
        JSONObject info = new JSONObject(controllerThreadData.getThreadContent(id));
        Thread thread = new Thread();
        if (info.get("status").equals("Ok")) {
            thread.setTitle(info.getString("title"));
            thread.setContent(info.getString("content"));
            thread.setCategory(info.getString("category"));
            thread.setAuthor(info.getString("username"));
            thread.setAuthorRank(info.getString("rank"));
            thread.setAuthorImage(Integer.valueOf(info.getString("profilePicture")));
            thread.setCreatedAt(info.getString("createdAt"));
            thread.setVotes(info.getInt("votes"));
            thread.setCanVote(info.getBoolean("canVote"));
            thread.setCanReport(info.getBoolean("canReport"));
        }
        //TODO throw new exception

        return thread;
    }

    public List<Comment> getThreadComments(int id) throws JSONException {
        JSONObject info = new JSONObject(controllerThreadData.getThreadComments(id));
        List<Comment> commentList = new ArrayList<>();
        if (info.get("status").equals("Ok")) {
            JSONArray commentsDataArray = (JSONArray)info.get("comments");
            for(int i = 0; i < commentsDataArray.length(); i++) {
                JSONObject commentData = commentsDataArray.getJSONObject(i);

                Comment comment = new Comment();
                comment.setContent(commentData.getString("content"));
                comment.setAuthor(commentData.getString("username"));
                comment.setAuthorRank(commentData.getString("rank"));
                comment.setAuthorImage(Integer.valueOf(commentData.getString("profilePicture")));
                comment.setCreatedAt(commentData.getString("createdAt"));
                comment.setVotes(commentData.getInt("votes"));
                comment.setVotable(commentData.getBoolean("canVote"));
                comment.setReportable(commentData.getBoolean("canReport"));
                commentList.add(comment);
            }
        }
        //TODO throw new exception


       return commentList;

    }

    public void newComment(String commentText, int threadId) {
        controllerThreadData.newComment(commentText, threadId);
        /*
        if (!info.get("status").equals("Ok")) {
            // TODO throw exception.
        }
        */
    }


}
