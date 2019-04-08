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
    private String male;
    private int hrMax = 200;
    private int hrRest = 60;
    private int lastPerformance = 0;

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

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public int getHrMax() {
        return hrMax;
    }

    public void setHrMax(int hrMax) {
        this.hrMax = hrMax;
    }

    public int getHrRest() {
        return hrRest;
    }

    public void setHrRest(int hrRest) {
        this.hrRest = hrRest;
    }

    public int getLastPerformance() {
        return lastPerformance;
    }

    public void setLastPerformance(int lastPerformance) {
        this.lastPerformance = lastPerformance;
    }
}
