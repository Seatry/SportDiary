package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.CompetitionToImportance;

import java.util.List;

@Dao
public interface CompetitionToImportanceDao {
    @Insert
    long insert(CompetitionToImportance competitionToImportance);

    @Update
    void update(CompetitionToImportance competitionToImportance);

    @Delete
    void delete(CompetitionToImportance competitionToImportance);

    @Query("SELECT * FROM CompetitionToImportance")
    LiveData<List<CompetitionToImportance>> getAll();

    @Query("SELECT competition_id FROM CompetitionToImportance where id = :id")
    long getCompetitionIdById(long id);

    @Query("SELECT importance_id FROM CompetitionToImportance where id = :id")
    Long getImportanceIdById(long id);

    @Query("SELECT * FROM CompetitionToImportance where id = :id")
    CompetitionToImportance getById(Long id);

    @Query("DELETE FROM CompetitionToImportance where id = :id")
    void deleteById(Long id);
}
