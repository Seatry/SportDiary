package com.example.alexander.sportdiary.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayDto {
    private Long id;

    private Date date;
    private int capacity = 0;
    private double health = 0;
    private double mood = 0;
    private double activity = 0;
    private double dream = 0;

    @JsonProperty("competition_to_importance_id")
    private Long competitionToImportanceId;

    @JsonProperty("block_id")
    private Long blockId;

    @JsonProperty("stage_id")
    private Long stageId;

    @JsonProperty("type_id")
    private Long typeId;

    @JsonProperty("camp_id")
    private Long campId;

    @JsonProperty("season_plan_id")
    private Long seasonPlanId;

    private List<TrainingDto> trainings;

    private List<DayToTestDto> dayToTests;

    private List<RestDto> rests;

    private List<DreamAnswerDto> dreamAnswers;

    private List<SanAnswerDto> sanAnswers;

    private CompetitionToImportanceDto competitionToImportance;
}
