package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Stage;

import java.util.List;

@Dao
public interface StageDao extends EditDao<Stage> {
    @Query("SELECT * FROM Stage")
    LiveData<List<Stage>> getAll();

    @Query("SELECT name FROM Stage where id = :id")
    String getNameById(Long id);

    @Query("SELECT name FROM Stage")
    List<String> getAllNames();

    @Query("SELECT id FROM Stage where name = :name")
    long getIdByName(String name);
}
