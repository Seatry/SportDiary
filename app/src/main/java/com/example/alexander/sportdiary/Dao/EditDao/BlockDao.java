package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Block;

import java.util.List;

@Dao
public interface BlockDao extends EditDao<Block> {
    @Query("SELECT * FROM Block")
    LiveData<List<Block>> getAll();

    @Query("SELECT name FROM Block where id = :id")
    String getNameById(Long id);

    @Query("SELECT name FROM Block")
    List<String> getAllNames();

    @Query("SELECT id FROM Block where name = :name")
    long getIdByName(String name);
}
