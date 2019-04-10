package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.alexander.sportdiary.Entities.DreamAnswer;
import com.example.alexander.sportdiary.Entities.DreamQuestion;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.ViewHolders.DreamViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DreamAdapter extends RecyclerView.Adapter<DreamViewHolder> {
    private static int countItems;
    private long dayId;
    private SportDataBase sportDataBase = MainActivity.getInstance().getDatabase();
    private List<DreamQuestion> questions = new ArrayList<>();
    private HashMap<Long, String> answerToQuestion= new HashMap<>();

    @NonNull
    @Override
    public DreamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.dream_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        DreamViewHolder viewHolder = new DreamViewHolder(view);
        if(countItems < questions.size()) {
            DreamQuestion question = questions.get(countItems);
            DreamAnswer answer = sportDataBase.dreamAnswerDao().getByDayAndQuestion(dayId, question.getId());
            viewHolder.setData(question.getQuestion(), answer == null ? null : answer.getAnswer());
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DreamViewHolder dreamViewHolder, final int i) {
        final DreamQuestion question = questions.get(i);
        DreamAnswer answer = sportDataBase.dreamAnswerDao().getByDayAndQuestion(dayId, question.getId());
        dreamViewHolder.setData(question.getQuestion(), answer == null ? null : answer.getAnswer());
        dreamViewHolder.getAnswerSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    public void setQuestions(List<DreamQuestion> questions) {
        this.questions = questions;
    }

    public List<DreamQuestion> getQuestions() {
        return questions;
    }

    public HashMap<Long, String> getAnswerToQuestion() {
        return answerToQuestion;
    }
}
