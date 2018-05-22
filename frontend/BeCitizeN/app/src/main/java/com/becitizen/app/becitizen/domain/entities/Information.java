package com.becitizen.app.becitizen.domain.entities;

import java.util.ArrayList;

public class Information {

    private int id;
    private String title;
    private String content;
    private String url;
    private String type;
    private ArrayList<Marker> markers;

    public Information(int id, String title, String content, String url, String type, ArrayList<Marker> markers) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.url = url;
        this.type = type;
        this.markers = markers;
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

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<Marker> markers) {
        this.markers = markers;
    }
}
