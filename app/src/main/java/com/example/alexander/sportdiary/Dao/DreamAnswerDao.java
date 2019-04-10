package com.example.alexander.sportdiary.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alexander.sportdiary.Entities.DreamAnswer;

import java.util.List;

@Dao
public interface DreamAnswerDao {
    @Insert
    long insert(DreamAnswer answer);

    @Update
    void update(DreamAnswer answer);

    @Delete
    void delete(DreamAnswer answer);

    @Query("SELECT * FROM DreamAnswer WHERE day_id = :dayId and question_id = :id")
    DreamAnswer getByDayAndQuestion(long dayId, long id);

    @Query("SELECT * FROM DreamAnswer WHERE day_id = :dayId and answer = 1")
    List<DreamAnswer> getAllByDayWhereAnswerIsYes(long dayId);
}
