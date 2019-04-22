package com.example.alexander.sportdiary.DataBase;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.Dao.SeasonPlanDao;
import com.example.alexander.sportdiary.Dto.DayDto;
import com.example.alexander.sportdiary.Dto.DayToTestDto;
import com.example.alexander.sportdiary.Dto.DreamAnswerDto;
import com.example.alexander.sportdiary.Dto.EditDto;
import com.example.alexander.sportdiary.Dto.HeartRateDto;
import com.example.alexander.sportdiary.Dto.RestDto;
import com.example.alexander.sportdiary.Dto.SanAnswerDto;
import com.example.alexander.sportdiary.Dto.SeasonPlanDto;
import com.example.alexander.sportdiary.Dto.TrainingDto;
import com.example.alexander.sportdiary.Dto.TrainingExerciseDto;
import com.example.alexander.sportdiary.Dto.TrainingsToAimsDto;
import com.example.alexander.sportdiary.Dto.TrainingsToEquipmentsDto;
import com.example.alexander.sportdiary.Dto.UnloadDto;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.Entities.HeartRate;
import com.example.alexander.sportdiary.Entities.SanAnswer;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.TrainingsToAims;
import com.example.alexander.sportdiary.Entities.TrainingsToEquipments;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class SyncDataBase extends AsyncTask<String, Void, Void> {

    @Override
    protected void onPostExecute(Void object) {
        Log.i("post", "POST EXECUTE");
        ProgressBar progressBar = MainActivity.getInstance().findViewById(R.id.loadBar);
        progressBar.setVisibility(View.GONE);
        ExpandableListView listView = MainActivity.getInstance().findViewById(R.id.expandableListView);
        listView.setEnabled(true);
        MainActivity.getInstance().fillDiariesMenu();
        super.onPostExecute(object);
    }

    @Override
    protected Void doInBackground(String... strings) {
//        save(strings[0]);
        return null;
    }

    private void save(String data) {
        SportDataBase dataBase = MainActivity.getInstance().getDatabase();
        try {
            UnloadDto dto = MainActivity.getObjectMapper().readValue(data, UnloadDto.class);
            dto.getDreamQuestionDtos()
                    .stream()
                    .map(MainActivity.getConverter()::convertDtoToEntity)
                    .forEach(x -> dataBase.dreamQuestionDao().insert(x));
            dto.getSanQuestionDtos()
                    .stream()
                    .map(MainActivity.getConverter()::convertDtoToEntity)
                    .forEach(x -> dataBase.sanQuestionDao().insert(x));
            addEdit(dto.getAims(), MainActivity.getConverter()::convertDtoToEntityAim, dataBase.aimDao());
            addEdit(dto.getBlock(), MainActivity.getConverter()::convertDtoToEntityBlock, dataBase.blockDao());
            addEdit(dto.getBorg(), MainActivity.getConverter()::convertDtoToEntityBorg, dataBase.borgDao());
            addEdit(dto.getStages(), MainActivity.getConverter()::convertDtoToEntityStage, dataBase.stageDao());
            addEdit(dto.getStyles(), MainActivity.getConverter()::convertDtoToEntityStyle, dataBase.styleDao());
            addEdit(dto.getCamps(), MainActivity.getConverter()::convertDtoToEntityCamp, dataBase.campDao());
            addEdit(dto.getCompetitions(), MainActivity.getConverter()::convertDtoToEntityCompetition, dataBase.competitionDao());
            addEdit(dto.getTempos(), MainActivity.getConverter()::convertDtoToEntityTempo, dataBase.tempoDao());
            addEdit(dto.getTests(), MainActivity.getConverter()::convertDtoToEntityTest, dataBase.testDao());
            addEdit(dto.getTimes(), MainActivity.getConverter()::convertDtoToEntityTime, dataBase.timeDao());
            addEdit(dto.getTrainingPlaces(), MainActivity.getConverter()::convertDtoToEntityTrainingPlace, dataBase.trainingPlaceDao());
            addEdit(dto.getTypes(), MainActivity.getConverter()::convertDtoToEntityType, dataBase.typeDao());
            addEdit(dto.getEquipments(), MainActivity.getConverter()::convertDtoToEntityEquipment, dataBase.equipmentDao());
            addEdit(dto.getExercises(), MainActivity.getConverter()::convertDtoToEntityExercise, dataBase.exerciseDao());
            addEdit(dto.getImportances(), MainActivity.getConverter()::convertDtoToEntityImportance, dataBase.importanceDao());
            addEdit(dto.getZones(), MainActivity.getConverter()::convertDtoToEntityZone, dataBase.zoneDao());
            addEdit(dto.getRestPlaces(), MainActivity.getConverter()::convertDtoToEntityRestPlace, dataBase.restPlaceDao());
            for (SeasonPlanDto seasonPlanDto : dto.getSeasonPlanDtos()) {
                dataBase.seasonPlanDao().insert(MainActivity.getConverter().convertDtoToEntity(seasonPlanDto));
                for (DayDto dayDto : seasonPlanDto.getDays()) {
                    dataBase.dayDao().insert(MainActivity.getConverter().convertDtoToEntity(dayDto));
                    for (TrainingDto trainingDto : dayDto.getTrainings()) {
                        dataBase.trainingDao().insert(MainActivity.getConverter().convertDtoToEntity(trainingDto));
                        for (TrainingExerciseDto trainingExerciseDto : trainingDto.getTrainingExercises()) {
                            dataBase.trainingExerciseDao().insert(MainActivity.getConverter().convertDtoToEntity(trainingExerciseDto));
                            for (HeartRateDto heartRateDto : trainingExerciseDto.getHeartRates()) {
                                dataBase.heartRateDao().insert(MainActivity.getConverter().convertDtoToEntity(heartRateDto));
                            }
                        }
                        for (TrainingsToAimsDto toAimsDto : trainingDto.getTrainingsToAims()) {
                            dataBase.trainingsToAimsDao().insert(MainActivity.getConverter().convertDtoToEntity(toAimsDto));
                        }
                        for (TrainingsToEquipmentsDto toEquipmentsDto : trainingDto.getTrainingsToEquipments()) {
                            dataBase.trainingsToEquipmentsDao().insert(MainActivity.getConverter().convertDtoToEntity(toEquipmentsDto));
                        }
                    }
                    for (DayToTestDto dayToTestDto : dayDto.getDayToTests()) {
                        dataBase.dayToTestDao().insert(MainActivity.getConverter().convertDtoToEntity(dayToTestDto));
                    }
                    for (RestDto restDto : dayDto.getRests()) {
                        dataBase.restDao().insert(MainActivity.getConverter().convertDtoToEntity(restDto));
                    }
                    for (DreamAnswerDto dreamAnswerDto : dayDto.getDreamAnswers()) {
                        dataBase.dreamAnswerDao().insert(MainActivity.getConverter().convertDtoToEntity(dreamAnswerDto));
                    }
                    for (SanAnswerDto sanAnswerDto : dayDto.getSanAnswers()) {
                        dataBase.sanAnswerDao().insert(MainActivity.getConverter().convertDtoToEntity(sanAnswerDto));
                    }
                    if (dayDto.getCompetitionToImportance() != null) {
                        dataBase.competitionToImportanceDao().insert(MainActivity.getConverter()
                                .convertDtoToEntity(dayDto.getCompetitionToImportance()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T extends Edit> void addEdit(List<EditDto> editDtoList,
                                          Function<EditDto, T> converter, EditDao<T> dao) {
        editDtoList.stream().map(converter).forEach(dao::insert);
    }

    @Override
    protected void onPreExecute() {
        Log.i("pre", "PRE EXECUTE");
        ProgressBar progressBar = MainActivity.getInstance().findViewById(R.id.loadBar);
        progressBar.setVisibility(View.VISIBLE);
        ExpandableListView listView = MainActivity.getInstance().findViewById(R.id.expandableListView);
        listView.setEnabled(false);
        super.onPreExecute();
    }
}
