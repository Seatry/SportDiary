package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao extends EditDao<Exercise> {
    @Query("SELECT * FROM Exercise WHERE userId = :userId")
    LiveData<List<Exercise>> getAllByUserId(String userId);

    @Query("SELECT name FROM Exercise where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Exercise WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Exercise where name = :name")
    long getIdByName(String name);
}
