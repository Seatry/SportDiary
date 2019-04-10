package com.example.alexander.sportdiary.ViewHolders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.Utils.ToolerOfSpinners;

public class DreamViewHolder extends RecyclerView.ViewHolder {
    private TextView questionView;
    private Spinner answerSpinner;

    public DreamViewHolder(@NonNull View itemView) {
        super(itemView);
        questionView = itemView.findViewById(R.id.dream_question);
        answerSpinner = itemView.findViewById(R.id.dream_answer);
        itemView.setTag(itemView);
    }

    public void setData(String question, @Nullable Integer answer) {
        questionView.setText(question);
        ToolerOfSpinners.toolDreamAnswer(answerSpinner, answer);
    }

    public Spinner getAnswerSpinner() {
        return answerSpinner;
    }
}
