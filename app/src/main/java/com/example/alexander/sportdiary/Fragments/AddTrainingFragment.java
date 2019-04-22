package com.example.alexander.sportdiary.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.TrainingsToAims;
import com.example.alexander.sportdiary.Entities.TrainingsToEquipments;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.Utils.MultiSelectionSpinner;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
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
    private Training updateTraining = new Training();

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

        toolSpinner(sportDataBase.timeDao(), timeSpinner, sportDataBase.timeDao().getNameByIdAndUserId(updateTraining.getTimeId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.trainingPlaceDao(), trainingPlaceSpinner, sportDataBase.trainingPlaceDao().getNameByIdAndUserId(updateTraining.getPlaceId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.borgDao(), borgRatingSpinner, sportDataBase.borgDao().getNameByIdAndUserId(updateTraining.getBorgId(), MainActivity.getUserId()));
        toolMultiSpinner(sportDataBase.aimDao(), aimSpinner, this, getSelectedAims());
        toolMultiSpinner(sportDataBase.equipmentDao(), equipmentSpinner, this, getSelectedEquipments());

        return v;
    }

    private List<String> getSelectedAims() {
        if (option == EditOption.UPDATE) {
            List<Long> aimIds = sportDataBase.trainingsToAimsDao().getAimIdsByTrainingId(updateTraining.getId());
            if (aimIds.size() == 0) {
                return null;
            }
            List<String> aims = new ArrayList<>();
            for (long id : aimIds) {
                aims.add(sportDataBase.aimDao().getNameByIdAndUserId(id, MainActivity.getUserId()));
            }
            return aims;
        }
        return null;
    }

    private List<String> getSelectedEquipments() {
        if (option == EditOption.UPDATE) {
            List<Long> equipmentIds = sportDataBase.trainingsToEquipmentsDao().getEquipmentIdsByTrainingId(updateTraining.getId());
            if (equipmentIds.size() == 0) {
                return null;
            }
            List<String> equipments = new ArrayList<>();
            for (long id : equipmentIds) {
                equipments.add(sportDataBase.equipmentDao().getNameByIdAndUserId(id, MainActivity.getUserId()));
            }
            return equipments;
        }
        return null;
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

    public void update() {
        Long timeId = timeSpinner.getSelectedItem() == null ? null :
                sportDataBase.timeDao().getIdByName(timeSpinner.getSelectedItem().toString());
        Long trainingPlaceId = trainingPlaceSpinner.getSelectedItem() == null ? null
                : sportDataBase.trainingPlaceDao().getIdByName(trainingPlaceSpinner.getSelectedItem().toString());
        Long borgRatingId = borgRatingSpinner.getSelectedItem() == null ? null
                : sportDataBase.borgDao().getIdByName(borgRatingSpinner.getSelectedItem().toString());
        updateTraining.setTimeId(timeId);
        updateTraining.setPlaceId(trainingPlaceId);
        updateTraining.setBorgId(borgRatingId);
        sportDataBase.trainingDao().update(updateTraining);
        save(updateTraining);

        List<String> aimNames = aimSpinner.getSelectedStrings();
        List<String> equipmentNames = equipmentSpinner.getSelectedStrings();
        sportDataBase.trainingsToAimsDao().getByTrainingId(updateTraining.getId())
                .forEach(x -> {
                    sportDataBase.trainingsToAimsDao().delete(x);
                    MainActivity.syncDelete(x.getId(), Table.TRAININGS_TO_AIMS);
                });
        sportDataBase.trainingsToEquipmentsDao().getByTrainingId(updateTraining.getId())
                .forEach(x -> {
                    sportDataBase.trainingsToEquipmentsDao().delete(x);
                    MainActivity.syncDelete(x.getId(), Table.TRAININGS_TO_EQUIPMENTS);
                });
        for(String aimName : aimNames) {
            long aimId = sportDataBase.aimDao().getIdByName(aimName);
            TrainingsToAims toAims = new TrainingsToAims(updateTraining.getId(), aimId);
            long id = sportDataBase.trainingsToAimsDao().insert(toAims);
            toAims.setId(id);
            saveAims(toAims);
        }
        for(String equipmentName : equipmentNames) {
            long equipmentId = sportDataBase.equipmentDao().getIdByName(equipmentName);
            TrainingsToEquipments toEquipments = new TrainingsToEquipments(updateTraining.getId(), equipmentId);
            long id = sportDataBase.trainingsToEquipmentsDao().insert(toEquipments);
            toEquipments.setId(id);
            saveEquipments(toEquipments);
            }
    }

    public void add() {
        Long timeId = timeSpinner.getSelectedItem() == null ? null :
                sportDataBase.timeDao().getIdByName(timeSpinner.getSelectedItem().toString());
        Long trainingPlaceId = trainingPlaceSpinner.getSelectedItem() == null ? null
                : sportDataBase.trainingPlaceDao().getIdByName(trainingPlaceSpinner.getSelectedItem().toString());
        Long borgRatingId = borgRatingSpinner.getSelectedItem() == null ? null
                : sportDataBase.borgDao().getIdByName(borgRatingSpinner.getSelectedItem().toString());
        Training training = new Training(dayId, timeId, trainingPlaceId, borgRatingId);
        long trainingId = sportDataBase.trainingDao().insert(training);
        training.setId(trainingId);
        save(training);

        List<String> aimNames = aimSpinner.getSelectedStrings();
        List<String> equipmentNames = equipmentSpinner.getSelectedStrings();
        for(String aimName : aimNames) {
            long aimId = sportDataBase.aimDao().getIdByName(aimName);
            TrainingsToAims toAims = new TrainingsToAims(trainingId, aimId);
            long id = sportDataBase.trainingsToAimsDao().insert(toAims);
            toAims.setId(id);
            saveAims(toAims);
        }
        for(String equipmentName : equipmentNames) {
            long equipmentId = sportDataBase.equipmentDao().getIdByName(equipmentName);
            TrainingsToEquipments toEquipments = new TrainingsToEquipments(trainingId, equipmentId);
            long id = sportDataBase.trainingsToEquipmentsDao().insert(toEquipments);
            toEquipments.setId(id);
            saveEquipments(toEquipments);
        }
    }

    private void saveAims(TrainingsToAims toAims) {
        AsyncTask.execute(() -> {
                    try {
                        MainActivity.syncSave(
                                MainActivity.getObjectMapper().writeValueAsString(
                                        MainActivity.getConverter().convertEntityToDto(toAims)
                                ), Table.TRAININGS_TO_AIMS
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void saveEquipments(TrainingsToEquipments toEquipments) {
        AsyncTask.execute(() -> {
                    try {
                        MainActivity.syncSave(
                                MainActivity.getObjectMapper().writeValueAsString(
                                        MainActivity.getConverter().convertEntityToDto(toEquipments)
                                ), Table.TRAININGS_TO_EQUIPMENTS
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void save(Training training) {
        AsyncTask.execute(() -> {
                    try {
                        MainActivity.syncSave(
                                MainActivity.getObjectMapper().writeValueAsString(
                                        MainActivity.getConverter().convertEntityToDto(training)
                                ), Table.TRAINING
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
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
        this.updateTraining = training;
        return this;
    }
}
