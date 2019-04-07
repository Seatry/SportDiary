package com.example.alexander.sportdiary.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Adapters.OverallPlanAdapter;
import com.example.alexander.sportdiary.R;

import java.util.ArrayList;
import java.util.List;

public class OverallPlanFragment extends DialogFragment implements View.OnClickListener {
    private long seasonPlanId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overall_plan, container, false);
        v.findViewById(R.id.cancelPlan).setOnClickListener(this);

        RecyclerView recyclerView = v.findViewById(R.id.plan_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final OverallPlanAdapter adapter = new OverallPlanAdapter();
        recyclerView.setAdapter(adapter);

        List<String> weeks = new ArrayList<>();
        for(int i = 0; i < 52; i++) {
            weeks.add(String.valueOf(i+1));
        }
        adapter.setWeeks(weeks);
        adapter.setSeasonPlanId(seasonPlanId);
        adapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelPlan:
                dismiss();
                break;
        }
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

    public void setSeasonPlanId(long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
    }
}
