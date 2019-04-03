package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

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

    @ColumnInfo(name = "exercise_id")
    private long exerciseId;

    @ColumnInfo(name = "style_id")
    private long styleId;

    @ColumnInfo(name = "tempo_id")
    private long tempoId;

    @ColumnInfo(name = "zone_id")
    private long zoneId;

    @ColumnInfo(name = "borg_id")
    private long borgId;

    private int work;
    private int rest;
    private int length;
    private int repeats;
    private int series;
    private String note;
    private int minutes;

    public TrainingExercise(long trainingId, long exerciseId, long styleId, long tempoId,
                            long zoneId, long borgId, int work, int rest, int length, int repeats, int series, String note, int minutes) {
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

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public long getStyleId() {
        return styleId;
    }

    public void setStyleId(long styleId) {
        this.styleId = styleId;
    }

    public long getTempoId() {
        return tempoId;
    }

    public void setTempoId(long tempoId) {
        this.tempoId = tempoId;
    }

    public long getZoneId() {
        return zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public long getBorgId() {
        return borgId;
    }

    public void setBorgId(long borgId) {
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
