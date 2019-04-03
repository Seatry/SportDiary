package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

public class TrainingExerciseViewHolder extends RecyclerView.ViewHolder {
    private TextView exerciseView;
    private TextView styleView;
    private TextView tempoView;
    private TextView zoneView;
    private TextView workToRestView;
    private TextView capacityView;
    private TextView timeView;
    private TextView borgView;
    private TextView noteView;

    public TrainingExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        exerciseView = itemView.findViewById(R.id.exerciseText);
        styleView = itemView.findViewById(R.id.styleText);
        tempoView = itemView.findViewById(R.id.tempoText);
        zoneView = itemView.findViewById(R.id.zoneText);
        workToRestView = itemView.findViewById(R.id.workToRestText);
        capacityView = itemView.findViewById(R.id.trainingExerciseCapacityText);
        timeView = itemView.findViewById(R.id.timeExerciseText);
        borgView = itemView.findViewById(R.id.borgExerciseText);
        noteView = itemView.findViewById(R.id.noteText);
        itemView.setTag(itemView);
    }

    public void setData(String exercise, String style, String tempo, String zone,
                        String workToRest, String capacity, String time, String borg, String note) {
        exerciseView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.exercises_to_training), exercise));
        styleView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.style), style));
        tempoView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.tempo), tempo));
        zoneView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.zone), zone));
        workToRestView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.work_rest), workToRest));
        capacityView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.capacity), capacity));
        timeView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.time_min), time));
        borgView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.borg_rating), borg));
        noteView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.note), note));
    }
}
