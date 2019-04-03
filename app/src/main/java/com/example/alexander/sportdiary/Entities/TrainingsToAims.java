package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.alexander.sportdiary.Entities.EditEntities.Aim;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys =
        {@ForeignKey(
                entity = Training.class, parentColumns = "id", childColumns = "training_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(
                entity = Aim.class, parentColumns = "id", childColumns = "aim_id", onDelete = CASCADE, onUpdate = CASCADE
        )})
public class TrainingsToAims {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "training_id")
    private long trainingId;

    @ColumnInfo(name = "aim_id")
    private long aimId;

    public TrainingsToAims(long trainingId, long aimId) {
        this.trainingId = trainingId;
        this.aimId = aimId;
    }

    public TrainingsToAims() {

    }

    public long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
    }

    public long getAimId() {
        return aimId;
    }

    public void setAimId(long aimId) {
        this.aimId = aimId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
