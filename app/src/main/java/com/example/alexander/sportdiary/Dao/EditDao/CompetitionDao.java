package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Competition;

import java.util.List;

@Dao
public interface CompetitionDao extends EditDao<Competition>{
    @Query("SELECT * FROM Competition WHERE userId = :userId")
    LiveData<List<Competition>> getAllByUserId(String userId);

    @Query("SELECT name FROM Competition where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Competition WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Competition where name = :name")
    long getIdByName(String name);
}
