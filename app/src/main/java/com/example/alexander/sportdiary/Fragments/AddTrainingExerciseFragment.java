package com.example.alexander.sportdiary.Fragments;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alexander.sportdiary.EditOption;
import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;

import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolSpinner;

public class AddTrainingExerciseFragment extends DialogFragment implements View.OnClickListener {
    private SportDataBase sportDataBase;
    private long trainingId;
    private String title;
    private EditOption option;
    private TrainingExercise updateTrainingExercise;
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

        sportDataBase = MainActivity.getInstance().getDatabase();

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

        toolSpinner(sportDataBase.exerciseDao(), exerciseSpinner);
        toolSpinner(sportDataBase.styleDao(), styleSpinner);
        toolSpinner(sportDataBase.tempoDao(), tempoSpinner);
        toolSpinner(sportDataBase.zoneDao(), zoneSpinner);
        toolSpinner(sportDataBase.borgDao(), borgSpinner);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
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
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                add();
                            }
                        });
                        break;
                    case UPDATE:
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                update();
                            }
                        });
                        break;
                }
                dismiss();
                break;
        }
    }

    public void add() {
        long exerciseId = sportDataBase.exerciseDao().getIdByName(exerciseSpinner.getSelectedItem().toString());
        long styleId = sportDataBase.styleDao().getIdByName(styleSpinner.getSelectedItem().toString());
        long tempoId = sportDataBase.tempoDao().getIdByName(tempoSpinner.getSelectedItem().toString());
        long zoneId = sportDataBase.zoneDao().getIdByName(zoneSpinner.getSelectedItem().toString());
        long borgId = sportDataBase.borgDao().getIdByName(borgSpinner.getSelectedItem().toString());
        int work = Integer.parseInt(workEdit.getText().toString());
        int rest = Integer.parseInt(restEdit.getText().toString());
        int length = Integer.parseInt(lengthEdit.getText().toString());
        int repeats = Integer.parseInt(repeatsEdit.getText().toString());
        int series = Integer.parseInt(seriesEdit.getText().toString());
        String note = noteEdit.getText().toString();
        int minutes = Integer.parseInt(timeEdit.getText().toString());
        TrainingExercise trainingExercise = new TrainingExercise(trainingId, exerciseId, styleId, tempoId, zoneId,
                borgId, work, rest, length, repeats, series, note, minutes);
        sportDataBase.trainingExerciseDao().insert(trainingExercise);
    }

    public void update() {
        long exerciseId = sportDataBase.exerciseDao().getIdByName(exerciseSpinner.getSelectedItem().toString());
        long styleId = sportDataBase.styleDao().getIdByName(styleSpinner.getSelectedItem().toString());
        long tempoId = sportDataBase.tempoDao().getIdByName(tempoSpinner.getSelectedItem().toString());
        long zoneId = sportDataBase.zoneDao().getIdByName(zoneSpinner.getSelectedItem().toString());
        long borgId = sportDataBase.borgDao().getIdByName(borgSpinner.getSelectedItem().toString());
        int work = Integer.parseInt(workEdit.getText().toString());
        int rest = Integer.parseInt(restEdit.getText().toString());
        int length = Integer.parseInt(lengthEdit.getText().toString());
        int repeats = Integer.parseInt(repeatsEdit.getText().toString());
        int series = Integer.parseInt(seriesEdit.getText().toString());
        String note = noteEdit.getText().toString();
        int minutes = Integer.parseInt(timeEdit.getText().toString());

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
