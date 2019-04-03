package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Converters.DateConverter;
import com.example.alexander.sportdiary.Entities.Day;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters(DateConverter.class)
public interface DayDao {
    @Insert
    long insert(Day day);

    @Update
    void update(Day day);

    @Delete
    void delete(Day day);

    @Query("SELECT * FROM Day")
    List<Day> getAll();

    @Query("SELECT id FROM Day WHERE  date = :date and season_plan_id = :id")
    long getDayIdByDateAndSeasonPlanId(Date date, long id);
}
