package com.becitizen.app.becitizen.domain;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.becitizen.app.becitizen.domain.adapters.ControllerMsgData;
import com.becitizen.app.becitizen.domain.entities.Conversation;
import com.becitizen.app.becitizen.domain.entities.Message;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControllerMsgDomain {

    private static ControllerMsgDomain instance;

    protected ControllerMsgDomain() {
    }

    public static ControllerMsgDomain getInstance() {
        if (instance == null) instance = new ControllerMsgDomain();
        return instance;
    }

    public Conversation getConversation() {
        // TODO: Conversation is harcoded
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(true, new Date(18,5,16,9,45, 30), "Muchas felicidades :)"));
        messages.add(new Message(true, new Date(18,5,16,9,45, 49), "Qué tal todo?"));
        messages.add(new Message(true, new Date(18,5,16,9,46, 50), "A ver si quedamos, que hace mucho que no nos vemos!"));
        messages.add(new Message(true, new Date(18,5,16,9,47, 36), "Disfruta de tu día"));
        messages.add(new Message(false, new Date(18,5,16,22,57, 10), "Gracias"));

        return new Conversation(1, 2, "Alex", new Date(18,5,16,22,57, 10), messages);
    }

    public List<Conversation> getConversations() throws ServerException, NetworkErrorException {
        JSONObject json = ControllerMsgData.getInstance().getConversations();

        try {
            if (!json.getString("status").equals("Ok")) {
                Log.e("SERVER_ERRORS", json.getJSONArray("errors").toString());
                throw new ServerException("Server has rerturned errors");
            } else {
                JSONArray jsonArray = json.getJSONArray("conversations");
                List<Conversation> conversations = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    if(!c.isNull("lastMessageTime"))
                        conversations.add(new Conversation(
                            c.getInt("id"),
                            c.getInt("profilePicture"),
                            c.getString("username"),
                            new Date(c.getString("lastMessageTime"))));
                }

                return conversations;
            }
        } catch (JSONException e) {
            Log.e("SERVER_RESPONSE", json.toString());
            e.printStackTrace();
            throw new ServerException("Server has not returned the expected JSON!");
        }
    }
}
