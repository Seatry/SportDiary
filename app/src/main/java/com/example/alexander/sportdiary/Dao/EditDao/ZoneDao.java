package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Zone;

import java.util.List;

@Dao
public interface ZoneDao extends EditDao<Zone> {

    @Query("SELECT * FROM Zone")
    LiveData<List<Zone>> getAll();

    @Query("SELECT name FROM Zone where id = :id")
    String getNameById(Long id);

    @Query("SELECT name FROM Zone")
    List<String> getAllNames();

    @Query("SELECT id FROM Zone where name = :name")
    long getIdByName(String name);
}
