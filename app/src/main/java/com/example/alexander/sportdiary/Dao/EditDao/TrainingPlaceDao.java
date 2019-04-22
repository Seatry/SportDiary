package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.example.alexander.sportdiary.Entities.EditEntities.TrainingPlace;

import java.util.List;

@Dao
public interface TrainingPlaceDao extends EditDao<TrainingPlace> {
    @Query("SELECT * FROM TrainingPlace WHERE userId = :userId")
    LiveData<List<TrainingPlace>> getAllByUserId(String userId);


    @Query("SELECT name FROM TrainingPlace WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);


    @Query("SELECT id FROM TrainingPlace where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM TrainingPlace where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("DELETE FROM TrainingPlace WHERE userId = :userId")
    void deleteByUserId(String userId);
}
