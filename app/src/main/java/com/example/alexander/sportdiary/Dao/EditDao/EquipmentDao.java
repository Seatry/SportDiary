package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.example.alexander.sportdiary.Entities.EditEntities.Equipment;

import java.util.List;

@Dao
public interface EquipmentDao extends EditDao<Equipment>{
    @Query("SELECT * FROM Equipment")
    LiveData<List<Equipment>> getAll();


    @Query("SELECT name FROM Equipment")
    List<String> getAllNames();


    @Query("SELECT id FROM Equipment where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Equipment where id = :id")
    String getNameById(long id);
}
