package com.becitizen.app.becitizen.domain.entities;

public class Thread {

    private String title;
    private String content;
    private String category;
    private String author;
    private int authorImage;
    private String authorRank;
    private String createdAt;
    private int votes;
    private boolean canReport;
    private boolean canVote;

    public Thread(String title, String content, String category, String author, int authorImage, String authorRank, String createdAt, int votes, boolean canReport, boolean canVote) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.authorImage = authorImage;
        this.authorRank = authorRank;
        this.createdAt = createdAt;
        this.votes = votes;
        this.canReport = canReport;
        this.canVote = canVote;
    }

    public Thread(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(int authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorRank() {
        return authorRank;
    }

    public void setAuthorRank(String authorRank) {
        this.authorRank = authorRank;
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

    public boolean isCanReport() {
        return canReport;
    }

    public void setCanReport(boolean canReport) {
        this.canReport = canReport;
    }

    public boolean isCanVote() {
        return canVote;
    }

    public void setCanVote(boolean canVote) {
        this.canVote = canVote;
    }
}
