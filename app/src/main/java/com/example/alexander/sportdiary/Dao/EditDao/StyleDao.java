package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Style;

import java.util.List;

@Dao
public interface StyleDao extends EditDao<Style> {
    @Query("SELECT * FROM Style WHERE userId = :userId")
    LiveData<List<Style>> getAllByUserId(String userId);

    @Query("SELECT name FROM Style where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Style WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Style where name = :name")
    long getIdByName(String name);
}
