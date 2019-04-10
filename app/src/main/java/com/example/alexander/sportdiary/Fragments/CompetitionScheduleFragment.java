package com.example.alexander.sportdiary.Fragments;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Adapters.CompetitionScheduleAdapter;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

import java.util.List;

public class CompetitionScheduleFragment extends DialogFragment implements View.OnClickListener  {
    private long seasonPlanId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_competition_schedule, container, false);
        v.findViewById(R.id.cancelCompetitionButton).setOnClickListener(this);
        v.findViewById(R.id.add_competition_button).setOnClickListener(this);

        RecyclerView recyclerView = v.findViewById(R.id.competition_schedule_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final CompetitionScheduleAdapter adapter = new CompetitionScheduleAdapter();
        recyclerView.setAdapter(adapter);

        LiveData<List<Day>> liveData = MainActivity.getInstance().getDatabase()
                .dayDao().getAllDaysBySeasonPlanIdWhereCompetitionToImportanceIsNotNull(seasonPlanId);
        liveData.observe(this, new Observer<List<Day>>() {
            @Override
            public void onChanged(@Nullable List<Day> days) {
                adapter.setData(days);
                adapter.notifyDataSetChanged();
            }
        });

        return v;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelCompetitionButton:
                dismiss();
                break;
            case R.id.add_competition_button:
                AddCompetitionScheduleFragment scheduleFragment = new AddCompetitionScheduleFragment();
                scheduleFragment
                        .setOption(EditOption.INSERT)
                        .setTitle(getString(R.string.addCompetition))
                        .setSeasonPlanId(seasonPlanId);
                scheduleFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addSchedule");
                break;
        }
    }

    public void setSeasonPlanId(Long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
    }
}
