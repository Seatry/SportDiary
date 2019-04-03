package com.example.alexander.sportdiary.Dao.EditDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.EditEntities.Edit;

import java.util.List;

public interface EditDao<T extends Edit> {
    @Insert
    void insert(T t);

    @Update
    void update(T t);

    @Delete
    void delete(T t);

    /* Here Table Time just an example of Edit Table */

    @Query("SELECT * FROM Time")
    LiveData<List<T>> getAll();

    @Query("SELECT name FROM Time")
    List<String> getAllNames();

    @Query("SELECT id FROM Time where name = :name")
    long getIdByName(String name);

    @Query("SELECT name FROM Time where id = :id")
    String getNameById(long id);
}
