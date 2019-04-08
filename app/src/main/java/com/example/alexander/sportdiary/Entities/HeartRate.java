package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = TrainingExercise.class, parentColumns = "id",
            childColumns = "exercise_id", onDelete = CASCADE, onUpdate = CASCADE),
        indices = {@Index(value = {"exercise_id", "time", "series", "repeat"}, unique = true)})
public class HeartRate {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "exercise_id")
    private long exerciseId;

    private String time;
    private int series;
    private int repeat;
    private int hr = 0;

    public HeartRate() {

    }

    public HeartRate(long exerciseId, String time, int series, int repeat) {
        this.exerciseId = exerciseId;
        this.time = time;
        this.series = series;
        this.repeat = repeat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }
}
