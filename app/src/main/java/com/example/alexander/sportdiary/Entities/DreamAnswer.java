package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys =
        {@ForeignKey(
                entity = Day.class, parentColumns = "id", childColumns = "day_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(
                entity = DreamQuestion.class, parentColumns = "id", childColumns = "question_id", onDelete = CASCADE, onUpdate = CASCADE
        )},
        indices = {@Index(value = {"day_id", "question_id"}, unique = true)})
public class DreamAnswer {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "day_id")
    private long dayId;

    @ColumnInfo(name = "question_id")
    private long questionId;

    private int answer;

    public DreamAnswer() {

    }

    public DreamAnswer(long dayId, long questionId, int answer) {
        this.dayId = dayId;
        this.questionId = questionId;
        this.answer = answer;
    }

    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
