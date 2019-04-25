package com.example.alexander.sportdiary.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnloadDto {
    private List<SeasonPlanDto> seasonPlanDtos;

    private List<SanQuestionDto> sanQuestionDtos;
    private List<DreamQuestionDto> dreamQuestionDtos;

    private List<EditDto> times;
    private List<EditDto> aims;
    private List<EditDto> block;
    private List<EditDto> borg;
    private List<EditDto> camps;
    private List<EditDto> competitions;
    private List<EditDto> equipments;
    private List<EditDto> exercises;
    private List<EditDto> importances;
    private List<EditDto> restPlaces;
    private List<EditDto> stages;
    private List<EditDto> styles;
    private List<EditDto> tempos;
    private List<EditDto> tests;
    private List<EditDto> trainingPlaces;
    private List<EditDto> types;
    private List<EditDto> zones;
    private VersionDto versionDto;
}
