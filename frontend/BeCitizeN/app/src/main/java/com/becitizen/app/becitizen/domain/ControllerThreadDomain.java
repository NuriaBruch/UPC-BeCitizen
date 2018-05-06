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
            JSONObject data = new JSONObject(controllerThreadData.getThreadsCategory(category.replace(" ", "%20")));
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
    public ArrayList<String> getCategories() {
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
    public boolean newThread(Thread t) throws ServerException {
        return controllerThreadData.newThread(t);
    }


    public Thread getThreadContent(int id) throws JSONException, ServerException {
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

    public List<Comment> getThreadComments(int id) throws JSONException, ServerException {
        JSONObject info = new JSONObject(controllerThreadData.getThreadComments(id));
        List<Comment> commentList = new ArrayList<>();
        if (info.get("status").equals("Ok")) {
            JSONArray commentsDataArray = (JSONArray)info.get("comment");
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
                commentList.add(comment);
            }
        }
        else if (info.get("status").equals("E2")) throw new ServerException("DB error");

       return commentList;
    }

    public void newComment(String commentText, int threadId) throws JSONException, ServerException {
        JSONObject info = new JSONObject(controllerThreadData.newComment(commentText, threadId));
        if (info.get("status").equals("E2")) throw new ServerException("DB error");
        else if (info.get("status").equals("E1")) throw new ServerException("server error");
    }

    public void voteThread(int threadId) throws JSONException, ServerException {
        JSONObject info = new JSONObject(controllerThreadData.voteThread(threadId));

        if (info.get("status").equals("E1")) throw new ServerException("server error");
        else if (info.get("status").equals("E2")) throw new ServerException("token error");
        else if (info.get("status").equals("E3")) throw new ServerException("thread not found");
        else if (info.get("status").equals("E4")) throw new ServerException("already voted");

    }

    public void reportThread(int threadId) throws ServerException, JSONException {
        JSONObject info = new JSONObject(controllerThreadData.reportThread(threadId));

        if (info.get("status").equals("E1")) throw new ServerException("server error");
        else if (info.get("status").equals("E2")) throw new ServerException("token error");
        else if (info.get("status").equals("E3")) throw new ServerException("thread not found");
        else if (info.get("status").equals("E4")) throw new ServerException("already reported");
    }

    public void voteComment(int commentId) throws ServerException, JSONException {
        JSONObject info = new JSONObject(controllerThreadData.voteComment(commentId));

        if (info.get("status").equals("E1")) throw new ServerException("server error");
        else if (info.get("status").equals("E2")) throw new ServerException("token error");
        else if (info.get("status").equals("E3")) throw new ServerException("comment not found");
        else if (info.get("status").equals("E4")) throw new ServerException("already voted");
    }

    public void reportComment(int commentId) throws ServerException, JSONException {
        JSONObject info = new JSONObject(controllerThreadData.reportComment(commentId));

        if (info.get("status").equals("E1")) throw new ServerException("server error");
        else if (info.get("status").equals("E2")) throw new ServerException("token error");
        else if (info.get("status").equals("E3")) throw new ServerException("comment not found");
        else if (info.get("status").equals("E4")) throw new ServerException("already reported");
    }


}
