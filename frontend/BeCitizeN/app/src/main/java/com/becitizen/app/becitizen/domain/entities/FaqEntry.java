package com.becitizen.app.becitizen.domain.entities;
import android.arch.persistence.room.*;

@Entity
public class FaqEntry {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "answer")
    private String answer;
    @ColumnInfo(name = "rating")
    private Float rating;
    @ColumnInfo(name="category")
    private String category;



    public FaqEntry(int id, String question, String answer, Float rating, String category) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.rating = rating;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
