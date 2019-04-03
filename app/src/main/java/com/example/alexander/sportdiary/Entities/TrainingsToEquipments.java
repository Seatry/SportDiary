package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.alexander.sportdiary.Entities.EditEntities.Equipment;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(foreignKeys =
        {@ForeignKey(
                entity = Training.class, parentColumns = "id", childColumns = "training_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(
                entity = Equipment.class, parentColumns = "id", childColumns = "equipment_id", onDelete = CASCADE, onUpdate = CASCADE
        )})
public class TrainingsToEquipments {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "training_id")
    private long trainingId;

    @ColumnInfo(name = "equipment_id")
    private long equipmentId;

    public TrainingsToEquipments(long trainingId, long equipmentId) {
        this.trainingId = trainingId;
        this.equipmentId = equipmentId;
    }

    public long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
