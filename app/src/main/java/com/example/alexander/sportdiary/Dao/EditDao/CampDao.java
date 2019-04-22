package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Camp;

import java.util.List;

@Dao
public interface CampDao extends EditDao<Camp> {
    @Query("SELECT * FROM Camp WHERE userId = :userId")
    LiveData<List<Camp>> getAllByUserId(String userId);

    @Query("SELECT name FROM Camp where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Camp WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Camp where name = :name")
    long getIdByName(String name);

    @Query("DELETE FROM Camp WHERE userId = :userId")
    void deleteByUserId(String userId);
}
