package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Stage;

import java.util.List;

@Dao
public interface StageDao extends EditDao<Stage> {
    @Query("SELECT * FROM Stage WHERE userId = :userId")
    LiveData<List<Stage>> getAllByUserId(String userId);

    @Query("SELECT name FROM Stage where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Stage WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Stage where name = :name")
    long getIdByName(String name);

    @Query("DELETE FROM Stage WHERE userId = :userId")
    void deleteByUserId(String userId);
}
