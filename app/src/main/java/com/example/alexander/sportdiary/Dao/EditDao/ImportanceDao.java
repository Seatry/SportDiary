package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Importance;

import java.util.List;

@Dao
public interface ImportanceDao extends EditDao<Importance> {
    @Query("SELECT * FROM Importance WHERE userId = :userId")
    LiveData<List<Importance>> getAllByUserId(String userId);

    @Query("SELECT name FROM Importance where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Importance WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Importance where name = :name")
    long getIdByName(String name);

    @Query("DELETE FROM Importance WHERE userId = :userId")
    void deleteByUserId(String userId);
}
