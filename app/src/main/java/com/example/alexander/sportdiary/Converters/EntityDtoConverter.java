package com.example.alexander.sportdiary.Converters;


import com.example.alexander.sportdiary.Dto.*;
import com.example.alexander.sportdiary.Entities.EditEntities.*;
import com.example.alexander.sportdiary.Entities.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class EntityDtoConverter {
    private final ModelMapper modelMapper = new ModelMapper();

    public EntityDtoConverter() {
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public EditDto convertEntityToDto(Edit edit) {
        return modelMapper.map(edit, EditDto.class);
    }

    public Time convertDtoToEntityTime(EditDto editDto) {
        return modelMapper.map(editDto, Time.class);
    }

    public Aim convertDtoToEntityAim(EditDto editDto) {
        return modelMapper.map(editDto, Aim.class);
    }

    public Block convertDtoToEntityBlock(EditDto editDto) {
        return modelMapper.map(editDto, Block.class);
    }

    public Borg convertDtoToEntityBorg(EditDto editDto) {
        return modelMapper.map(editDto, Borg.class);
    }

    public Camp convertDtoToEntityCamp(EditDto editDto) {
        return modelMapper.map(editDto, Camp.class);
    }

    public Competition convertDtoToEntityCompetition(EditDto editDto) {
        return modelMapper.map(editDto, Competition.class);
    }

    public Equipment convertDtoToEntityEquipment(EditDto editDto) {
        return modelMapper.map(editDto, Equipment.class);
    }

    public Exercise convertDtoToEntityExercise(EditDto editDto) {
        return modelMapper.map(editDto, Exercise.class);
    }

    public Importance convertDtoToEntityImportance(EditDto editDto) {
        return modelMapper.map(editDto, Importance.class);
    }

    public RestPlace convertDtoToEntityRestPlace(EditDto editDto) {
        return modelMapper.map(editDto, RestPlace.class);
    }

    public Stage convertDtoToEntityStage(EditDto editDto) {
        return modelMapper.map(editDto, Stage.class);
    }

    public Style convertDtoToEntityStyle(EditDto editDto) {
        return modelMapper.map(editDto, Style.class);
    }

    public Tempo convertDtoToEntityTempo(EditDto editDto) {
        return modelMapper.map(editDto, Tempo.class);
    }

    public Test convertDtoToEntityTest(EditDto editDto) {
        return modelMapper.map(editDto, Test.class);
    }

    public TrainingPlace convertDtoToEntityTrainingPlace(EditDto editDto) {
        return modelMapper.map(editDto, TrainingPlace.class);
    }

    public Type convertDtoToEntityType(EditDto editDto) {
        return modelMapper.map(editDto, Type.class);
    }

    public Zone convertDtoToEntityZone(EditDto editDto) {
        return modelMapper.map(editDto, Zone.class);
    }

    public CompetitionToImportanceDto convertEntityToDto(CompetitionToImportance competitionToImportance) {
        return modelMapper.map(competitionToImportance, CompetitionToImportanceDto.class);
    }

    public CompetitionToImportance convertDtoToEntity(CompetitionToImportanceDto competitionToImportanceDto) {
        return modelMapper.map(competitionToImportanceDto, CompetitionToImportance.class);
    }

    public DayDto convertEntityToDto(Day day) {
        return modelMapper.map(day, DayDto.class);
    }

    public Day convertDtoToEntity(DayDto dayDto) {
        return modelMapper.map(dayDto, Day.class);
    }

    public DayToTestDto convertEntityToDto(DayToTest dayToTest) {
        return modelMapper.map(dayToTest, DayToTestDto.class);
    }

    public DayToTest convertDtoToEntity(DayToTestDto dayToTestDto) {
        return modelMapper.map(dayToTestDto, DayToTest.class);
    }

    public DreamAnswerDto convertEntityToDto(DreamAnswer dreamAnswer) {
        return modelMapper.map(dreamAnswer, DreamAnswerDto.class);
    }

    public DreamAnswer convertDtoToEntity(DreamAnswerDto dreamAnswerDto) {
        return modelMapper.map(dreamAnswerDto, DreamAnswer.class);
    }

    public DreamQuestionDto convertEntityToDto(DreamQuestion dreamQuestion) {
        return modelMapper.map(dreamQuestion, DreamQuestionDto.class);
    }

    public DreamQuestion convertDtoToEntity(DreamQuestionDto dreamQuestionDto) {
        return modelMapper.map(dreamQuestionDto, DreamQuestion.class);
    }

    public HeartRateDto convertEntityToDto(HeartRate heartRate) {
        return modelMapper.map(heartRate, HeartRateDto.class);
    }

    public HeartRate convertDtoToEntity(HeartRateDto heartRateDto) {
        return modelMapper.map(heartRateDto, HeartRate.class);
    }

    public RestDto convertEntityToDto(Rest rest) {
        return modelMapper.map(rest, RestDto.class);
    }

    public Rest convertDtoToEntity(RestDto restDto) {
        return modelMapper.map(restDto,Rest.class);
    }

    public SanAnswerDto convertEntityToDto(SanAnswer sanAnswer) {
        return modelMapper.map(sanAnswer, SanAnswerDto.class);
    }

    public SanAnswer convertDtoToEntity(SanAnswerDto sanAnswerDto) {
        return modelMapper.map(sanAnswerDto, SanAnswer.class);
    }

    public SanQuestionDto convertEntityToDto(SanQuestion sanQuestion) {
        return modelMapper.map(sanQuestion, SanQuestionDto.class);
    }

    public SanQuestion convertDtoToEntity(SanQuestionDto sanQuestionDto) {
        return modelMapper.map(sanQuestionDto, SanQuestion.class);
    }

    public SeasonPlanDto convertEntityToDto(SeasonPlan seasonPlan) {
        return modelMapper.map(seasonPlan, SeasonPlanDto.class);
    }

    public SeasonPlan convertDtoToEntity(SeasonPlanDto seasonPlanDto) {
        return modelMapper.map(seasonPlanDto, SeasonPlan.class);
    }

    public TrainingDto convertEntityToDto(Training training) {
        return modelMapper.map(training, TrainingDto.class);
    }

    public Training convertDtoToEntity(TrainingDto trainingDto) {
        return modelMapper.map(trainingDto, Training.class);
    }

    public TrainingExerciseDto convertEntityToDto(TrainingExercise trainingExercise) {
        return modelMapper.map(trainingExercise, TrainingExerciseDto.class);
    }

    public TrainingExercise convertDtoToEntity(TrainingExerciseDto trainingExerciseDto) {
        return modelMapper.map(trainingExerciseDto, TrainingExercise.class);
    }

    public TrainingsToAimsDto convertEntityToDto(TrainingsToAims trainingsToAims) {
        return modelMapper.map(trainingsToAims, TrainingsToAimsDto.class);
    }

    public TrainingsToAims convertDtoToEntity(TrainingsToAimsDto trainingsToAimsDto) {
        return modelMapper.map(trainingsToAimsDto, TrainingsToAims.class);
    }

    public TrainingsToEquipmentsDto convertEntityToDto(TrainingsToEquipments trainingsToEquipments) {
        return modelMapper.map(trainingsToEquipments, TrainingsToEquipmentsDto.class);
    }

    public TrainingsToEquipments convertDtoToEntity(TrainingsToEquipmentsDto trainingsToEquipmentsDto) {
        return modelMapper.map(trainingsToEquipmentsDto, TrainingsToEquipments.class);
    }
}
