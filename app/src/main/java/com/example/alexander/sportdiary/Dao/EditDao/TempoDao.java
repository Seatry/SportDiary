package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Tempo;

import java.util.List;

@Dao
public interface TempoDao extends EditDao<Tempo> {
    @Query("SELECT * FROM Tempo WHERE userId = :userId")
    LiveData<List<Tempo>> getAllByUserId(String userId);

    @Query("SELECT name FROM Tempo where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Tempo WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Tempo where name = :name")
    long getIdByName(String name);
}
