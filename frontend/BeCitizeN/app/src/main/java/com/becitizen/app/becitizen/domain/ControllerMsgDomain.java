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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ControllerMsgDomain {

    private static ControllerMsgDomain instance;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    protected ControllerMsgDomain() {
    }

    public static ControllerMsgDomain getInstance() {
        if (instance == null) instance = new ControllerMsgDomain();
        return instance;
    }

    public Conversation getConversation(String email) throws ServerException {
        JSONObject json = ControllerMsgData.getInstance().getConversation(email);

        try {
            if (!json.getString("status").equalsIgnoreCase("OK")) {
                Log.e("SERVER_ERRORS", json.getJSONArray("errors").toString());
                throw new ServerException("Server has rerturned errors");
            } else {
                return new Conversation(
                        json.getInt("conversationId"),
                        json.getString("username"),
                        json.getString("name"),
                        json.getInt("profilePicture"));
            }
        } catch (JSONException e) {
            Log.e("SERVER_RESPONSE", json.toString());
            e.printStackTrace();
            throw new ServerException("Server has not returned the expected JSON!");
        }
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
                                c.getString("name"),
                                c.getString("username"),
                                c.getInt("profilePicture"),
                                c.getBoolean("newMessage"),
                                c.getString("lastMessageContent"),
                                dateFormat.parse(c.getString("lastMessageTime"))));
                }

                return conversations;
            }
        } catch (JSONException e) {
            Log.e("SERVER_RESPONSE", json.toString());
            e.printStackTrace();
            throw new ServerException("Server has not returned the expected JSON!");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ServerException("Error formatting date.");
        }
    }

    public List<Message> getMessages(int conversationId) throws ServerException {
        JSONObject json = ControllerMsgData.getInstance().getMessages(conversationId);

        try {
            if (!json.getString("status").equalsIgnoreCase("OK")) {
                Log.e("SERVER_ERRORS", json.getJSONArray("errors").toString());
                throw new ServerException("Server has rerturned errors");
            } else {
                JSONArray jsonArray = json.getJSONArray("messages");
                List<Message> messages = new ArrayList<>();

                for (int i = jsonArray.length()-1; i >= 0; i--) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    if(!c.isNull("date") && c.getString("content").length()>0) {
                        try {
                            messages.add(new Message(
                                    c.getBoolean("sendedByMe"),
                                    dateFormat.parse(c.getString("date")),
                                    c.getString("content")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.e("DATE", c.toString());
                        }
                    }
                }

                Log.d("Messages", messages.toString());
                return messages;
            }
        } catch (JSONException e) {
            Log.e("SERVER_RESPONSE", json.toString());
            e.printStackTrace();
            throw new ServerException("Server has not returned the expected JSON!");
        }
    }

    public void newMessage(int id, String s) {
        ControllerMsgData.getInstance().newMessage(id, s);
    }
}
