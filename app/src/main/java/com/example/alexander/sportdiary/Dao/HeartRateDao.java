package com.example.alexander.sportdiary.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.HeartRate;

import java.util.List;

@Dao
public interface HeartRateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(HeartRate heartRate);

    @Update
    void update(HeartRate heartRate);

    @Delete
    void delete(HeartRate heartRate);

    @Query("DELETE FROM HeartRate WHERE exercise_id = :id and (series > :series or repeat > :repeats)")
    void deleteByExerciseIdWithGreaterSeriesOrRepeat(long id, int series, int repeats);

    @Query("SELECT hr FROM HeartRate WHERE exercise_id = :id")
    List<Integer> getHrByExerciseId(long id);

    @Query("SELECT * FROM HeartRate WHERE exercise_id = :exerciseId order by series, repeat")
    List<HeartRate> getAllByExerciseId(long exerciseId);
}
