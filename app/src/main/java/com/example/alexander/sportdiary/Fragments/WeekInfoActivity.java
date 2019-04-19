package com.example.alexander.sportdiary.Fragments;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alexander.sportdiary.Entities.CompetitionToImportance;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class WeekInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private SportDataBase sportDataBase;
    private static WeekInfoActivity instance;
    private long seasonPlanId;
    private Date start;
    private TextView blockText;
    private TextView stageText;
    private TextView typeText;
    private TextView campsText;

    public static WeekInfoActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sportDataBase = MainActivity.getInstance().getDatabase();
        instance = this;

        Intent intent = getIntent();
        seasonPlanId = intent.getLongExtra("seasonPlanId", 0);
        String title = intent.getStringExtra("title");
        actionBar.setTitle(title);
        int week = intent.getIntExtra("week", 0);

        blockText = findViewById(R.id.weekBlock);
        stageText = findViewById(R.id.weekStage);
        typeText = findViewById(R.id.weekType);
        campsText = findViewById(R.id.weekCamps);
        TextView aimsText = findViewById(R.id.weekAims);
        TextView equipmentsText = findViewById(R.id.weekEquipments);
        TextView competitionsText = findViewById(R.id.weekCompetitions);
        TextView capacityText = findViewById(R.id.weekCapacity);
        Button updateButton = findViewById(R.id.updateWeekInfo);
        updateButton.setOnClickListener(this);

        SeasonPlan seasonPlan = sportDataBase.seasonPlanDao().getSeasonPlanById(seasonPlanId);
        start = DateUtil.addDays(seasonPlan.getStart(), (week -1) * 7);
        LiveData<Day> dayLiveData = sportDataBase.dayDao().getLiveDayByDateAndSeasonPlanId(start, seasonPlanId);
        dayLiveData.observe(this, day -> {
            if (day == null) return;
            String block = day.getBlockId() == null ? "" :
                    sportDataBase.blockDao().getNameByIdAndUserId(day.getBlockId(), MainActivity.getUserId());
            String stage = day.getStageId() == null ? "" :
                    sportDataBase.stageDao().getNameByIdAndUserId(day.getStageId(), MainActivity.getUserId());
            String type = day.getTypeId() == null ? "" :
                    sportDataBase.typeDao().getNameByIdAndUserId(day.getTypeId(), MainActivity.getUserId());
            String camps = day.getCampId() == null ? "" :
                    sportDataBase.campDao().getNameByIdAndUserId(day.getCampId(), MainActivity.getUserId());

            blockText.setText(String.format("%s: %s", getString(R.string.block), block));
            stageText.setText(String.format("%s: %s", getString(R.string.stage), stage));
            typeText.setText(String.format("%s: %s", getString(R.string.type), type));
            campsText.setText(String.format("%s: %s", getString(R.string.camps), camps));
        });


        Date date = start;
        int capacity = 0;
        StringBuilder competitions = new StringBuilder();
        HashSet<String> aims = new HashSet<>();
        HashSet<String> equipments = new HashSet<>();
        for(int i = 0; i < 7; i++) {
            Day day = sportDataBase.dayDao().getDayByDateAndSeasonPlanId(date, seasonPlanId);
            if (day.getCompetitionToImportanceId() != null) {
                CompetitionToImportance competitionToImportance = sportDataBase.competitionToImportanceDao()
                        .getById(day.getCompetitionToImportanceId());
                String competition = sportDataBase.competitionDao().getNameByIdAndUserId(competitionToImportance.getCompetitionId(), MainActivity.getUserId());
                String importance = competitionToImportance.getImportanceId() == null ? "" :
                        sportDataBase.importanceDao().getNameByIdAndUserId(competitionToImportance.getImportanceId(), MainActivity.getUserId());
                competitions.append(competition).append(" (").append(importance).append(")").append(i == 6 ? "" : ", ");
            }
            List<Training> trainings = sportDataBase.trainingDao().getAllByDayId(day.getId());
            for (Training training : trainings) {
                List<Long> aimIds = sportDataBase.trainingsToAimsDao().getAimIdsByTrainingId(training.getId());
                List<Long> equipmentIds = sportDataBase.trainingsToEquipmentsDao().getEquipmentIdsByTrainingId(training.getId());
                for(Long aimId : aimIds) {
                    aims.add(sportDataBase.aimDao().getNameByIdAndUserId(aimId, MainActivity.getUserId()));
                }
                for(Long equipmentId : equipmentIds) {
                    equipments.add(sportDataBase.equipmentDao().getNameByIdAndUserId(equipmentId, MainActivity.getUserId()));
                }
            }
            capacity += day.getCapacity();
            date = DateUtil.addDays(date, 1);
        }

        aimsText.setText(String.format("%s: %s", getString(R.string.aims), aims.toString()));
        equipmentsText.setText(String.format("%s: %s", getString(R.string.equipment), equipments.toString()));
        competitionsText.setText(String.format("%s: %s", getString(R.string.competitions), competitions));
        capacityText.setText(String.format("%s: %s", getString(R.string.capacity), String.valueOf(capacity)));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateWeekInfo:
                UpdateWeekInfoFragment weekInfoFragment = new UpdateWeekInfoFragment();
                weekInfoFragment.setSeasonPlanId(seasonPlanId);
                weekInfoFragment.setStart(start);
                weekInfoFragment.show(getSupportFragmentManager(), "updateWeekInfo");
                break;
        }
    }
}

