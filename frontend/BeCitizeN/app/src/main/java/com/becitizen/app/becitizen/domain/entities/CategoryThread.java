package com.becitizen.app.becitizen.domain.entities;

import java.time.LocalTime;

public class CategoryThread {
    private String title;
    private String author;
    private String createdAt;
    private int votes;

    public CategoryThread(String title, String author, String createdAt, int votes) {
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.votes = votes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
