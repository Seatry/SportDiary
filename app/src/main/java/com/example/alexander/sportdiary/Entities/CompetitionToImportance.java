package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import com.example.alexander.sportdiary.Entities.EditEntities.Competition;
import com.example.alexander.sportdiary.Entities.EditEntities.Importance;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.SET_NULL;

@Entity(foreignKeys =
        {@ForeignKey(
                entity = Competition.class, parentColumns = "id", childColumns = "competition_id", onDelete = CASCADE, onUpdate = CASCADE),
        @ForeignKey(
                entity = Importance.class, parentColumns = "id", childColumns = "importance_id", onDelete = SET_NULL, onUpdate = CASCADE)
        })
public class CompetitionToImportance {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "competition_id")
    private long competitionId;

    @Nullable
    @ColumnInfo(name = "importance_id")
    private Long importanceId;


    public CompetitionToImportance(long competitionId, @Nullable Long importanceId) {
        this.competitionId = competitionId;
        this.importanceId = importanceId;
    }

    public CompetitionToImportance() {

    }

    @Nullable
    public Long getImportanceId() {
        return importanceId;
    }

    public void setImportanceId(@Nullable Long importanceId) {
        this.importanceId = importanceId;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(long competitionId) {
        this.competitionId = competitionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
