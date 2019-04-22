package com.example.alexander.sportdiary.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.DreamQuestion;

import java.util.List;

@Dao
public interface DreamQuestionDao {
    @Insert
    long insert(DreamQuestion question);

    @Update
    void update(DreamQuestion question);

    @Delete
    void delete(DreamQuestion question);

    @Query("SELECT * FROM DreamQuestion")
    List<DreamQuestion> getAll();

    @Query("DELETE FROM DreamQuestion")
    void deleteAll();
}
