package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Entities.HeartRate;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.ViewHolders.HeartRateViewHolder;

import java.util.ArrayList;
import java.util.List;

public class HeartRateAdapter extends RecyclerView.Adapter<HeartRateViewHolder> {
    private int countItems;
    private List<HeartRate> heartRates = new ArrayList<>();

    @NonNull
    @Override
    public HeartRateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.heart_rate_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        HeartRateViewHolder viewHolder = new HeartRateViewHolder(view, new MyCustomEditTextListener());
        if(countItems < heartRates.size()) {
            HeartRate heartRate = heartRates.get(countItems);
            viewHolder.setData(heartRate.getSeries(), heartRate.getRepeat(), heartRate.getTime(), heartRate.getHr());
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HeartRateViewHolder heartRateViewHolder, int i) {
        heartRateViewHolder.getMyCustomEditTextListener().updatePosition(i);
        HeartRate heartRate = heartRates.get(i);
        heartRateViewHolder.setData(heartRate.getSeries(), heartRate.getRepeat(), heartRate.getTime(), heartRate.getHr());
    }

    @Override
    public int getItemCount() {
        return heartRates.size();
    }

    public void setData(List<HeartRate> data) {
        this.heartRates = data;
    }

    public List<HeartRate> getHeartRates() {
        return heartRates;
    }

    public class MyCustomEditTextListener implements TextWatcher {
        private int position = -1;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if(charSequence.length() > 0 && position != -1) {
                Log.d("HR", "ON CHANGED " + position + " " + Integer.valueOf(charSequence.toString()));
                heartRates.get(position).setHr(Integer.valueOf(charSequence.toString()));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
