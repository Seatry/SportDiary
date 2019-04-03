package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.TrainingsToAims;

import java.util.List;


@Dao
public interface TrainingsToAimsDao {
    @Insert
    long insert(TrainingsToAims trainingsToAims);

    @Update
    void update(TrainingsToAims trainingsToAims);

    @Delete
    void delete(TrainingsToAims trainingsToAims);

    @Query("SELECT aim_id FROM TrainingsToAims where training_id = :id")
    List<Long> getAimIdsByTrainingId(long id);

    @Query("SELECT * FROM TrainingsToAims")
    LiveData<List<TrainingsToAims>> getAll();

    @Query("DELETE FROM TrainingsToAims where training_id = :id")
    void deleteByTrainingId(long id);
}
