package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Test;

import java.util.List;

@Dao
public interface TestDao extends EditDao<Test> {
    @Query("SELECT * FROM Test")
    LiveData<List<Test>> getAll();

    @Query("SELECT name FROM Test")
    List<String> getAllNames();

    @Query("SELECT id FROM Test where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Test where id = :id")
    String getNameById(Long id);
}
