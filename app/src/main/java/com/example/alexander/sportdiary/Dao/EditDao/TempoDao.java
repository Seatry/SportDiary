package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Tempo;

import java.util.List;

@Dao
public interface TempoDao extends EditDao<Tempo> {
    @Query("SELECT * FROM Tempo")
    LiveData<List<Tempo>> getAll();

    @Query("SELECT name FROM Tempo where id = :id")
    String getNameById(long id);

    @Query("SELECT name FROM Tempo")
    List<String> getAllNames();

    @Query("SELECT id FROM Tempo where name = :name")
    long getIdByName(String name);
}
