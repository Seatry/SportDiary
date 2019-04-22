package com.example.alexander.sportdiary.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Adapters.DreamAdapter;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.DreamAnswer;
import com.example.alexander.sportdiary.Entities.DreamQuestion;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.List;

public class DreamFragment extends DialogFragment implements View.OnClickListener {
    private long dayId;
    private SportDataBase sportDataBase;
    private DreamAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dream, container, false);
        v.findViewById(R.id.cancelDream).setOnClickListener(this);
        v.findViewById(R.id.okDream).setOnClickListener(this);

        sportDataBase = MainActivity.getInstance().getDatabase();

        RecyclerView recyclerView = v.findViewById(R.id.dream_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DreamAdapter();
        adapter.setDayId(dayId);
        recyclerView.setAdapter(adapter);

        List<DreamQuestion> questions = sportDataBase.dreamQuestionDao().getAll();
        adapter.setQuestions(questions);
        adapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelDream:
                dismiss();
                break;
            case R.id.okDream:
                AsyncTask.execute(this::update);
                dismiss();
                break;
        }
    }

    public void update() {
        HashMap<Long, String> answerToQuestion = adapter.getAnswerToQuestion();
        for (Long questionId : answerToQuestion.keySet()) {
            DreamAnswer dreamAnswer = sportDataBase.dreamAnswerDao().getByDayAndQuestion(dayId, questionId);
            String answer = answerToQuestion.get(questionId);
            if (answer.equals("-")) {
                if (dreamAnswer != null) {
                    sportDataBase.dreamAnswerDao().delete(dreamAnswer);
                    MainActivity.syncDelete(dreamAnswer.getId(), Table.DREAM_ANSWER);
                }
                continue;
            }
            int answerInt = answer.equals(MainActivity.getInstance().getString(R.string.yes)) ? 1 : 0;
            if (dreamAnswer == null) {
                dreamAnswer = new DreamAnswer(dayId, questionId, answerInt);
                long id = sportDataBase.dreamAnswerDao().insert(dreamAnswer);
                dreamAnswer.setId(id);
            } else {
                dreamAnswer.setAnswer(answerInt);
                sportDataBase.dreamAnswerDao().update(dreamAnswer);
            }
            save(dreamAnswer);
        }
        List<DreamAnswer> dreamAnswers = sportDataBase.dreamAnswerDao().getAllByDayWhereAnswerIsYes(dayId);
        double dream = dreamAnswers.size() * 6.7;
        Day day = sportDataBase.dayDao().getById(dayId);
        day.setDream(dream);
        sportDataBase.dayDao().update(day);
        saveDay(day);
    }

    private void saveDay(Day day) {
        AsyncTask.execute(() -> {
            try {
                MainActivity.syncSave(
                        MainActivity.getObjectMapper().writeValueAsString(
                                MainActivity.getConverter().convertEntityToDto(day)
                        ), Table.DAY
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    private void save(DreamAnswer dreamAnswer) {
        AsyncTask.execute(() -> {
                    try {
                        MainActivity.syncSave(
                                MainActivity.getObjectMapper().writeValueAsString(
                                        MainActivity.getConverter().convertEntityToDto(dreamAnswer)
                                ), Table.DREAM_ANSWER
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }
}
