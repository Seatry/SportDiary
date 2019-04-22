package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.Fragments.AddTrainingFragment;
import com.example.alexander.sportdiary.Fragments.TrainingExerciseActivity;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.ViewHolders.TrainingViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingViewHolder> {
    private static int countItems;
    private SportDataBase sportDataBase = MainActivity.getInstance().getDatabase();
    private List<Training> trainings = new ArrayList<>();

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.training_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrainingViewHolder viewHolder = new TrainingViewHolder(view);
        if(countItems < trainings.size()) {
            Training training = trainings.get(countItems);
            setViewHolderData(training, viewHolder);
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrainingViewHolder trainingViewHolder, final int i) {
        setViewHolderData(trainings.get(i), trainingViewHolder);
        trainingViewHolder.itemView.findViewById(R.id.training_opts).setOnClickListener(view -> {
            //creating a popup menu
            PopupMenu popup = new PopupMenu(MainActivity.getInstance(), trainingViewHolder.itemView);
            //inflating menu from xml resource
            popup.inflate(R.menu.training_opts);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.trainingUpdate:
                        AddTrainingFragment addTrainingFragment = new AddTrainingFragment();
                        addTrainingFragment
                                .setTitle(MainActivity.getInstance().getString(R.string.updateTraining))
                                .setUpdateItem(trainings.get(i))
                                .setOption(EditOption.UPDATE);
                        addTrainingFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "updateTraining");
                        break;
                    case R.id.trainingDelete:
                        AsyncTask.execute(() -> sportDataBase.trainingDao().delete(trainings.get(i)));
                        MainActivity.syncDelete(trainings.get(i).getId(), Table.TRAINING);
                        break;
                    case R.id.trainingExercises:
                        Intent intent = new Intent(MainActivity.getInstance(), TrainingExerciseActivity.class);
                        intent.putExtra("trainingId", (int) trainings.get(i).getId());
                        MainActivity.getInstance().startActivity(intent);
                        break;
                }
                return false;
            });
            //displaying the popup
            popup.setGravity(Gravity.END);
            popup.show();
        });
    }

    private void setViewHolderData(Training training, TrainingViewHolder viewHolder) {
        String time = training.getTimeId() == null ? "" : sportDataBase.timeDao().getNameByIdAndUserId(training.getTimeId(), MainActivity.getUserId());
        String place = training.getPlaceId() == null ? "" : sportDataBase.trainingPlaceDao().getNameByIdAndUserId(training.getPlaceId(), MainActivity.getUserId());
        StringBuilder aims = new StringBuilder();
        for(long aimId : sportDataBase.trainingsToAimsDao().getAimIdsByTrainingId(training.getId())) {
            aims.append(sportDataBase.aimDao().getNameByIdAndUserId(aimId, MainActivity.getUserId())).append(", ");
        }
        if(aims.length() > 1) {
            aims.deleteCharAt(aims.length() - 1);
            aims.deleteCharAt(aims.length() - 1);
        }
        StringBuilder equipments = new StringBuilder();
        for(long eqId : sportDataBase.trainingsToEquipmentsDao().getEquipmentIdsByTrainingId(training.getId())) {
            equipments.append(sportDataBase.equipmentDao().getNameByIdAndUserId(eqId, MainActivity.getUserId())).append(", ");
        }
        if(equipments.length() > 1) {
            equipments.deleteCharAt(equipments.length()-1);
            equipments.deleteCharAt(equipments.length()-1);
        }
        String borgs = training.getBorgId() == null ? "" : sportDataBase.borgDao().getNameByIdAndUserId(training.getBorgId(), MainActivity.getUserId());
        viewHolder.setData(time, place, aims.toString(), equipments.toString(), borgs, String.valueOf(training.getCapacity()));
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }
}
