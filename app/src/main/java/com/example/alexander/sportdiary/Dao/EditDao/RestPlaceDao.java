package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.RestPlace;

import java.util.List;

@Dao
public interface RestPlaceDao extends EditDao<RestPlace> {
    @Query("SELECT * FROM RestPlace WHERE userId = :userId")
    LiveData<List<RestPlace>> getAllByUserId(String userId);

    @Query("SELECT name FROM RestPlace WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM RestPlace where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM RestPlace where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);
}
