package com.example.alexander.sportdiary.Fragments;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Adapters.HeartRateAdapter;
import com.example.alexander.sportdiary.Entities.HeartRate;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;

import java.util.List;

public class HeartRateFragment extends DialogFragment implements View.OnClickListener {
    private long exerciseId;
    private HeartRateAdapter adapter;
    private SportDataBase sportDataBase;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_heart_rate, container, false);
        v.findViewById(R.id.cancelHr).setOnClickListener(this);
        v.findViewById(R.id.okHr).setOnClickListener(this);

        sportDataBase = MainActivity.getInstance().getDatabase();

        RecyclerView recyclerView = v.findViewById(R.id.hrItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HeartRateAdapter();
        recyclerView.setAdapter(adapter);

        List<HeartRate> heartRates = sportDataBase.heartRateDao().getAllByExerciseId(exerciseId);
        adapter.setData(heartRates);
        adapter.notifyDataSetChanged();

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
            case R.id.cancelHr:
                dismiss();
                break;
            case R.id.okHr:
                AsyncTask.execute(this::update);
                dismiss();
        }
    }

    public void update() {
        List<HeartRate> heartRates = adapter.getHeartRates();
        double hr = 0;
        for (HeartRate heartRate : heartRates) {
            hr += heartRate.getHr();
            sportDataBase.heartRateDao().update(heartRate);
        }
        if (heartRates.size() > 0) {
            hr /= heartRates.size();
            sportDataBase.trainingExerciseDao().updateHrById(hr, exerciseId);
        }
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }
}
