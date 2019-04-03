package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

public class TrainingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView timeView;
    private TextView placeView;
    private TextView aimsView;
    private TextView equipmentsView;
    private TextView borgView;
    private TextView capacityView;

    public TrainingViewHolder(@NonNull View itemView) {
        super(itemView);
        timeView = itemView.findViewById(R.id.timeText);
        placeView = itemView.findViewById(R.id.placeText);
        aimsView = itemView.findViewById(R.id.aimText);
        equipmentsView = itemView.findViewById(R.id.equipmentText);
        borgView = itemView.findViewById(R.id.borgText);
        capacityView = itemView.findViewById(R.id.trainingCapacity);
        itemView.setTag(itemView);
        itemView.setOnClickListener(this);
    }

    public void setData(String time, String place, String aims, String equipments, String borg, String capacity) {
        timeView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.time), time));
        placeView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.training_place), place));
        aimsView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.aims), aims));
        equipmentsView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.equipment), equipments));
        borgView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.borg_rating), borg));
        capacityView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.capacity), capacity));

    }

    @Override
    public void onClick(View v) {

    }
}
