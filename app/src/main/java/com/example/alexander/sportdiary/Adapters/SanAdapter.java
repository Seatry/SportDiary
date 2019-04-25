package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.alexander.sportdiary.Entities.SanAnswer;
import com.example.alexander.sportdiary.Entities.SanQuestion;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.ViewHolders.SanViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SanAdapter extends RecyclerView.Adapter<SanViewHolder> {
    private static int countItems;
    private long dayId;
    private SportDataBase sportDataBase = MainActivity.getDatabase();
    private List<SanQuestion> questions = new ArrayList<>();
    private HashMap<Long, String> answerToQuestion= new HashMap<>();

    @NonNull
    @Override
    public SanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.san_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        SanViewHolder viewHolder = new SanViewHolder(view);
        if(countItems < questions.size()) {
            SanQuestion question = questions.get(countItems);
            SanAnswer answer = sportDataBase.sanAnswerDao().getByDayAndQuestion(dayId, question.getId());
            viewHolder.setData(question.getPositive(), question.getNegative(), answer == null ? null : answer.getAnswer());
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SanViewHolder sanViewHolder, int i) {
        final SanQuestion question = questions.get(i);
        SanAnswer answer = sportDataBase.sanAnswerDao().getByDayAndQuestion(dayId, question.getId());
        sanViewHolder.setData(question.getPositive(), question.getNegative(), answer == null ? null : answer.getAnswer());
        sanViewHolder.getAnswerSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem() != null) {
                        answerToQuestion.put(question.getId(), parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public HashMap<Long, String> getAnswerToQuestion() {
        return answerToQuestion;
    }

    public void setQuestions(List<SanQuestion> questions) {
        this.questions = questions;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }
}
