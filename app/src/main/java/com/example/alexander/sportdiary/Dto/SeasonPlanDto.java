package com.example.alexander.sportdiary.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonPlanDto {
    private Long id;

    private String userId;
    private String name;
    private Date start;
    private String male = "М";
    private int hrMax = 200;
    private int hrRest = 60;
    private int lastPerformance;

    private List<DayDto> days;
}
