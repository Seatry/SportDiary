package com.example.alexander.sportdiary.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.SanAnswer;
import com.example.alexander.sportdiary.Entities.SanQuestion;

import java.util.List;

@Dao
public interface SanAnswerDao {
    @Insert
    long insert(SanAnswer answer);

    @Update
    void update(SanAnswer answer);

    @Delete
    void delete(SanAnswer answer);

    @Query("SELECT * FROM SanAnswer WHERE day_id = :dayId and question_id = :id")
    SanAnswer getByDayAndQuestion(long dayId, long id);

    @Query("SELECT * FROM SanAnswer WHERE day_id = :id")
    List<SanAnswer> getAllByDayId(long id);
}
