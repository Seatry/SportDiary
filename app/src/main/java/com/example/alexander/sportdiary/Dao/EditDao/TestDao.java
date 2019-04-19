package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Test;

import java.util.List;

@Dao
public interface TestDao extends EditDao<Test> {
    @Query("SELECT * FROM Test WHERE userId = :userId")
    LiveData<List<Test>> getAllByUserId(String userId);

    @Query("SELECT name FROM Test WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Test where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Test where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);
}
