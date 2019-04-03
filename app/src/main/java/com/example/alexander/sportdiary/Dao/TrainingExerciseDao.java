package com.example.alexander.sportdiary.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.alexander.sportdiary.Entities.TrainingExercise;

import java.util.List;

@Dao
public interface TrainingExerciseDao {
    @Insert
    long insert(TrainingExercise trainingExercise);

    @Update
    void update(TrainingExercise trainingExercise);

    @Delete
    void delete(TrainingExercise trainingExercise);

    @Query("SELECT * FROM TrainingExercise")
    LiveData<List<TrainingExercise>> getAll();

    @Query("SELECT * FROM TrainingExercise WHERE training_id = :id")
    LiveData<List<TrainingExercise>> getAllByTrainingId(long id);
}
