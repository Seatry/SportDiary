package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.RestPlace;

import java.util.List;

@Dao
public interface RestPlaceDao extends EditDao<RestPlace> {
    @Query("SELECT * FROM RestPlace")
    LiveData<List<RestPlace>> getAll();

    @Query("SELECT name FROM RestPlace")
    List<String> getAllNames();

    @Query("SELECT id FROM RestPlace where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM RestPlace where id = :id")
    String getNameById(Long id);
}
