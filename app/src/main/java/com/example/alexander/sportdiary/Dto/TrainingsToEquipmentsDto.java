package com.example.alexander.sportdiary.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingsToEquipmentsDto {
    private Long id;

    @JsonProperty("training_id")
    private Long trainingId;

    @JsonProperty("equipment_id")
    private Long equipmentId;
}
