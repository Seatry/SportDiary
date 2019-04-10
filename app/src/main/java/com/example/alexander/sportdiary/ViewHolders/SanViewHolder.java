package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.Utils.ToolerOfSpinners;

public class SanViewHolder extends RecyclerView.ViewHolder {
    private TextView positiveView;
    private TextView negativeView;

    private Spinner answerSpinner;

    public SanViewHolder(@NonNull View itemView) {
        super(itemView);
        positiveView = itemView.findViewById(R.id.positive_question);
        negativeView = itemView.findViewById(R.id.negative_question);
        answerSpinner = itemView.findViewById(R.id.san_answer);
        itemView.setTag(itemView);
    }

    public void setData(String positive, String negative, @Nullable Integer answer) {
        positiveView.setText(positive);
        negativeView.setText(negative);
        ToolerOfSpinners.toolSanAnswer(answerSpinner, answer);
    }

    public Spinner getAnswerSpinner() {
        return answerSpinner;
    }
}
