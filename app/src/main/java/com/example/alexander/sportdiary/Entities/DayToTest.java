package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.alexander.sportdiary.Entities.EditEntities.Test;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys =
        {@ForeignKey(
                entity = Day.class, parentColumns = "id", childColumns = "day_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(
                entity = Test.class, parentColumns = "id", childColumns = "test_id", onDelete = CASCADE, onUpdate = CASCADE
        )})
public class DayToTest {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "day_id")
    private long dayId;

    @ColumnInfo(name = "test_id")
    private long testId;

    public DayToTest() {

    }

    public DayToTest(long dayId, long testId) {
        this.dayId = dayId;
        this.testId = testId;
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

    public Long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }
}
