package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.example.alexander.sportdiary.Entities.EditEntities.Borg;

import java.util.List;

@Dao
public interface BorgDao extends EditDao<Borg>{
    @Query("SELECT * FROM Borg")
    LiveData<List<Borg>> getAll();


    @Query("SELECT name FROM Borg")
    List<String> getAllNames();

    @Query("SELECT id FROM Borg where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Borg where id = :id")
    String getNameById(long id);
}