package com.becitizen.app.becitizen.domain.entities;

import java.util.Date;
import java.util.List;

public class Conversation {

    private int id;
    private String name;
    private String userName;
    private int userImage;
    private boolean newMessage;
    private String lastMessage;
    private Date lastMessageDate;
    private List<Message> messages;

    public Conversation(int id, String name, String userName, int userImage) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.userImage = userImage;
    }

    public Conversation(int id, String name, String userName, int userImage, boolean newMessage, String lastMessage, Date lastMessageDate) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.userImage = userImage;
        this.newMessage = newMessage;
        this.lastMessage = lastMessage;
        this.lastMessageDate = lastMessageDate;
    }

    public Conversation(int id, String name, String userName, int userImage, boolean newMessage, String lastMessage, Date lastMessageDate, List<Message> messages) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.userImage = userImage;
        this.newMessage = newMessage;
        this.lastMessage = lastMessage;
        this.lastMessageDate = lastMessageDate;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public boolean isNewMessage() {
        return newMessage;
    }

    public void setNewMessage(boolean newMessage) {
        this.newMessage = newMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
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
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", userImage=" + userImage +
                ", newMessage=" + newMessage +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageDate=" + lastMessageDate +
                ", messages=" + messages +
                '}';
    }
}
