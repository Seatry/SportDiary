package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Camp;

import java.util.List;

@Dao
public interface CampDao extends EditDao<Camp> {
    @Query("SELECT * FROM Camp")
    LiveData<List<Camp>> getAll();

    @Query("SELECT name FROM Camp where id = :id")
    String getNameById(Long id);

    @Query("SELECT name FROM Camp")
    List<String> getAllNames();

    @Query("SELECT id FROM Camp where name = :name")
    long getIdByName(String name);
}
