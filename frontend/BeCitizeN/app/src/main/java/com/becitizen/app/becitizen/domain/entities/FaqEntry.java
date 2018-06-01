package com.becitizen.app.becitizen.domain.entities;

public class FaqEntry {

    private int id;
    private String question;
    private String answer;
    private Float rating;

    public FaqEntry(int id, String question, String answer, Float rating) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
