package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Converters.DateConverter;
import com.example.alexander.sportdiary.Entities.CompetitionToImportance;
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

    @Query("DELETE FROM Day WHERE season_plan_id = :id")
    void deleteBySeasonPlanId(long id);

    @Query("SELECT * FROM Day")
    List<Day> getAll();

    @Query("SELECT id FROM Day WHERE  date = :date and season_plan_id = :id")
    long getDayIdByDateAndSeasonPlanId(Date date, long id);

    @Query("SELECT * FROM Day WHERE  date = :date and season_plan_id = :id")
    Day getDayByDateAndSeasonPlanId(Date date, long id);

    @Query("SELECT * FROM Day WHERE  date = :date and season_plan_id = :id")
    LiveData<Day> getLiveDayByDateAndSeasonPlanId(Date date, long id);

    @Query("SELECT * FROM Day WHERE competition_to_importance_id is not null and season_plan_id = :id")
    LiveData<List<Day>> getAllDaysBySeasonPlanIdWhereCompetitionToImportanceIsNotNull(long id);

    @Query("UPDATE Day set competition_to_importance_id = :id WHERE date = :date and season_plan_id = :seasonId")
    void updateCompetitionToImportanceByDateAndSeasonId(Long id, Date date, long seasonId);

    @Query("SELECT competition_to_importance_id FROM Day WHERE  date = :date and season_plan_id = :id")
    long getCompetitionToImportanceIdByDateAndSeasonId(Date date, long id);

    @Query("SELECT * FROM Day WHERE date = :date and season_plan_id = :seasonPlanId and competition_to_importance_id is not null")
    Day getDayByDateAndSeasonIdWhereCompetitionToImportanceNotNull(Date date, long seasonPlanId);

    @Query("UPDATE Day set capacity = :capacity WHERE id = :dayId")
    void updateCapacityById(int capacity, long dayId);
}
