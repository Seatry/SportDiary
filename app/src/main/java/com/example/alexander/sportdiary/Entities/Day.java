package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.Nullable;

import com.example.alexander.sportdiary.Converters.DateConverter;
import com.example.alexander.sportdiary.Entities.EditEntities.Block;
import com.example.alexander.sportdiary.Entities.EditEntities.Camp;
import com.example.alexander.sportdiary.Entities.EditEntities.Competition;
import com.example.alexander.sportdiary.Entities.EditEntities.Importance;
import com.example.alexander.sportdiary.Entities.EditEntities.Stage;
import com.example.alexander.sportdiary.Entities.EditEntities.Type;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.SET_NULL;

@Entity(foreignKeys =
        {@ForeignKey(
                entity = SeasonPlan.class, parentColumns = "id", childColumns = "season_plan_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(
                entity = CompetitionToImportance.class, parentColumns = "id", childColumns = "competition_to_importance_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Block.class, parentColumns = "id", childColumns = "block_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Stage.class, parentColumns = "id", childColumns = "stage_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Type.class, parentColumns = "id", childColumns = "type_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Camp.class, parentColumns = "id", childColumns = "camp_id", onDelete = SET_NULL, onUpdate = CASCADE),
        },
        indices = {@Index(value = {"date", "season_plan_id"}, unique = true)})
@TypeConverters(DateConverter.class)
public class Day {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date date;
    private int capacity = 0;
    private double health = 0;
    private double mood = 0;
    private double activity = 0;
    private double dream = 0;

    public Day() {

    }

    @Nullable
    @ColumnInfo(name = "competition_to_importance_id")
    private Long competitionToImportanceId;

    @Nullable
    @ColumnInfo(name = "block_id")
    private Long blockId;

    @Nullable
    @ColumnInfo(name = "stage_id")
    private Long stageId;

    @Nullable
    @ColumnInfo(name = "type_id")
    private Long typeId;

    @Nullable
    @ColumnInfo(name = "camp_id")
    private Long campId;

    public Day(Date date, long seasonPlanId) {
        this.date = date;
        this.seasonPlanId = seasonPlanId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ColumnInfo(name = "season_plan_id")
    private long seasonPlanId;

    public long getSeasonPlanId() {
        return seasonPlanId;
    }

    public void setSeasonPlanId(long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
    }

    @Nullable
    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(@Nullable Long blockId) {
        this.blockId = blockId;
    }

    @Nullable
    public Long getStageId() {
        return stageId;
    }

    public void setStageId(@Nullable Long stageId) {
        this.stageId = stageId;
    }

    @Nullable
    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(@Nullable Long typeId) {
        this.typeId = typeId;
    }

    @Nullable
    public Long getCampId() {
        return campId;
    }

    public void setCampId(@Nullable Long campId) {
        this.campId = campId;
    }

    @Nullable
    public Long getCompetitionToImportanceId() {
        return competitionToImportanceId;
    }

    public void setCompetitionToImportanceId(@Nullable Long competitionToImportanceId) {
        this.competitionToImportanceId = competitionToImportanceId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getDream() {
        return dream;
    }

    public void setDream(double dream) {
        this.dream = dream;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMood() {
        return mood;
    }

    public void setMood(double mood) {
        this.mood = mood;
    }

    public double getActivity() {
        return activity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }
}
