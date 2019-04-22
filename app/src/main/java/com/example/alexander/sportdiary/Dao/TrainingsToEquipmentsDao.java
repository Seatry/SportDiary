package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.TrainingsToEquipments;

import java.util.List;


@Dao
public interface TrainingsToEquipmentsDao {
    @Insert
    long insert(TrainingsToEquipments trainingsToEquipments);

    @Update
    void update(TrainingsToEquipments trainingsToEquipments);

    @Delete
    void delete(TrainingsToEquipments trainingsToEquipments);

    @Query("SELECT equipment_id FROM TrainingsToEquipments where training_id = :id")
    List<Long> getEquipmentIdsByTrainingId(long id);

    @Query("SELECT * FROM TrainingsToEquipments")
    LiveData<List<TrainingsToEquipments>> getAll();

    @Query("DELETE FROM TrainingsToEquipments where training_id = :id")
    void deleteByTrainingId(long id);

    @Query("SELECT * FROM TrainingsToEquipments where training_id = :id")
    List<TrainingsToEquipments> getByTrainingId(long id);
}
