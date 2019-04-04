package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.alexander.sportdiary.Entities.EditEntities.Type;

import java.util.List;

@Dao
public interface TypeDao extends EditDao<Type> {
    @Query("SELECT * FROM Type")
    LiveData<List<Type>> getAll();

    @Query("SELECT name FROM Type where id = :id")
    String getNameById(Long id);

    @Query("SELECT name FROM Type")
    List<String> getAllNames();

    @Query("SELECT id FROM Type where name = :name")
    long getIdByName(String name);
}
