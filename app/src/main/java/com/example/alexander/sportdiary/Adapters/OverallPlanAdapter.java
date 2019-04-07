package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.ViewHolders.OverAllPlanViewHolder;

import java.util.ArrayList;
import java.util.List;

public class OverallPlanAdapter extends RecyclerView.Adapter<OverAllPlanViewHolder> {
    private List<String> weeks = new ArrayList<>();
    private static int countItems;
    private long seasonPlanId;

    @NonNull
    @Override
    public OverAllPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.plan_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        OverAllPlanViewHolder viewHolder = new OverAllPlanViewHolder(view);
        if(countItems < weeks.size()) {
            viewHolder.setData(weeks.get(countItems));
            viewHolder.setSeasonPlanId(seasonPlanId);
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OverAllPlanViewHolder overAllPlanViewHolder, int i) {
       overAllPlanViewHolder.setData(weeks.get(i));
       overAllPlanViewHolder.setSeasonPlanId(seasonPlanId);
    }

    @Override
    public int getItemCount() {
        return weeks.size();
    }

    public void setWeeks(List<String> weeks) {
        this.weeks = weeks;
    }

    public void setSeasonPlanId(long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
    }
}
