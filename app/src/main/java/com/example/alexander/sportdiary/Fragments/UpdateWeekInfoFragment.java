package com.example.alexander.sportdiary.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.util.Date;

import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolSpinner;

public class UpdateWeekInfoFragment extends DialogFragment implements View.OnClickListener {
    private Date start;
    private long seasonPlanId;
    private SportDataBase sportDataBase;
    private Spinner blockSpinner;
    private Spinner stageSpinner;
    private Spinner typeSpinner;
    private Spinner campSpinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_week_info, container, false);
        v.findViewById(R.id.cancelUpdateWeekInfo).setOnClickListener(this);
        v.findViewById(R.id.okUpdateWeekInfo).setOnClickListener(this);

        sportDataBase = MainActivity.getInstance().getDatabase();
        Day day = sportDataBase.dayDao().getDayByDateAndSeasonPlanId(start, seasonPlanId);

        blockSpinner = v.findViewById(R.id.weekBlockSpinner);
        stageSpinner = v.findViewById(R.id.weekStageSpinner);
        typeSpinner = v.findViewById(R.id.weekTypeSpinner);
        campSpinner = v.findViewById(R.id.weekCampSpinner);

        toolSpinner(sportDataBase.blockDao(), blockSpinner, sportDataBase.blockDao().getNameByIdAndUserId(day.getBlockId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.stageDao(), stageSpinner, sportDataBase.stageDao().getNameByIdAndUserId(day.getStageId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.typeDao(), typeSpinner, sportDataBase.typeDao().getNameByIdAndUserId(day.getTypeId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.campDao(), campSpinner, sportDataBase.campDao().getNameByIdAndUserId(day.getCampId(), MainActivity.getUserId()));

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelUpdateWeekInfo:
                dismiss();
                break;
            case R.id.okUpdateWeekInfo:
                AsyncTask.execute(this::update);
                dismiss();
                break;
        }
    }

    public void update() {
        Date date = start;
        for (int i = 0; i < 7; i++) {
            Day day = sportDataBase.dayDao().getDayByDateAndSeasonPlanId(date, seasonPlanId);
            day.setBlockId(blockSpinner.getSelectedItem() == null ? null
                    : sportDataBase.blockDao().getIdByName(blockSpinner.getSelectedItem().toString()));
            day.setStageId(stageSpinner.getSelectedItem() == null ? null
                    : sportDataBase.stageDao().getIdByName(stageSpinner.getSelectedItem().toString()));
            day.setTypeId(typeSpinner.getSelectedItem() == null ? null
                    : sportDataBase.typeDao().getIdByName(typeSpinner.getSelectedItem().toString()));
            day.setCampId(campSpinner.getSelectedItem() == null ? null
                    : sportDataBase.campDao().getIdByName(campSpinner.getSelectedItem().toString()));
            sportDataBase.dayDao().update(day);
            date = DateUtil.addDays(date, 1);
        }
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setSeasonPlanId(long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
    }
}
