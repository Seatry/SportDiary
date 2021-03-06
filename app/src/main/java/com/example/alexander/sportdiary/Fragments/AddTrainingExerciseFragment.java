package com.example.alexander.sportdiary.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alexander.sportdiary.Dto.HeartRateDto;
import com.example.alexander.sportdiary.Dto.TrainingExerciseDto;
import com.example.alexander.sportdiary.Entities.Rest;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.HeartRate;
import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;

import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolSpinner;

public class AddTrainingExerciseFragment extends DialogFragment implements View.OnClickListener {
    private SportDataBase sportDataBase;
    private long trainingId;
    private String title;
    private EditOption option;
    private TrainingExercise updateTrainingExercise = new TrainingExercise();
    private Spinner exerciseSpinner;
    private Spinner styleSpinner;
    private Spinner tempoSpinner;
    private Spinner zoneSpinner;
    private EditText workEdit;
    private EditText restEdit;
    private EditText seriesEdit;
    private EditText repeatsEdit;
    private EditText lengthEdit;
    private EditText timeEdit;
    private Spinner borgSpinner;
    private EditText noteEdit;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_training_exercise, container, false);
        v.findViewById(R.id.cancelAddTrainingExercise).setOnClickListener(this);
        v.findViewById(R.id.okAddTrainingExercise).setOnClickListener(this);
        ((TextView)v.findViewById(R.id.addTrainingExerciseTitle)).setText(title);

        sportDataBase = MainActivity.getDatabase();

        exerciseSpinner = v.findViewById(R.id.exerciseSpinner);
        styleSpinner = v.findViewById(R.id.styleSpinner);
        tempoSpinner = v.findViewById(R.id.tempoSpinner);
        zoneSpinner = v.findViewById(R.id.zoneSpinner);
        workEdit = v.findViewById(R.id.workEdit);
        restEdit = v.findViewById(R.id.restEdit);
        seriesEdit = v.findViewById(R.id.seriesEdit);
        repeatsEdit = v.findViewById(R.id.repeatsEdit);
        lengthEdit = v.findViewById(R.id.lengthEdit);
        timeEdit = v.findViewById(R.id.timeExerciseEdit);
        borgSpinner = v.findViewById(R.id.borgExerciseSpinner);
        noteEdit = v.findViewById(R.id.noteEdit);

        toolSpinner(sportDataBase.exerciseDao(), exerciseSpinner, sportDataBase.exerciseDao().getNameByIdAndUserId(updateTrainingExercise.getExerciseId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.styleDao(), styleSpinner, sportDataBase.styleDao().getNameByIdAndUserId(updateTrainingExercise.getStyleId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.tempoDao(), tempoSpinner, sportDataBase.tempoDao().getNameByIdAndUserId(updateTrainingExercise.getTempoId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.zoneDao(), zoneSpinner, sportDataBase.zoneDao().getNameByIdAndUserId(updateTrainingExercise.getZoneId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.borgDao(), borgSpinner, sportDataBase.borgDao().getNameByIdAndUserId(updateTrainingExercise.getBorgId(), MainActivity.getUserId()));

        if (option == EditOption.UPDATE) {
            fillEdits();
        }

        return v;
    }

    private void fillEdits() {
        workEdit.setText(String.valueOf(updateTrainingExercise.getWork()));
        restEdit.setText(String.valueOf(updateTrainingExercise.getRest()));
        seriesEdit.setText(String.valueOf(updateTrainingExercise.getSeries()));
        repeatsEdit.setText(String.valueOf(updateTrainingExercise.getRepeats()));
        lengthEdit.setText(String.valueOf(updateTrainingExercise.getLength()));
        timeEdit.setText(String.valueOf(updateTrainingExercise.getMinutes()));
        noteEdit.setText(updateTrainingExercise.getNote());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelAddTrainingExercise:
                dismiss();
                break;
            case R.id.okAddTrainingExercise:
                switch (option) {
                    case INSERT:
                        AsyncTask.execute(this::add);
                        break;
                    case UPDATE:
                        AsyncTask.execute(this::update);
                        break;
                }
                dismiss();
                break;
        }
    }

    public void add() {
        Long exerciseId = exerciseSpinner.getSelectedItem() == null ? null
                : sportDataBase.exerciseDao().getIdByName(exerciseSpinner.getSelectedItem().toString());
        Long styleId = styleSpinner.getSelectedItem() == null ? null
                : sportDataBase.styleDao().getIdByName(styleSpinner.getSelectedItem().toString());
        Long tempoId = tempoSpinner.getSelectedItem() == null ? null
                : sportDataBase.tempoDao().getIdByName(tempoSpinner.getSelectedItem().toString());
        Long zoneId = zoneSpinner.getSelectedItem() == null ? null
                : sportDataBase.zoneDao().getIdByName(zoneSpinner.getSelectedItem().toString());
        Long borgId = borgSpinner.getSelectedItem() == null ? null
                : sportDataBase.borgDao().getIdByName(borgSpinner.getSelectedItem().toString());
        int work = workEdit.getText().length() == 0 ? 1 : Integer.parseInt(workEdit.getText().toString());
        int rest = restEdit.getText().length() == 0 ? 1 : Integer.parseInt(restEdit.getText().toString());
        int length = lengthEdit.getText().length() == 0 ? 0 : Integer.parseInt(lengthEdit.getText().toString());
        int repeats = repeatsEdit.getText().length() == 0 ? 0 : Integer.parseInt(repeatsEdit.getText().toString());
        int series = seriesEdit.getText().length() == 0 ? 0 : Integer.parseInt(seriesEdit.getText().toString());
        String note = noteEdit.getText().toString();
        int minutes = timeEdit.getText().length() == 0 ? 0 : Integer.parseInt(timeEdit.getText().toString());
        TrainingExercise trainingExercise = new TrainingExercise(trainingId, exerciseId, styleId, tempoId, zoneId,
                borgId, work, rest, length, repeats, series, note, minutes);
        long id = sportDataBase.trainingExerciseDao().insert(trainingExercise);
        trainingExercise.setId(id);
        TrainingExerciseDto trainingExerciseDto = MainActivity.getConverter().convertEntityToDto(trainingExercise);
        List<HeartRateDto> heartRateDtos = addHeartRates(0, 0, series, repeats, id);
        trainingExerciseDto.setHeartRates(heartRateDtos);
        try {
            MainActivity.syncSave(MainActivity.getObjectMapper().writeValueAsString(trainingExerciseDto), Table.TRAINING_EXERCISE);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<HeartRateDto> addHeartRates(int oldSeries, int oldRepeats, int series, int repeats, long id) {
        List<HeartRateDto> heartRateDtos = new ArrayList<>();
        for(int i = 0; i < series; i++) {
            int j = i < oldSeries ? oldRepeats : 0;
            for(; j < repeats; j++) {
                HeartRate heartRate = new HeartRate(id, TrainingExerciseActivity.getInstance().getString(R.string.before), i+1, j+1);
                long hid = sportDataBase.heartRateDao().insert(heartRate);
                heartRate.setId(hid);
                heartRate = new HeartRate(id, TrainingExerciseActivity.getInstance().getString(R.string.after), i+1, j+1);
                hid = sportDataBase.heartRateDao().insert(heartRate);
                heartRate.setId(hid);
                heartRateDtos.add(MainActivity.getConverter().convertEntityToDto(heartRate));

            }
        }
        return heartRateDtos;
    }

    public void update() {
        Long exerciseId = exerciseSpinner.getSelectedItem() == null ? null
                : sportDataBase.exerciseDao().getIdByName(exerciseSpinner.getSelectedItem().toString());
        Long styleId = styleSpinner.getSelectedItem() == null ? null
                : sportDataBase.styleDao().getIdByName(styleSpinner.getSelectedItem().toString());
        Long tempoId = tempoSpinner.getSelectedItem() == null ? null
                : sportDataBase.tempoDao().getIdByName(tempoSpinner.getSelectedItem().toString());
        Long zoneId = zoneSpinner.getSelectedItem() == null ? null
                : sportDataBase.zoneDao().getIdByName(zoneSpinner.getSelectedItem().toString());
        Long borgId = borgSpinner.getSelectedItem() == null ? null
                : sportDataBase.borgDao().getIdByName(borgSpinner.getSelectedItem().toString());
        int work = workEdit.getText().length() == 0 ? 1 : Integer.parseInt(workEdit.getText().toString());
        int rest = restEdit.getText().length() == 0 ? 1 : Integer.parseInt(restEdit.getText().toString());
        int length = lengthEdit.getText().length() == 0 ? 0 : Integer.parseInt(lengthEdit.getText().toString());
        int repeats = repeatsEdit.getText().length() == 0 ? 0 : Integer.parseInt(repeatsEdit.getText().toString());
        int series = seriesEdit.getText().length() == 0 ? 0 : Integer.parseInt(seriesEdit.getText().toString());
        String note = noteEdit.getText().toString();
        int minutes = timeEdit.getText().length() == 0 ? 0 : Integer.parseInt(timeEdit.getText().toString());

        int oldSeries = updateTrainingExercise.getSeries();
        int oldRepeats = updateTrainingExercise.getRepeats();

        updateTrainingExercise.setExerciseId(exerciseId);
        updateTrainingExercise.setStyleId(styleId);
        updateTrainingExercise.setTempoId(tempoId);
        updateTrainingExercise.setZoneId(zoneId);
        updateTrainingExercise.setBorgId(borgId);
        updateTrainingExercise.setWork(work);
        updateTrainingExercise.setRest(rest);
        updateTrainingExercise.setLength(length);
        updateTrainingExercise.setRepeats(repeats);
        updateTrainingExercise.setSeries(series);
        updateTrainingExercise.setNote(note);
        updateTrainingExercise.setMinutes(minutes);

        sportDataBase.trainingExerciseDao().update(updateTrainingExercise);
        TrainingExerciseDto trainingExerciseDto = MainActivity.getConverter().convertEntityToDto(updateTrainingExercise);

        if (oldSeries != series || oldRepeats != repeats) {
            sportDataBase.heartRateDao().getByExerciseIdWithGreaterSeriesOrRepeat(updateTrainingExercise.getId(), series, repeats)
                    .forEach(x -> {
                        sportDataBase.heartRateDao().delete(x);
                        MainActivity.syncDelete(x.getId(), Table.HEART_RATE);
                    });
            List<HeartRateDto> heartRateDtos = addHeartRates(oldSeries, oldRepeats, series, repeats, updateTrainingExercise.getId());
            trainingExerciseDto.setHeartRates(heartRateDtos);
            double hr = 0;
            List<Integer> hrList = sportDataBase.heartRateDao().getHrByExerciseId(updateTrainingExercise.getId());
            if (hrList.size() != 0 ) {
                for (Integer pulse : hrList) {
                    hr += pulse;
                }
                hr /= hrList.size();
                updateTrainingExercise.setHrAvg(hr);
                sportDataBase.trainingExerciseDao().update(updateTrainingExercise);
                trainingExerciseDto.setHrAvg(hr);
            }
        }
        try {
            MainActivity.syncSave(MainActivity.getObjectMapper().writeValueAsString(trainingExerciseDto), Table.TRAINING_EXERCISE);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public AddTrainingExerciseFragment setUpdateTrainingExercise(TrainingExercise updateTrainingExercise) {
        this.updateTrainingExercise = updateTrainingExercise;
        return this;
    }

    public AddTrainingExerciseFragment setOption(EditOption option) {
        this.option = option;
        return this;
    }

    public AddTrainingExerciseFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public AddTrainingExerciseFragment setTrainingId(long trainingId) {
        this.trainingId = trainingId;
        return this;
    }
}
