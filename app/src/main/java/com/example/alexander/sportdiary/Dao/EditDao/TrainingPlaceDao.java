package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.example.alexander.sportdiary.Entities.EditEntities.TrainingPlace;

import java.util.List;

@Dao
public interface TrainingPlaceDao extends EditDao<TrainingPlace> {
    @Query("SELECT * FROM TrainingPlace")
    LiveData<List<TrainingPlace>> getAll();


    @Query("SELECT name FROM TrainingPlace")
    List<String> getAllNames();


    @Query("SELECT id FROM TrainingPlace where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM TrainingPlace where id = :id")
    String getNameById(long id);
}
