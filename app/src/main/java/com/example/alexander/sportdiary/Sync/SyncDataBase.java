package com.example.alexander.sportdiary.Sync;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
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
import com.example.alexander.sportdiary.Dto.VersionDto;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.Entities.Version;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

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
        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://192.168.1.136:8082";
        HttpGet httpGet = new HttpGet(url + "/auth/unload?userId="+strings[0]);
        httpGet.setHeader("X-Firebase-Auth", strings[1]);
        try {
            Log.d("UNLOAD", "start execute");
            HttpResponse response = httpClient.execute(httpGet);
            System.out.println(response);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                save(result, strings[0]);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainActivity.getInstance().signOut();
        return null;
    }

    private void save(String data, String userId) {
        SportDataBase dataBase = MainActivity.getDatabase();
        try {
            UnloadDto dto = MainActivity.getObjectMapper().readValue(data, UnloadDto.class);
            Version version = dataBase.versionDao().getByUserId(userId);
            VersionDto versionDto = dto.getVersionDto();
            if (version != null && versionDto != null && versionDto.getVersion().equals(version.getVersion())) {
                Log.d("VERSION", "equal versions - not need unload");
                return;
            } else if (version != null) {
                dataBase.versionDao().delete(version);
            }
            dataBase.seasonPlanDao().deleteByUserId(userId);
            dataBase.dreamQuestionDao().deleteAll();
            dataBase.sanQuestionDao().deleteAll();
            dataBase.aimDao().deleteByUserId(userId);
            dataBase.blockDao().deleteByUserId(userId);
            dataBase.borgDao().deleteByUserId(userId);
            dataBase.campDao().deleteByUserId(userId);
            dataBase.competitionDao().deleteByUserId(userId);
            dataBase.equipmentDao().deleteByUserId(userId);
            dataBase.exerciseDao().deleteByUserId(userId);
            dataBase.importanceDao().deleteByUserId(userId);
            dataBase.restPlaceDao().deleteByUserId(userId);
            dataBase.stageDao().deleteByUserId(userId);
            dataBase.styleDao().deleteByUserId(userId);
            dataBase.tempoDao().delteByUserId(userId);
            dataBase.testDao().deleteByUserId(userId);
            dataBase.timeDao().deleteByUserId(userId);
            dataBase.trainingPlaceDao().deleteByUserId(userId);
            dataBase.typeDao().deleteByUserId(userId);
            dataBase.zoneDao().deleteByUserId(userId);
            if (dto.getDreamQuestionDtos() != null) {
                dto.getDreamQuestionDtos()
                        .stream()
                        .map(MainActivity.getConverter()::convertDtoToEntity)
                        .forEach(x -> dataBase.dreamQuestionDao().insert(x));
            }
            if (dto.getSanQuestionDtos() != null) {
                dto.getSanQuestionDtos()
                        .stream()
                        .map(MainActivity.getConverter()::convertDtoToEntity)
                        .forEach(x -> dataBase.sanQuestionDao().insert(x));
            }
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

            if (dto.getVersionDto() != null) {
                Log.d("VERSION", "get version from server");
                dataBase.versionDao().insert(MainActivity.getConverter().convertDtoToEntity(dto.getVersionDto()));
            } else {
                Log.d("VERSION", "initialize version for user");
                version = new Version(userId, 0L);
                long vid = dataBase.versionDao().insert(version);
                version.setId(vid);
                versionDto = MainActivity.getConverter().convertEntityToDto(version);
                MainActivity.syncSave(MainActivity.getObjectMapper().writeValueAsString(versionDto), Table.VERSION);
            }

            if (dto.getSeasonPlanDtos() == null) return;
            for (SeasonPlanDto seasonPlanDto : dto.getSeasonPlanDtos()) {
                dataBase.seasonPlanDao().insert(MainActivity.getConverter().convertDtoToEntity(seasonPlanDto));
                for (DayDto dayDto : seasonPlanDto.getDays()) {
                    dataBase.dayDao().insert(MainActivity.getConverter().convertDtoToEntity(dayDto));
                    if (dayDto.getTrainings() == null) dayDto.setTrainings(new ArrayList<>());
                    for (TrainingDto trainingDto : dayDto.getTrainings()) {
                        dataBase.trainingDao().insert(MainActivity.getConverter().convertDtoToEntity(trainingDto));
                        if (trainingDto.getTrainingExercises() == null) trainingDto.setTrainingExercises(new ArrayList<>());
                        for (TrainingExerciseDto trainingExerciseDto : trainingDto.getTrainingExercises()) {
                            dataBase.trainingExerciseDao().insert(MainActivity.getConverter().convertDtoToEntity(trainingExerciseDto));
                            if (trainingExerciseDto.getHeartRates() == null) trainingExerciseDto.setHeartRates(new ArrayList<>());
                            for (HeartRateDto heartRateDto : trainingExerciseDto.getHeartRates()) {
                                dataBase.heartRateDao().insert(MainActivity.getConverter().convertDtoToEntity(heartRateDto));
                            }
                        }
                        if (trainingDto.getTrainingsToAims() == null) trainingDto.setTrainingsToAims(new ArrayList<>());
                        for (TrainingsToAimsDto toAimsDto : trainingDto.getTrainingsToAims()) {
                            dataBase.trainingsToAimsDao().insert(MainActivity.getConverter().convertDtoToEntity(toAimsDto));
                        }
                        if (trainingDto.getTrainingsToEquipments() == null) trainingDto.setTrainingsToEquipments(new ArrayList<>());
                        for (TrainingsToEquipmentsDto toEquipmentsDto : trainingDto.getTrainingsToEquipments()) {
                            dataBase.trainingsToEquipmentsDao().insert(MainActivity.getConverter().convertDtoToEntity(toEquipmentsDto));
                        }
                    }
                    if (dayDto.getDayToTests() == null) dayDto.setDayToTests(new ArrayList<>());
                    for (DayToTestDto dayToTestDto : dayDto.getDayToTests()) {
                        dataBase.dayToTestDao().insert(MainActivity.getConverter().convertDtoToEntity(dayToTestDto));
                    }
                    if (dayDto.getRests() == null) dayDto.setRests(new ArrayList<>());
                    for (RestDto restDto : dayDto.getRests()) {
                        dataBase.restDao().insert(MainActivity.getConverter().convertDtoToEntity(restDto));
                    }
                    if (dayDto.getDreamAnswers() == null) dayDto.setDreamAnswers(new ArrayList<>());
                    for (DreamAnswerDto dreamAnswerDto : dayDto.getDreamAnswers()) {
                        dataBase.dreamAnswerDao().insert(MainActivity.getConverter().convertDtoToEntity(dreamAnswerDto));
                    }
                    if (dayDto.getSanAnswers() == null) dayDto.setSanAnswers(new ArrayList<>());
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
        if (editDtoList != null) {
            editDtoList.stream().map(converter).forEach(dao::insert);
        }
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
