package com.example.alexander.sportdiary.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.SeasonPlan;

import java.util.List;

@Dao
public interface SeasonPlanDao {
    @Insert
    long insert(SeasonPlan seasonPlan);

    @Update
    void update(SeasonPlan seasonPlan);

    @Delete
    void delete(SeasonPlan seasonPlan);

    @Query("SELECT * FROM SeasonPlan WHERE userId = :userId")
    List<SeasonPlan> getAllByUserId(String userId);

    @Query("SELECT * FROM SeasonPlan WHERE id = :id")
    SeasonPlan getSeasonPlanById(long id);
}
