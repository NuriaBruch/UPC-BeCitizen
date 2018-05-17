package com.becitizen.app.becitizen.domain;

import com.becitizen.app.becitizen.domain.entities.Conversation;
import com.becitizen.app.becitizen.domain.entities.Message;

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
}
