package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Competition;

import java.util.List;

@Dao
public interface CompetitionDao extends EditDao<Competition>{
    @Query("SELECT * FROM Competition")
    LiveData<List<Competition>> getAll();

    @Query("SELECT name FROM Competition where id = :id")
    String getNameById(Long id);

    @Query("SELECT name FROM Competition")
    List<String> getAllNames();

    @Query("SELECT id FROM Competition where name = :name")
    long getIdByName(String name);
}
