package com.example.alexander.sportdiary.Dto;

import com.example.alexander.sportdiary.Enums.SanType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanQuestionDto {
    private Long id;
    private String positive;
    private String negative;
    private SanType type;
}
