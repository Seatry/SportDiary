package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alexander.sportdiary.R;

public class EditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView editName;

    public EditViewHolder(@NonNull View itemView) {
        super(itemView);
        editName = itemView.findViewById(R.id.edit_name);
        itemView.setTag(itemView);
        itemView.setOnClickListener(this);
    }
    public void setData(String data) {
        editName.setText(data);
    }

    @Override
    public void onClick(View v) {

    }
}
