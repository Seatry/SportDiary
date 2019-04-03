package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Aim;

import java.util.List;

@Dao
public interface AimDao extends EditDao<Aim>{
    @Query("SELECT * FROM Aim")
    LiveData<List<Aim>> getAll();


    @Query("SELECT name FROM Aim")
    List<String> getAllNames();

    @Query("SELECT id FROM Aim where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Aim where id = :id")
    String getNameById(long id);
}
