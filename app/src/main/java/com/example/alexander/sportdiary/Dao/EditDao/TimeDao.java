package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Time;

import java.util.List;

@Dao
public interface TimeDao extends EditDao<Time>{
    @Query("SELECT * FROM Time")
    LiveData<List<Time>> getAll();

    @Query("SELECT name FROM Time")
    List<String> getAllNames();

    @Query("SELECT id FROM Time where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Time where id = :id")
    String getNameById(Long id);
}
