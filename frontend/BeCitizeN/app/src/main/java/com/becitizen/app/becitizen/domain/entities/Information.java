package com.becitizen.app.becitizen.domain.entities;

public class Information {

    private int id;
    private String title;
    private String content;
    private String url;
    private String type;

    public Information(int id, String title, String content, String url, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.url = url;
        this.type = type;
    }

    public Information(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
