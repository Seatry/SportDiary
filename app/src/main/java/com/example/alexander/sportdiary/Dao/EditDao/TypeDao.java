package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Type;

import java.util.List;

@Dao
public interface TypeDao extends EditDao<Type> {
    @Query("SELECT * FROM Type WHERE userId = :userId")
    LiveData<List<Type>> getAllByUserId(String userId);

    @Query("SELECT name FROM Type where id = :id and userId = :userId")
    String getNameByIdAndUserId(Long id, String userId);

    @Query("SELECT name FROM Type WHERE userId = :userId")
    List<String> getAllNamesByUserId(String userId);

    @Query("SELECT id FROM Type where name = :name")
    long getIdByName(String name);
}
