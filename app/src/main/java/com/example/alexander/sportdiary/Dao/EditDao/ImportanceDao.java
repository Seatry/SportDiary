package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Importance;

import java.util.List;

@Dao
public interface ImportanceDao extends EditDao<Importance> {
    @Query("SELECT * FROM Importance")
    LiveData<List<Importance>> getAll();

    @Query("SELECT name FROM Importance where id = :id")
    String getNameById(Long id);

    @Query("SELECT name FROM Importance")
    List<String> getAllNames();

    @Query("SELECT id FROM Importance where name = :name")
    long getIdByName(String name);
}
