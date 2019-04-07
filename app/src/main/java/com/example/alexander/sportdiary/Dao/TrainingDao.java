package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.alexander.sportdiary.Entities.Training;

import java.util.List;

@Dao
public interface TrainingDao {
    @Insert
    long insert(Training training);

    @Update
    void update(Training training);

    @Delete
    void delete(Training training);

    @Query("SELECT * FROM Training")
    LiveData<List<Training>> getAll();

    @Query("SELECT * FROM Training WHERE day_id = :id")
    LiveData<List<Training>> getAllLiveByDayId(long id);

    @Query("SELECT * FROM Training WHERE day_id = :id")
    List<Training> getAllByDayId(long id);

    @Query("SELECT * FROM Training WHERE id = :id")
    Training getTrainingById(long id);

    @Query("UPDATE Training SET capacity = :capacity WHERE id = :id")
    void updateCapacityById(int capacity, long id);
}
