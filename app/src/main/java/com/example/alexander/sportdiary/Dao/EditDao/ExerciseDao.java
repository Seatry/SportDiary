package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao extends EditDao<Exercise> {
    @Query("SELECT * FROM Exercise")
    LiveData<List<Exercise>> getAll();

    @Query("SELECT name FROM Exercise where id = :id")
    String getNameById(long id);

    @Query("SELECT name FROM Exercise")
    List<String> getAllNames();

    @Query("SELECT id FROM Exercise where name = :name")
    long getIdByName(String name);
}
