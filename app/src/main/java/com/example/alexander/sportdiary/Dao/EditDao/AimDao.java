package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Aim;

import java.util.List;

@Dao
public interface AimDao extends EditDao<Aim>{
    @Query("SELECT * FROM Aim WHERE userId = :userId")
    LiveData<List<Aim>> getAllByUserId(String userId);

    @Query("SELECT name FROM Aim WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Aim where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Aim where id = :id and userId = :userId")
    String getNameByIdAndUserId(long id, String userId);

    @Query("DELETE FROM Aim WHERE userId = :userId")
    void deleteByUserId(String userId);
}
