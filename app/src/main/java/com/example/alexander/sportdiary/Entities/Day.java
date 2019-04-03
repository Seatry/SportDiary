package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.alexander.sportdiary.Converters.DateConverter;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys =
    @ForeignKey(entity = SeasonPlan.class, parentColumns = "id", childColumns = "season_plan_id", onDelete = CASCADE, onUpdate = CASCADE),
        indices = {@Index(value = {"date", "season_plan_id"}, unique = true)})
@TypeConverters(DateConverter.class)
public class Day {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date date;

    public Day() {

    }

    public Day(Date date, long seasonPlanId) {
        this.date = date;
        this.seasonPlanId = seasonPlanId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ColumnInfo(name = "season_plan_id")
    private long seasonPlanId;

    public long getSeasonPlanId() {
        return seasonPlanId;
    }

    public void setSeasonPlanId(long seasonPlanId) {
        seasonPlanId = seasonPlanId;
    }
}
