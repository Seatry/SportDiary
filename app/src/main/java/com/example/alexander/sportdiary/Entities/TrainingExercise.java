package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import com.example.alexander.sportdiary.Entities.EditEntities.Borg;
import com.example.alexander.sportdiary.Entities.EditEntities.Exercise;
import com.example.alexander.sportdiary.Entities.EditEntities.Style;
import com.example.alexander.sportdiary.Entities.EditEntities.Tempo;
import com.example.alexander.sportdiary.Entities.EditEntities.Zone;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.SET_NULL;

@Entity(foreignKeys =
        {@ForeignKey(
                entity = Training.class, parentColumns = "id", childColumns = "training_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(
                entity = Exercise.class, parentColumns = "id", childColumns = "exercise_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Style.class, parentColumns = "id", childColumns = "style_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Tempo.class, parentColumns = "id", childColumns = "tempo_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Zone.class, parentColumns = "id", childColumns = "zone_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Borg.class, parentColumns = "id", childColumns = "borg_id", onDelete = SET_NULL, onUpdate = CASCADE
        )})
public class TrainingExercise {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "training_id")
    private long trainingId;

    @Nullable
    @ColumnInfo(name = "exercise_id")
    private Long exerciseId;

    @Nullable
    @ColumnInfo(name = "style_id")
    private Long styleId;

    @Nullable
    @ColumnInfo(name = "tempo_id")
    private Long tempoId;

    @Nullable
    @ColumnInfo(name = "zone_id")
    private Long zoneId;

    @Nullable
    @ColumnInfo(name = "borg_id")
    private Long borgId;

    private int work = 1;
    private int rest = 1;
    private int length = 0;
    private int repeats = 0;
    private int series = 0;
    private String note = "";
    private int minutes = 0;

    public TrainingExercise() {

    }

    public TrainingExercise(long trainingId, @Nullable Long exerciseId, @Nullable Long styleId, @Nullable Long tempoId,
                            @Nullable Long zoneId, @Nullable Long borgId, int work, int rest, int length, int repeats, int series, String note, int minutes) {
        this.trainingId = trainingId;
        this.exerciseId = exerciseId;
        this.styleId = styleId;
        this.tempoId = tempoId;
        this.zoneId = zoneId;
        this.borgId = borgId;
        this.work = work;
        this.rest = rest;
        this.length = length;
        this.repeats = repeats;
        this.series = series;
        this.note = note;
        this.minutes = minutes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
    }

    @Nullable
    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(@Nullable Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Nullable
    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(@Nullable Long styleId) {
        this.styleId = styleId;
    }

    @Nullable
    public Long getTempoId() {
        return tempoId;
    }

    public void setTempoId(@Nullable Long tempoId) {
        this.tempoId = tempoId;
    }

    @Nullable
    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(@Nullable Long zoneId) {
        this.zoneId = zoneId;
    }

    @Nullable
    public Long getBorgId() {
        return borgId;
    }

    public void setBorgId(@Nullable Long borgId) {
        this.borgId = borgId;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
