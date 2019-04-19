package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Block;

import java.util.List;

@Dao
public interface BlockDao extends EditDao<Block> {
    @Query("SELECT * FROM Block WHERE userId = :userId")
    LiveData<List<Block>> getAllByUserId(String userId);

    @Query("SELECT name FROM Block where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Block WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Block where name = :name")
    long getIdByName(String name);
}
