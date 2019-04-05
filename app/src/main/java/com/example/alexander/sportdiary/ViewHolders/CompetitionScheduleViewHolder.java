package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alexander.sportdiary.R;

public class CompetitionScheduleViewHolder extends RecyclerView.ViewHolder {
    private TextView competitionName;

    public CompetitionScheduleViewHolder(@NonNull View itemView) {
        super(itemView);
        competitionName = itemView.findViewById(R.id.competition_name);
        itemView.setTag(itemView);
    }

    public void setData(String data) {
        competitionName.setText(data);
    }
}
