package com.example.alexander.sportdiary;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.alexander.sportdiary.Dao.CompetitionToImportanceDao;
import com.example.alexander.sportdiary.Dao.DayDao;
import com.example.alexander.sportdiary.Dao.DayToTestDao;
import com.example.alexander.sportdiary.Dao.DreamAnswerDao;
import com.example.alexander.sportdiary.Dao.DreamQuestionDao;
import com.example.alexander.sportdiary.Dao.EditDao.AimDao;
import com.example.alexander.sportdiary.Dao.EditDao.BlockDao;
import com.example.alexander.sportdiary.Dao.EditDao.BorgDao;
import com.example.alexander.sportdiary.Dao.EditDao.CampDao;
import com.example.alexander.sportdiary.Dao.EditDao.CompetitionDao;
import com.example.alexander.sportdiary.Dao.EditDao.EquipmentDao;
import com.example.alexander.sportdiary.Dao.EditDao.ExerciseDao;
import com.example.alexander.sportdiary.Dao.EditDao.ImportanceDao;
import com.example.alexander.sportdiary.Dao.EditDao.RestPlaceDao;
import com.example.alexander.sportdiary.Dao.EditDao.StageDao;
import com.example.alexander.sportdiary.Dao.EditDao.StyleDao;
import com.example.alexander.sportdiary.Dao.EditDao.TempoDao;
import com.example.alexander.sportdiary.Dao.EditDao.TestDao;
import com.example.alexander.sportdiary.Dao.EditDao.TimeDao;
import com.example.alexander.sportdiary.Dao.EditDao.TrainingPlaceDao;
import com.example.alexander.sportdiary.Dao.EditDao.TypeDao;
import com.example.alexander.sportdiary.Dao.HeartRateDao;
import com.example.alexander.sportdiary.Dao.RestDao;
import com.example.alexander.sportdiary.Dao.SanAnswerDao;
import com.example.alexander.sportdiary.Dao.SanQuestionDao;
import com.example.alexander.sportdiary.Dao.SeasonPlanDao;
import com.example.alexander.sportdiary.Dao.EditDao.ZoneDao;
import com.example.alexander.sportdiary.Dao.TrainingDao;
import com.example.alexander.sportdiary.Dao.TrainingExerciseDao;
import com.example.alexander.sportdiary.Dao.TrainingsToAimsDao;
import com.example.alexander.sportdiary.Dao.TrainingsToEquipmentsDao;
import com.example.alexander.sportdiary.Entities.CompetitionToImportance;
import com.example.alexander.sportdiary.Entities.DayToTest;
import com.example.alexander.sportdiary.Entities.DreamAnswer;
import com.example.alexander.sportdiary.Entities.DreamQuestion;
import com.example.alexander.sportdiary.Entities.EditEntities.Aim;
import com.example.alexander.sportdiary.Entities.EditEntities.Block;
import com.example.alexander.sportdiary.Entities.EditEntities.Borg;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.EditEntities.Camp;
import com.example.alexander.sportdiary.Entities.EditEntities.Competition;
import com.example.alexander.sportdiary.Entities.EditEntities.Equipment;
import com.example.alexander.sportdiary.Entities.EditEntities.Exercise;
import com.example.alexander.sportdiary.Entities.EditEntities.Importance;
import com.example.alexander.sportdiary.Entities.EditEntities.RestPlace;
import com.example.alexander.sportdiary.Entities.EditEntities.Stage;
import com.example.alexander.sportdiary.Entities.EditEntities.Style;
import com.example.alexander.sportdiary.Entities.EditEntities.Tempo;
import com.example.alexander.sportdiary.Entities.EditEntities.Test;
import com.example.alexander.sportdiary.Entities.EditEntities.TrainingPlace;
import com.example.alexander.sportdiary.Entities.EditEntities.Type;
import com.example.alexander.sportdiary.Entities.HeartRate;
import com.example.alexander.sportdiary.Entities.Rest;
import com.example.alexander.sportdiary.Entities.SanAnswer;
import com.example.alexander.sportdiary.Entities.SanQuestion;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.EditEntities.Time;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.EditEntities.Zone;
import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.Entities.TrainingsToAims;
import com.example.alexander.sportdiary.Entities.TrainingsToEquipments;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Database(entities =
        {Exercise.class, Zone.class, SeasonPlan.class, Day.class, Equipment.class, Aim.class,
                Time.class, Borg.class, TrainingsToEquipments.class, TrainingExercise.class,
                Training.class, TrainingPlace.class, TrainingsToAims.class, Style.class,
                Tempo.class, Block.class, Camp.class, Competition.class, Importance.class,
                Stage.class, Type.class, CompetitionToImportance.class, HeartRate.class,
                RestPlace.class, Test.class, DayToTest.class, Rest.class, DreamQuestion.class,
                SanQuestion.class, DreamAnswer.class, SanAnswer.class
        }, version = 29)
public abstract class SportDataBase extends RoomDatabase {
    public abstract ExerciseDao exerciseDao();
    public abstract ZoneDao zoneDao();
    public abstract SeasonPlanDao seasonPlanDao();
    public abstract DayDao dayDao();
    public abstract EquipmentDao equipmentDao();
    public abstract AimDao aimDao();
    public abstract TimeDao timeDao();
    public abstract BorgDao borgDao();
    public abstract TrainingDao trainingDao();
    public abstract TrainingPlaceDao trainingPlaceDao();
    public abstract TrainingsToAimsDao trainingsToAimsDao();
    public abstract TrainingsToEquipmentsDao trainingsToEquipmentsDao();
    public abstract StyleDao styleDao();
    public abstract TempoDao tempoDao();
    public abstract TrainingExerciseDao trainingExerciseDao();
    public abstract BlockDao blockDao();
    public abstract CampDao campDao();
    public abstract CompetitionDao competitionDao();
    public abstract ImportanceDao importanceDao();
    public abstract StageDao stageDao();
    public abstract TypeDao typeDao();
    public abstract CompetitionToImportanceDao competitionToImportanceDao();
    public abstract HeartRateDao heartRateDao();
    public abstract RestPlaceDao restPlaceDao();
    public abstract TestDao testDao();
    public abstract DayToTestDao dayToTestDao();
    public abstract RestDao restDao();
    public abstract DreamQuestionDao dreamQuestionDao();
    public abstract SanQuestionDao sanQuestionDao();
    public abstract SanAnswerDao sanAnswerDao();
    public abstract DreamAnswerDao dreamAnswerDao();
}
