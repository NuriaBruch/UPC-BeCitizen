package com.becitizen.app.becitizen.data;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerMsgData {

    //URIs
    private static final String URI_BCN = "https://becitizen.cf";

    private static final String URI_CONVERSATIONS = URI_BCN + "/conversations";
    private static final String URI_NEW_CONVERSATION = URI_BCN + "/conversation";
    private static final String URI_NEW_MESSAGE = URI_BCN + "/message";
    private static final String URI_MESSAGES = URI_BCN + "/conversationMessages";

    // instance
    private static ControllerMsgData instance;

    protected ControllerMsgData() {
    }

    public static ControllerMsgData getInstance() {
        if (instance == null) instance = new ControllerMsgData();
        return instance;
    }

    public JSONObject getConversations() throws ServerException, NetworkErrorException {
        String json = ServerAdapter.getInstance().doGetRequest(URI_CONVERSATIONS);
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            Log.e("SERVER_RESPONSE", json);
            e.printStackTrace();
            throw new ServerException("The server has not returned a JSONObject");
        }
    }


    public void newMessage(int id, String s) {
        JSONObject json = new JSONObject();
        try {
            json.put("idConver", id);
            json.put("content", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] dataRequest = {URI_NEW_MESSAGE, json.toString()};

        String response = ServerAdapter.getInstance().doPostRequest(dataRequest);
        Log.d("Response", response);
    }

    public JSONObject getConversation(String email) throws ServerException {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] dataRequest = {URI_NEW_CONVERSATION, json.toString()};

        String response = ServerAdapter.getInstance().doPostRequest(dataRequest);
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            Log.e("SERVER_RESPONSE", response);
            e.printStackTrace();
            throw new ServerException("The server has not returned a JSONObject");
        }
    }

    public JSONObject getMessages(int conversationId) throws ServerException {
        String response = ServerAdapter.getInstance().doGetRequest(URI_MESSAGES + "?idConver=" + conversationId);
        Log.d("Conversation id", conversationId + "");
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            Log.e("SERVER_RESPONSE", response);
            e.printStackTrace();
            throw new ServerException("The server has not returned a JSONObject");
        }
    }
}
