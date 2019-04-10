package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.DayToTest;

import java.util.List;

@Dao
public interface DayToTestDao {
    @Insert
    long insert(DayToTest dayToTest);

    @Update
    void update(DayToTest dayToTest);

    @Delete
    void delete(DayToTest dayToTest);

    @Query("SELECT test_id FROM DayToTest where day_id = :id")
    List<Long> getTestIdsByDayId(long id);

    @Query("SELECT * FROM DayToTest WHERE day_id = :id")
    LiveData<List<DayToTest>> getAllLiveByDayId(long id);
}
