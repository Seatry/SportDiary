package com.example.alexander.sportdiary.Fragments;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.EditOption;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.TrainingsToAims;
import com.example.alexander.sportdiary.Entities.TrainingsToEquipments;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.Utils.MultiSelectionSpinner;

import java.util.List;

import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolMultiSpinner;
import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolSpinner;

public class AddTrainingFragment extends DialogFragment implements View.OnClickListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    private Spinner timeSpinner;
    private Spinner trainingPlaceSpinner;
    private Spinner borgRatingSpinner;
    private MultiSelectionSpinner aimSpinner;
    private MultiSelectionSpinner equipmentSpinner;
    private SportDataBase sportDataBase;
    private long dayId;
    private String title;
    private EditOption option;
    private Training training;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_training, container, false);
        v.findViewById(R.id.cancelAddTraining).setOnClickListener(this);
        v.findViewById(R.id.okAddTraining).setOnClickListener(this);
        ((TextView)v.findViewById(R.id.trainingTitle)).setText(title);

        sportDataBase= MainActivity.getInstance().getDatabase();
        timeSpinner = v.findViewById(R.id.timeSpinner);
        trainingPlaceSpinner = v.findViewById(R.id.trainingPlaceSpinner);
        borgRatingSpinner = v.findViewById(R.id.borgRatingSpinner);
        aimSpinner = v.findViewById(R.id.aimSpinner);
        equipmentSpinner = v.findViewById(R.id.equipmentSpinner);

        toolSpinner(sportDataBase.timeDao(), timeSpinner);
        toolSpinner(sportDataBase.trainingPlaceDao(), trainingPlaceSpinner);
        toolSpinner(sportDataBase.borgDao(), borgRatingSpinner);
        toolMultiSpinner(sportDataBase.aimDao(), aimSpinner, this);
        toolMultiSpinner(sportDataBase.equipmentDao(), equipmentSpinner, this);

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
            case R.id.cancelAddTraining:
                dismiss();
                break;
            case R.id.okAddTraining:
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

    public void update() {
        long timeId = sportDataBase.timeDao().getIdByName(timeSpinner.getSelectedItem().toString());
        long trainingPlaceId = sportDataBase.trainingPlaceDao().getIdByName(trainingPlaceSpinner.getSelectedItem().toString());
        long borgRatingId = sportDataBase.borgDao().getIdByName(borgRatingSpinner.getSelectedItem().toString());
        training.setTimeId(timeId);
        training.setPlaceId(trainingPlaceId);
        training.setBorgId(borgRatingId);
        sportDataBase.trainingDao().update(training);

        List<String> aimNames = aimSpinner.getSelectedStrings();
        List<String> equipmentNames = equipmentSpinner.getSelectedStrings();
        sportDataBase.trainingsToAimsDao().deleteByTrainingId(training.getId());
        sportDataBase.trainingsToEquipmentsDao().deleteByTrainingId(training.getId());
        for(String aimName : aimNames) {
            long aimId = sportDataBase.aimDao().getIdByName(aimName);
            sportDataBase.trainingsToAimsDao().insert(new TrainingsToAims(training.getId(), aimId));
        }
        for(String equipmentName : equipmentNames) {
            long equipmentId = sportDataBase.equipmentDao().getIdByName(equipmentName);
            sportDataBase.trainingsToEquipmentsDao().insert(new TrainingsToEquipments(training.getId(), equipmentId));
        }
    }

    public void add() {
        long timeId = sportDataBase.timeDao().getIdByName(timeSpinner.getSelectedItem().toString());
        long trainingPlaceId = sportDataBase.trainingPlaceDao().getIdByName(trainingPlaceSpinner.getSelectedItem().toString());
        long borgRatingId = sportDataBase.borgDao().getIdByName(borgRatingSpinner.getSelectedItem().toString());
        Training training = new Training(dayId, timeId, trainingPlaceId, borgRatingId);
        long trainingId = sportDataBase.trainingDao().insert(training);

        List<String> aimNames = aimSpinner.getSelectedStrings();
        List<String> equipmentNames = equipmentSpinner.getSelectedStrings();
        for(String aimName : aimNames) {
            long aimId = sportDataBase.aimDao().getIdByName(aimName);
            sportDataBase.trainingsToAimsDao().insert(new TrainingsToAims(trainingId, aimId));
        }
        for(String equipmentName : equipmentNames) {
            long equipmentId = sportDataBase.equipmentDao().getIdByName(equipmentName);
            sportDataBase.trainingsToEquipmentsDao().insert(new TrainingsToEquipments(trainingId, equipmentId));
        }
    }

    public AddTrainingFragment setDayId(long dayId) {
        this.dayId = dayId;
        return this;
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

    public AddTrainingFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public AddTrainingFragment setOption(EditOption option) {
        this.option = option;
        return this;
    }

    public AddTrainingFragment setUpdateItem(Training training) {
        this.training = training;
        return this;
    }
}
