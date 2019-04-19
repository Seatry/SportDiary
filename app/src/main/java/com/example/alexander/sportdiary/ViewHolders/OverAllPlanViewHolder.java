package com.example.alexander.sportdiary.ViewHolders;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alexander.sportdiary.Fragments.WeekInfoActivity;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

public class OverAllPlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView weekText;
    private long seasonPlanId;

    public OverAllPlanViewHolder(@NonNull View itemView) {
        super(itemView);
        weekText = itemView.findViewById(R.id.week_number);
        itemView.setTag(itemView);
        itemView.setOnClickListener(this);
    }

    public void setData(String weekNumber) {
        weekText.setText(String.format("%s ", weekNumber));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.getInstance(), WeekInfoActivity.class);
        intent.putExtra("seasonPlanId", seasonPlanId);
        intent.putExtra("title", MainActivity.getInstance().getString(R.string.info_for)
                + " " + weekText.getText() + " " + MainActivity.getInstance().getString(R.string.for_week));
        intent.putExtra("week", Integer.valueOf(weekText.getText().toString().replace(" ", "")));
        MainActivity.getInstance().startActivity(intent);
    }

    public void setSeasonPlanId(long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
    }
}
