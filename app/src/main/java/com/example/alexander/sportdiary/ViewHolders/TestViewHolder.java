package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alexander.sportdiary.R;

public class TestViewHolder extends RecyclerView.ViewHolder {
    private TextView testView;

    public TestViewHolder(@NonNull View itemView) {
        super(itemView);
        testView = itemView.findViewById(R.id.test_name);
        itemView.setTag(itemView);
    }

    public void setData(String test) {
        testView.setText(test);
    }
}
