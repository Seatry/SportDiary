package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.Rest;

import java.util.List;

@Dao
public interface RestDao {
    @Insert
    long insert(Rest rest);

    @Update
    void update(Rest rest);

    @Delete
    void delete(Rest rest);

    @Query("SELECT * FROM Rest")
    LiveData<List<Rest>> getAll();

    @Query("SELECT * FROM Rest WHERE day_id = :id")
    LiveData<List<Rest>> getAllLiveByDayId(long id);

    @Query("SELECT * FROM Rest WHERE day_id = :id")
    List<Rest> getAllByDayId(long id);
}
