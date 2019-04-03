package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Style;

import java.util.List;

@Dao
public interface StyleDao extends EditDao<Style> {
    @Query("SELECT * FROM Style")
    LiveData<List<Style>> getAll();

    @Query("SELECT name FROM Style where id = :id")
    String getNameById(Long id);

    @Query("SELECT name FROM Style")
    List<String> getAllNames();

    @Query("SELECT id FROM Style where name = :name")
    long getIdByName(String name);
}
