package com.becitizen.app.becitizen.presentation.controllers;

import android.accounts.NetworkErrorException;

import com.becitizen.app.becitizen.domain.controllers.ControllerMsgDomain;
import com.becitizen.app.becitizen.domain.entities.Conversation;
import com.becitizen.app.becitizen.domain.entities.Message;
import com.becitizen.app.becitizen.exceptions.ServerException;

import java.util.List;

public class ControllerMsgPresentation {

    private static ControllerMsgPresentation instance;

    protected ControllerMsgPresentation() {
    }

    public static ControllerMsgPresentation getInstance() {
        if (instance == null) instance = new ControllerMsgPresentation();
        return instance;
    }

    public List<Message> getMessages(int conversationId) throws ServerException {
        return ControllerMsgDomain.getInstance().getMessages(conversationId);
    }

    public Conversation getConversation(String email) throws ServerException {
        return ControllerMsgDomain.getInstance().getConversation(email);
    }

    public List<Conversation> getConversations() throws ServerException, NetworkErrorException {
        return ControllerMsgDomain.getInstance().getConversations();
    }

    public void newMessage(int id, String s) {
        ControllerMsgDomain.getInstance().newMessage(id, s);
    }
}
