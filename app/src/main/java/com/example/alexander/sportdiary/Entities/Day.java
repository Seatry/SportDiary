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
                entity = Competition.class, parentColumns = "id", childColumns = "competition_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Block.class, parentColumns = "id", childColumns = "block_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Stage.class, parentColumns = "id", childColumns = "stage_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Type.class, parentColumns = "id", childColumns = "type_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Camp.class, parentColumns = "id", childColumns = "camp_id", onDelete = SET_NULL, onUpdate = CASCADE),
        @ForeignKey(
                entity = Importance.class, parentColumns = "id", childColumns = "importance_id", onDelete = SET_NULL, onUpdate = CASCADE),
        },
        indices = {@Index(value = {"date", "season_plan_id"}, unique = true)})
@TypeConverters(DateConverter.class)
public class Day {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date date;

    public Day() {

    }

    @Nullable
    @ColumnInfo(name = "competition_id")
    private Long competitionId;

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

    @Nullable
    @ColumnInfo(name = "importance_id")
    private Long importanceId;

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
        seasonPlanId = seasonPlanId;
    }

    @Nullable
    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(@Nullable Long competitionId) {
        this.competitionId = competitionId;
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
    public Long getImportanceId() {
        return importanceId;
    }

    public void setImportanceId(@Nullable Long importanceId) {
        this.importanceId = importanceId;
    }
}
