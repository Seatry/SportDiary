package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

public class RestViewHolder extends RecyclerView.ViewHolder {
    private TextView placeView;
    private TextView timeView;

    public RestViewHolder(@NonNull View itemView) {
        super(itemView);
        placeView = itemView.findViewById(R.id.restPlaceText);
        timeView = itemView.findViewById(R.id.restTimeText);
        itemView.setTag(itemView);
    }

    public void setData(String place, String time) {
        placeView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.rest_place), place));
        timeView.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.time), time));
    }
}
