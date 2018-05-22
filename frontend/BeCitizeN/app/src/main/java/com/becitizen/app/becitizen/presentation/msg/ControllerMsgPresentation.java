package com.becitizen.app.becitizen.presentation.msg;

import com.becitizen.app.becitizen.domain.ControllerMsgDomain;
import com.becitizen.app.becitizen.domain.entities.Conversation;
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

    public Conversation getConversation() {
        return ControllerMsgDomain.getInstance().getConversation();
    }

    public List<Conversation> getConversations() throws ServerException {
        return ControllerMsgDomain.getInstance().getConversations();
    }
}
