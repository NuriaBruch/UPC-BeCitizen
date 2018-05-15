package com.becitizen.app.becitizen.domain.entities;

import java.util.Date;

public class Message {
    private boolean mine;
    private Date date;
    private String content;

    public Message(boolean mine, Date date, String content) {
        this.mine = mine;
        this.date = date;
        this.content = content;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mine=" + mine +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }
}
