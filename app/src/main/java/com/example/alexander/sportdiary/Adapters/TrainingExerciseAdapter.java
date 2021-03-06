package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.Fragments.AddTrainingExerciseFragment;
import com.example.alexander.sportdiary.Fragments.HeartRateFragment;
import com.example.alexander.sportdiary.Fragments.TrainingExerciseActivity;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.ViewHolders.TrainingExerciseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TrainingExerciseAdapter extends RecyclerView.Adapter<TrainingExerciseViewHolder> {
    private static int countItems;
    private SportDataBase sportDataBase = MainActivity.getDatabase();
    private List<TrainingExercise> trainingExercises = new ArrayList<>();

    @NonNull
    @Override
    public TrainingExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.training_exercise_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrainingExerciseViewHolder viewHolder = new TrainingExerciseViewHolder(view);
        if(countItems < trainingExercises.size()) {
            TrainingExercise trainingExercise = trainingExercises.get(countItems);
            setViewHolderData(trainingExercise, viewHolder);
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrainingExerciseViewHolder trainingExerciseViewHolder, final int i) {
        setViewHolderData(trainingExercises.get(i), trainingExerciseViewHolder);
        trainingExerciseViewHolder.itemView.findViewById(R.id.training_exercise_opts).setOnClickListener(view -> {
            //creating a popup menu
            PopupMenu popup = new PopupMenu(MainActivity.getInstance(), trainingExerciseViewHolder.itemView);
            //inflating menu from xml resource
            popup.inflate(R.menu.exercise_opts);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.exerciseUpdate:
                        AddTrainingExerciseFragment addTrainingExerciseFragment = new AddTrainingExerciseFragment();
                        addTrainingExerciseFragment
                                .setTrainingId(trainingExercises.get(i).getTrainingId())
                                .setOption(EditOption.UPDATE)
                                .setTitle(MainActivity.getInstance().getString(R.string.updateExerciseToTraining))
                                .setUpdateTrainingExercise(trainingExercises.get(i));
                        addTrainingExerciseFragment.show(TrainingExerciseActivity.getInstance().getSupportFragmentManager(), "updateTrainingExercise");
                        break;
                    case R.id.exerciseDelete:
                        AsyncTask.execute(() -> sportDataBase.trainingExerciseDao().delete(trainingExercises.get(i)));
                        MainActivity.syncDelete(trainingExercises.get(i).getId(), Table.TRAINING_EXERCISE);
                        break;
                    case R.id.heartRates:
                        HeartRateFragment heartRateFragment = new HeartRateFragment();
                        heartRateFragment.setExerciseId(trainingExercises.get(i).getId());
                        heartRateFragment.show(TrainingExerciseActivity.getInstance().getSupportFragmentManager(), "heartRates");
                        break;
                }
                return false;
            });
            //displaying the popup
            popup.setGravity(Gravity.END);
            popup.show();
        });
    }

    private void setViewHolderData(TrainingExercise trainingExercise, TrainingExerciseViewHolder viewHolder) {
        String exercise = trainingExercise.getExerciseId() == null ? "" : sportDataBase.exerciseDao().getNameByIdAndUserId(trainingExercise.getExerciseId(), MainActivity.getUserId());
        String style = trainingExercise.getStyleId() == null ? "" : sportDataBase.styleDao().getNameByIdAndUserId(trainingExercise.getStyleId(), MainActivity.getUserId());
        String tempo = trainingExercise.getTempoId() == null ? "" : sportDataBase.tempoDao().getNameByIdAndUserId(trainingExercise.getTempoId(), MainActivity.getUserId());
        String zone = trainingExercise.getZoneId() == null ? "" : sportDataBase.zoneDao().getNameByIdAndUserId(trainingExercise.getZoneId(), MainActivity.getUserId());
        int work = trainingExercise.getWork();
        int rest = trainingExercise.getRest();
        String workToRest = String.valueOf(work) + ":" + String.valueOf(rest);
        int series = trainingExercise.getSeries();
        int repeats = trainingExercise.getRepeats();
        int length = trainingExercise.getLength();
        String capacity = String.valueOf(series) + "x" + String.valueOf(repeats) + "x" + String.valueOf(length);
        String time = String.valueOf(trainingExercise.getMinutes());
        String borg = trainingExercise.getBorgId() == null ? "" : sportDataBase.borgDao().getNameByIdAndUserId(trainingExercise.getBorgId(), MainActivity.getUserId());
        String note = trainingExercise.getNote();
        viewHolder.setData(exercise, style, tempo, zone, workToRest, capacity, time, borg, note);
    }

    @Override
    public int getItemCount() {
        return  trainingExercises.size();
    }

    public void setTrainingExercises(List<TrainingExercise> trainingExercises) {
        this.trainingExercises = trainingExercises;
    }
}
