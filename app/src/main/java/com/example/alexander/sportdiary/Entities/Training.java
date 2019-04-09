package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import com.example.alexander.sportdiary.Entities.EditEntities.Borg;
import com.example.alexander.sportdiary.Entities.EditEntities.Time;
import com.example.alexander.sportdiary.Entities.EditEntities.TrainingPlace;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.SET_NULL;

@Entity(foreignKeys =
        {@ForeignKey(
                entity = Day.class, parentColumns = "id", childColumns = "day_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(
                entity = Time.class, parentColumns = "id", childColumns = "time_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = TrainingPlace.class, parentColumns = "id", childColumns = "place_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Borg.class, parentColumns = "id", childColumns = "borg_id", onDelete = SET_NULL, onUpdate = CASCADE
        )})
public class Training {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "day_id")
    private long dayId;

    @Nullable
    @ColumnInfo(name = "time_id")
    private Long timeId;

    @Nullable
    @ColumnInfo(name = "place_id")
    private Long placeId;

    @Nullable
    @ColumnInfo(name = "borg_id")
    private Long borgId;

    private int capacity = 0;

    public Training() {

    }

    public Training(long dayId) {
        this.dayId = dayId;
    }

    public Training(long dayId, @Nullable Long timeId, @Nullable Long placeId, @Nullable Long borgId) {
        this.dayId = dayId;
        this.timeId = timeId;
        this.placeId = placeId;
        this.borgId = borgId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    @Nullable
    public Long getTimeId() {
        return timeId;
    }

    public void setTimeId(@Nullable Long timeId) {
        this.timeId = timeId;
    }

    @Nullable
    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(@Nullable Long placeId) {
        this.placeId = placeId;
    }

    @Nullable
    public Long getBorgId() {
        return borgId;
    }

    public void setBorgId(@Nullable Long borgId) {
        this.borgId = borgId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
