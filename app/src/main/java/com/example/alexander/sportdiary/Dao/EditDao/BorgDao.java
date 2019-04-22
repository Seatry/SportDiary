package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.example.alexander.sportdiary.Entities.EditEntities.Borg;

import java.util.List;

@Dao
public interface BorgDao extends EditDao<Borg>{
    @Query("SELECT * FROM Borg WHERE userId = :userId")
    LiveData<List<Borg>> getAllByUserId(String userId);


    @Query("SELECT name FROM Borg WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Borg where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Borg where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("DELETE FROM Borg WHERE userId = :userId")
    void deleteByUserId(String userId);
}