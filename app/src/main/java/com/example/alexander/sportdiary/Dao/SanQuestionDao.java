package com.example.alexander.sportdiary.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.SanQuestion;

import java.util.List;

@Dao
public interface SanQuestionDao {
    @Insert
    long insert(SanQuestion question);

    @Update
    void update(SanQuestion sanQuestion);

    @Delete
    void delete(SanQuestion sanQuestion);

    @Query("SELECT * FROM SanQuestion")
    List<SanQuestion> getAll();

    @Query("SELECT * FROM SanQuestion WHERE id = :questionId")
    SanQuestion getById(long questionId);
}
