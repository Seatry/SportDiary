package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Time;

import java.util.List;

@Dao
public interface TimeDao extends EditDao<Time>{
    @Query("SELECT * FROM Time WHERE userId = :userId")
    LiveData<List<Time>> getAllByUserId(String userId);

    @Query("SELECT name FROM Time WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Time where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Time where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);
}
