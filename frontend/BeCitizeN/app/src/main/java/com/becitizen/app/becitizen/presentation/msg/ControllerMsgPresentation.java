package com.becitizen.app.becitizen.presentation.msg;

import com.becitizen.app.becitizen.domain.ControllerMsgDomain;
import com.becitizen.app.becitizen.domain.entities.Conversation;

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
}
