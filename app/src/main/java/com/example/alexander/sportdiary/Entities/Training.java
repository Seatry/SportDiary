package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

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

    @ColumnInfo(name = "time_id")
    private long timeId;

    @ColumnInfo(name = "place_id")
    private long placeId;

    @ColumnInfo(name = "borg_id")
    private long borgId;

    private int capacity = 0;

    public Training() {

    }

    public Training(long dayId, long timeId, long placeId, long borgId) {
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

    public long getTimeId() {
        return timeId;
    }

    public void setTimeId(long timeId) {
        this.timeId = timeId;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public long getBorgId() {
        return borgId;
    }

    public void setBorgId(long borgId) {
        this.borgId = borgId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
