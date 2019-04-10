package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = "question", unique = true)})
public class DreamQuestion {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String question;

    public DreamQuestion(String question) {
        this.question = question;
    }

    public DreamQuestion() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
