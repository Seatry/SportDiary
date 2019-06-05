package com.example.alexander.sportdiary.Fragments;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.alexander.sportdiary.Adapters.RestAdapter;
import com.example.alexander.sportdiary.Adapters.TestAdapter;
import com.example.alexander.sportdiary.Adapters.TrainingAdapter;
import com.example.alexander.sportdiary.Dao.DayDao;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.DayToTest;
import com.example.alexander.sportdiary.Entities.Rest;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.TrainingsToAims;
import com.example.alexander.sportdiary.Entities.TrainingsToEquipments;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class DayFragment extends Fragment implements View.OnClickListener {
    private long seasonPlanId;
    private String diaryName;
    private CalendarView calendarView;
    private String currentDate;
    private DayDao dayDao;
    private TrainingAdapter trainingAdapter;
    private TestAdapter testAdapter;
    private RestAdapter restAdapter;
    private SportDataBase sportDataBase;
    private TextView blockText;
    private TextView stageText;
    private TextView typeText;
    private TextView campsText;
    private TextView competitionText;
    private TextView capacityText;
    private LiveData<Long> blockId;
    private LiveData<Long> stageId;
    private LiveData<Long> typeId;
    private LiveData<Long> campId;
    private LiveData<Long> CiId;
    private LiveData<List<Training>> trainingLiveData;
    private LiveData<List<DayToTest>> testLiveData;
    private LiveData<List<Rest>> restLiveData;

    public void setSeasonPlanId(long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
    }

    @Nullable
    public Long getSeasonPlanId() {
        return seasonPlanId;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_day, container, false);

        sportDataBase = MainActivity.getDatabase();

        SeasonPlan seasonPlan = sportDataBase.seasonPlanDao().getSeasonPlanById(seasonPlanId);
        Date minDate = seasonPlan.getStart();
        Date maxDate = DateUtil.addDays(minDate, 365);
        diaryName = MainActivity.getInstance().getTitle().toString();
        MainActivity.getInstance().setTitle(diaryName + " " + sdf.format(minDate));

        currentDate = sdf.format(minDate);
        dayDao = MainActivity.getDatabase().dayDao();

        calendarView = v.findViewById(R.id.calendar);
        calendarView.setMinDate(minDate.getTime());
        calendarView.setMaxDate(maxDate.getTime());
        calendarView.setDate(calendarView.getMinDate());
        calendarView.setVisibility(View.GONE);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month += 1;
            currentDate = dayOfMonth + "." + (month < 10 ? "0" + month : month) + "." + year;
            MainActivity.getInstance().setTitle(diaryName + " " + currentDate);
            getTrainings();
            getTests();
            getRests();
            getInfo();
        });

        RecyclerView trainingRecyclerView = v.findViewById(R.id.trainingItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        trainingRecyclerView.setLayoutManager(layoutManager);
        trainingAdapter = new TrainingAdapter();
        trainingRecyclerView.setAdapter(trainingAdapter);
        getTrainings();

        RecyclerView testRecyclerView = v.findViewById(R.id.testItems);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        testRecyclerView.setLayoutManager(layoutManager2);
        testAdapter = new TestAdapter();
        testRecyclerView.setAdapter(testAdapter);
        getTests();

        RecyclerView restRecyclerView = v.findViewById(R.id.restItems);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        restRecyclerView.setLayoutManager(layoutManager3);
        restAdapter = new RestAdapter();
        restRecyclerView.setAdapter(restAdapter);
        getRests();

        blockText = v.findViewById(R.id.blockText);
        stageText = v.findViewById(R.id.stageText);
        typeText = v.findViewById(R.id.typeText);
        campsText = v.findViewById(R.id.campsText);
        competitionText = v.findViewById(R.id.day_competition_text);
        capacityText = v.findViewById(R.id.day_capacity_text);
        getInfo();

        setEditObservers();

        Button sanButton = v.findViewById(R.id.sanButton);
        sanButton.setOnClickListener(this);
        Button dreamButton = v.findViewById(R.id.dreamButton);
        dreamButton.setOnClickListener(this);

        TabHost tabHost = v.findViewById(R.id.day_tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("day1");

        tabSpec.setContent(R.id.trainingTab);
        tabSpec.setIndicator(MainActivity.getInstance().getString(R.string.Trainings));
        tabHost.addTab(tabSpec);

        FloatingActionButton addTraining = v.findViewById(R.id.addTraining);
        addTraining.setOnClickListener(view -> {
            try {
                long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
                AddTrainingFragment addTrainingFragment = new AddTrainingFragment();
                addTrainingFragment
                        .setDayId(dayId)
                        .setOption(EditOption.INSERT)
                        .setTitle(MainActivity.getInstance().getString(R.string.addTraining));
                addTrainingFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addTraining");
            } catch (ParseException e) {
                e.printStackTrace();
            }});

        FloatingActionButton addTest = v.findViewById(R.id.addTest);
        addTest.setOnClickListener(view -> {
            try {
                long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
                AddTestFragment addTestFragment = new AddTestFragment();
                addTestFragment
                        .setDayId(dayId)
                        .setOption(EditOption.INSERT)
                        .setTitle(MainActivity.getInstance().getString(R.string.addTest));
                addTestFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addTests");
            } catch (ParseException e) {
                e.printStackTrace();
            }});

        FloatingActionButton addRest = v.findViewById(R.id.addRest);
        addRest.setOnClickListener(view -> {
            try {
                long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
                AddRestFragment addRestFragment = new AddRestFragment();
                addRestFragment
                        .setDayId(dayId)
                        .setOption(EditOption.INSERT)
                        .setTitle(MainActivity.getInstance().getString(R.string.addRest));
                addRestFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addRest");
            } catch (ParseException e) {
                e.printStackTrace();
            }});

        tabSpec = tabHost.newTabSpec("day2");
        tabSpec.setContent(R.id.testTab);
        tabSpec.setIndicator(MainActivity.getInstance().getString(R.string.Tests));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("day3");
        tabSpec.setContent(R.id.restTab);
        tabSpec.setIndicator(MainActivity.getInstance().getString(R.string.Rest));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("day4");
        tabSpec.setContent(R.id.questionsTab);
        tabSpec.setIndicator(MainActivity.getInstance().getString(R.string.Questionnaire));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("day5");
        tabSpec.setContent(R.id.dayInfoTab);
        tabSpec.setIndicator(MainActivity.getInstance().getString(R.string.Information));
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        return v;
    }

    private void setEditObservers() {
        LiveData<List<TrainingsToAims>> trainingsToAims = sportDataBase.trainingsToAimsDao().getAll();
        trainingsToAims.observe(MainActivity.getInstance(), trainingsToAims1 -> trainingAdapter.notifyDataSetChanged());
        LiveData<List<TrainingsToEquipments>> trainingsToEquipments = sportDataBase.trainingsToEquipmentsDao().getAll();
        trainingsToEquipments.observe(MainActivity.getInstance(), trainingsToEquipments1 -> trainingAdapter.notifyDataSetChanged());
        sportDataBase.timeDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), times -> {
            trainingAdapter.notifyDataSetChanged();
            restAdapter.notifyDataSetChanged();
        });
        sportDataBase.trainingPlaceDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), trainingPlaces -> trainingAdapter.notifyDataSetChanged());
        sportDataBase.aimDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), aims -> trainingAdapter.notifyDataSetChanged());
        sportDataBase.equipmentDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), equipment -> trainingAdapter.notifyDataSetChanged());
        sportDataBase.borgDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), borgs -> trainingAdapter.notifyDataSetChanged());
        sportDataBase.testDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), tests -> testAdapter.notifyDataSetChanged());
        sportDataBase.restPlaceDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), restPlaces -> restAdapter.notifyDataSetChanged());
        sportDataBase.blockDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), blocks -> getInfo());
        sportDataBase.stageDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), stages -> getInfo());
        sportDataBase.typeDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), types -> getInfo());
        sportDataBase.campDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), camps -> getInfo());
        sportDataBase.competitionDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), competitions -> getInfo());
        sportDataBase.importanceDao().getAllByUserId(MainActivity.getUserId()).observe(MainActivity.getInstance(), importanceList -> getInfo());
    }

    public void getTrainings() {
        try {
            final long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
            if (trainingLiveData != null) trainingLiveData.removeObservers(MainActivity.getInstance());
            trainingLiveData = sportDataBase.trainingDao().getAllLiveByDayId(dayId);
            trainingLiveData.observe(MainActivity.getInstance(), elems -> {
                trainingAdapter.setTrainings(elems);
                trainingAdapter.notifyDataSetChanged();
                if (elems != null) {
                    int capacity = 0;
                    for(Training training : elems) {
                        capacity += training.getCapacity();
                    }
                    sportDataBase.dayDao().updateCapacityById(capacity, dayId);
                    capacityText.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.capacity), capacity));
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getTests() {
        try {
            final long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
            if (testLiveData != null) testLiveData.removeObservers(MainActivity.getInstance());
            testLiveData = sportDataBase.dayToTestDao().getAllLiveByDayId(dayId);
            testLiveData.observe(MainActivity.getInstance(), elems -> {
                testAdapter.setTests(elems);
                testAdapter.notifyDataSetChanged();
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getRests() {
        try {
            final long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
            if (restLiveData != null) restLiveData.removeObservers(MainActivity.getInstance());
            restLiveData = sportDataBase.restDao().getAllLiveByDayId(dayId);
            restLiveData.observe(MainActivity.getInstance(), elems -> {
                restAdapter.setRests(elems);
                restAdapter.notifyDataSetChanged();
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getInfo() {
        try {
            Date date = sdf.parse(currentDate);
            Day day = dayDao.getDayByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
            MainActivity instance = MainActivity.getInstance();

            if (blockId != null) blockId.removeObservers(instance);
            blockId = dayDao.getLiveBlockIdBySIdAndDate(seasonPlanId, date);
            blockId.observe(instance, id -> {
                String block = id == null ? "" : sportDataBase.blockDao().getNameByIdAndUserId(id, MainActivity.getUserId());
                blockText.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.block), block));
            });
            if (stageId != null) stageId.removeObservers(instance);
            stageId = dayDao.getLiveStageIdBySIdAndDate(seasonPlanId, date);
            stageId.observe(instance, id -> {
                String stage = id == null ? "" : sportDataBase.stageDao().getNameByIdAndUserId(id, MainActivity.getUserId());
                stageText.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.stage), stage));
            });
            if (typeId != null) typeId.removeObservers(instance);
            typeId = dayDao.getLiveTypeIdBySIdAndDate(seasonPlanId, date);
            typeId.observe(instance, id -> {
                String type = id == null ? "" : sportDataBase.typeDao().getNameByIdAndUserId(id, MainActivity.getUserId());
                typeText.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.type), type));
            });
            if (campId != null) campId.removeObservers(instance);
            campId = dayDao.getLiveCampIdBySIdAndDate(seasonPlanId, date);
            campId.observe(instance, id -> {
                String camps = id == null ? "" : sportDataBase.campDao().getNameByIdAndUserId(id, MainActivity.getUserId());
                campsText.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.camps), camps));
            });
            if (CiId != null) CiId.removeObservers(instance);
            CiId = dayDao.getLiveCIIdBySIdAndDate(seasonPlanId, date);
            CiId.observe(instance, competitionToImportanceId -> {
                String competitionToImportance;
                if (competitionToImportanceId == null) {
                    competitionToImportance = "";
                } else {
                    Long competitionId = sportDataBase.competitionToImportanceDao().getCompetitionIdById(competitionToImportanceId);
                    String competition = sportDataBase.competitionDao().getNameByIdAndUserId(competitionId, MainActivity.getUserId());
                    Long importanceId = sportDataBase.competitionToImportanceDao().getImportanceIdById(competitionToImportanceId);
                    String importance = importanceId == null ? "" : sportDataBase.importanceDao().getNameByIdAndUserId(importanceId, MainActivity.getUserId());
                    competitionToImportance = competition + " (" + importance + ")";
                }
                competitionText.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.competition), competitionToImportance));
            });
            if (day != null) {
                String capacity = String.valueOf(day.getCapacity());
                capacityText.setText(String.format("%s: %s", MainActivity.getInstance().getString(R.string.capacity), capacity));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void changeCalendarVisibility() {
        if (calendarView.getVisibility() == View.GONE) {
            calendarView.setVisibility(View.VISIBLE);
        } else {
            calendarView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sanButton:
                try {
                    final long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
                    SanFragment sanFragment = new SanFragment();
                    sanFragment.setDayId(dayId);
                    sanFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "san");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.dreamButton:
                try {
                    final long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
                    DreamFragment dreamFragment = new DreamFragment();
                    dreamFragment.setDayId(dayId);
                    dreamFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "san");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
