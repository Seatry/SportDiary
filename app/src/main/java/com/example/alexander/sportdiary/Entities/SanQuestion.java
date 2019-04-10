package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.alexander.sportdiary.Converters.SanTypeConverter;
import com.example.alexander.sportdiary.Enums.SanType;

@Entity(indices = {@Index(value = {"positive", "negative"}, unique = true)})
@TypeConverters(SanTypeConverter.class)
public class SanQuestion {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String positive;
    private String negative;
    private SanType type;

    public SanQuestion() {

    }

    public SanQuestion(String positive, String negative, SanType type) {
        this.positive = positive;
        this.negative = negative;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public SanType getType() {
        return type;
    }

    public void setType(SanType type) {
        this.type = type;
    }
}
