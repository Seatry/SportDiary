package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.alexander.sportdiary.Converters.DateConverter;

import java.util.Date;

@Entity(indices = {@Index(value = {"name", "start"}, unique = true)})
@TypeConverters(DateConverter.class)
public class SeasonPlan {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private Date start;

    public SeasonPlan(String name, Date start) {
        this.name = name;
        this.start = start;
    }

    public SeasonPlan() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }
}