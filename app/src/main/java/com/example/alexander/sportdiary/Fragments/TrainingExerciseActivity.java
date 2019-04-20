package com.example.alexander.sportdiary.Fragments;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.alexander.sportdiary.Adapters.TrainingExerciseAdapter;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;

import java.util.List;

public class TrainingExerciseActivity extends AppCompatActivity {
    private int trainingId;
    private TrainingExerciseAdapter adapter;
    private SportDataBase sportDataBase;
    private static TrainingExerciseActivity instance;

    public static TrainingExerciseActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_exercise);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.exercises_to_training);

        sportDataBase = MainActivity.getInstance().getDatabase();
        instance = this;

        Intent intent = getIntent();
        trainingId = intent.getIntExtra("trainingId", 0);
        RecyclerView recyclerView = findViewById(R.id.training_exercises);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TrainingExerciseAdapter();
        recyclerView.setAdapter(adapter);

        LiveData<List<TrainingExercise>> trainingLiveData = sportDataBase.trainingExerciseDao()
                .getAllLiveByTrainingId(trainingId);
        trainingLiveData.observe(TrainingExerciseActivity.getInstance(), elems -> {
            adapter.setTrainingExercises(elems);
            adapter.notifyDataSetChanged();
            if (elems != null) {
                int capacity = 0;
                for(TrainingExercise exercise : elems) {
                    capacity += (exercise.getLength() * exercise.getRepeats() * exercise.getSeries());
                }
                sportDataBase.trainingDao().updateCapacityById(capacity, trainingId);
            }
        });

        FloatingActionButton addTrainingExercise = findViewById(R.id.addTrainingExercise);
        addTrainingExercise.setOnClickListener(view -> {
            AddTrainingExerciseFragment addTrainingExerciseFragment = new AddTrainingExerciseFragment();
            addTrainingExerciseFragment
                    .setTrainingId(trainingId)
                    .setOption(EditOption.INSERT)
                    .setTitle(getString(R.string.addExerciseToTraining));
            addTrainingExerciseFragment.show(TrainingExerciseActivity.getInstance().getSupportFragmentManager(), "addTrainingExercise");
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
