package com.becitizen.app.becitizen.domain.adapters;

import android.util.Log;

import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerMsgData {

    //URIs
    private static final String URI_BCN = "http://becitizen.cf";

    private static final String URI_CONVERSATIONS = URI_BCN + "/conversations";
    private static final String URI_NEW_CONVERSATION = URI_BCN + "/conversation";

    // instance
    private static ControllerMsgData instance;

    protected ControllerMsgData() {
    }

    public static ControllerMsgData getInstance() {
        if (instance == null) instance = new ControllerMsgData();
        return instance;
    }

    public JSONObject getConversations() throws ServerException {
        String json = ServerAdapter.getInstance().doGetRequest(URI_CONVERSATIONS);
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            Log.e("SERVER_RESPONSE", json);
            e.printStackTrace();
            throw new ServerException("The server has not returned a JSONObject");
        }
    }


}
