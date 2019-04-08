package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alexander.sportdiary.Adapters.HeartRateAdapter;
import com.example.alexander.sportdiary.R;

public class HeartRateViewHolder extends RecyclerView.ViewHolder {
    private TextView seriesText;
    private TextView repeatText;
    private TextView timeText;
    private EditText hrText;
    private HeartRateAdapter.MyCustomEditTextListener myCustomEditTextListener;

    public HeartRateViewHolder(@NonNull View itemView, HeartRateAdapter.MyCustomEditTextListener myCustomEditTextListener) {
        super(itemView);
        seriesText = itemView.findViewById(R.id.seriesNumber);
        repeatText = itemView.findViewById(R.id.repeatNumber);
        timeText = itemView.findViewById(R.id.hrTimeText);
        hrText = itemView.findViewById(R.id.editPulse);
        this.myCustomEditTextListener = myCustomEditTextListener;
        this.hrText.addTextChangedListener(myCustomEditTextListener);
        itemView.setTag(itemView);
    }

    public void setData(int series, int repeat, String time, int hr) {
        seriesText.setText(String.format(" %s\t", String.valueOf(series)));
        repeatText.setText(String.format(" %s\t", String.valueOf(repeat)));
        timeText.setText(time);
        hrText.setText(String.valueOf(hr));
    }

    public HeartRateAdapter.MyCustomEditTextListener getMyCustomEditTextListener() {
        return myCustomEditTextListener;
    }
}
