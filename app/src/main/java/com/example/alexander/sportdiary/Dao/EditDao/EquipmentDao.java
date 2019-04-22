package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.example.alexander.sportdiary.Entities.EditEntities.Equipment;

import java.util.List;

@Dao
public interface EquipmentDao extends EditDao<Equipment>{
    @Query("SELECT * FROM Equipment WHERE userId = :userId")
    LiveData<List<Equipment>> getAllByUserId(String userId);


    @Query("SELECT name FROM Equipment WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);


    @Query("SELECT id FROM Equipment where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Equipment where id = :id and userId = :userId")
    String getNameByIdAndUserId(long id, String userId);

    @Query("DELETE FROM Equipment WHERE userId = :userId")
    void deleteByUserId(String userId);
}
