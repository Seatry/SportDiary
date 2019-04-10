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
import com.example.alexander.sportdiary.Adapters.SanAdapter;
import com.example.alexander.sportdiary.Entities.DreamQuestion;
import com.example.alexander.sportdiary.Entities.SanAnswer;
import com.example.alexander.sportdiary.Entities.SanQuestion;
import com.example.alexander.sportdiary.Enums.SanType;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;

import java.util.HashMap;
import java.util.List;

public class SanFragment extends DialogFragment implements View.OnClickListener {
    private long dayId;
    private SportDataBase sportDataBase;
    private RecyclerView recyclerView;
    private SanAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_san, container, false);
        v.findViewById(R.id.cancelSan).setOnClickListener(this);
        v.findViewById(R.id.okSan).setOnClickListener(this);

        sportDataBase = MainActivity.getInstance().getDatabase();

        recyclerView = v.findViewById(R.id.san_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SanAdapter();
        adapter.setDayId(dayId);
        recyclerView.setAdapter(adapter);

        List<SanQuestion> questions = sportDataBase.sanQuestionDao().getAll();
        adapter.setQuestions(questions);
        adapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelSan:
                dismiss();
                break;
            case R.id.okSan:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
                dismiss();
                break;
        }
    }

    public void update() {
        HashMap<Long, String> answerToQuestion = adapter.getAnswerToQuestion();
        for (Long questionId : answerToQuestion.keySet()) {
            SanAnswer sanAnswer = sportDataBase.sanAnswerDao().getByDayAndQuestion(dayId, questionId);
            String answer = answerToQuestion.get(questionId);
            if (answer.equals("-")) {
                if (sanAnswer != null) {
                    sportDataBase.sanAnswerDao().delete(sanAnswer);
                }
                continue;
            }
            int answerInt = Integer.valueOf(answer);
            if (sanAnswer == null) {
                sportDataBase.sanAnswerDao().insert(new SanAnswer(dayId, questionId, answerInt));
            } else {
                sanAnswer.setAnswer(answerInt);
                sportDataBase.sanAnswerDao().update(sanAnswer);
            }
        }
        List<SanAnswer> answers = sportDataBase.sanAnswerDao().getAllByDayId(dayId);
        int health = 0, activity = 0, mood = 0;
        for (SanAnswer answer : answers) {
            SanQuestion question = sportDataBase.sanQuestionDao().getById(answer.getQuestionId());
            switch (question.getType()) {
                case HEALTH:
                    health += answer.getAnswer() + 4;
                    break;
                case ACTIVITY:
                    activity += answer.getAnswer() + 4;
                    break;
                case MOOD:
                    mood += answer.getAnswer() + 4;
                    break;
            }
        }
        sportDataBase.dayDao().updateHealthById(health, dayId);
        sportDataBase.dayDao().updateActivityById(activity, dayId);
        sportDataBase.dayDao().updateMoodById(mood, dayId);
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }
}
