package com.becitizen.app.becitizen.domain.entities;

import java.util.Date;
import java.util.List;

public class Conversation {

    private int id;
    private int userImage;
    private String userName;
    private Date lastMessage;
    private List<Message> messages;

    public Conversation(int id, int userImage, String userName, Date lastMessage) {
        this.id = id;
        this.userImage = userImage;
        this.userName = userName;
        this.lastMessage = lastMessage;
    }

    public Conversation(int id, int userImage, String userName, Date lastMessage, List<Message> messages) {
        this.id = id;
        this.userImage = userImage;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", userImage=" + userImage +
                ", userName='" + userName + '\'' +
                ", lastMessage=" + lastMessage +
                ", messages=" + messages +
                '}';
    }
}
