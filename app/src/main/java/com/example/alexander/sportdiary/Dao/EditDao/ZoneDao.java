package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Zone;

import java.util.List;

@Dao
public interface ZoneDao extends EditDao<Zone> {

    @Query("SELECT * FROM Zone WHERE userId = :userId")
    LiveData<List<Zone>> getAllByUserId(String userId);

    @Query("SELECT name FROM Zone where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Zone WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Zone where name = :name")
    long getIdByName(String name);
}
