package com.example.alexander.sportdiary.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.DayToTest;
import com.example.alexander.sportdiary.Entities.EditEntities.Aim;
import com.example.alexander.sportdiary.Entities.EditEntities.Block;
import com.example.alexander.sportdiary.Entities.EditEntities.Borg;
import com.example.alexander.sportdiary.Entities.EditEntities.Camp;
import com.example.alexander.sportdiary.Entities.EditEntities.Competition;
import com.example.alexander.sportdiary.Entities.EditEntities.Equipment;
import com.example.alexander.sportdiary.Entities.EditEntities.Importance;
import com.example.alexander.sportdiary.Entities.EditEntities.RestPlace;
import com.example.alexander.sportdiary.Entities.EditEntities.Stage;
import com.example.alexander.sportdiary.Entities.EditEntities.Test;
import com.example.alexander.sportdiary.Entities.EditEntities.Time;
import com.example.alexander.sportdiary.Entities.EditEntities.TrainingPlace;
import com.example.alexander.sportdiary.Entities.EditEntities.Type;
import com.example.alexander.sportdiary.Entities.Rest;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.TrainingsToAims;
import com.example.alexander.sportdiary.Entities.TrainingsToEquipments;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
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
    private RecyclerView trainingRecyclerView;
    private TrainingAdapter trainingAdapter;
    private RecyclerView testRecyclerView;
    private TestAdapter testAdapter;
    private RecyclerView restRecyclerView;
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
    private Button sanButton;
    private Button dreamButton;

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

        sportDataBase = MainActivity.getInstance().getDatabase();

        SeasonPlan seasonPlan = sportDataBase.seasonPlanDao().getSeasonPlanById(seasonPlanId);
        Date minDate = seasonPlan.getStart();
        Date maxDate = DateUtil.addDays(minDate, 365);
        diaryName = MainActivity.getInstance().getTitle().toString();
        MainActivity.getInstance().setTitle(diaryName + " " + sdf.format(minDate));

        currentDate = sdf.format(minDate);
        dayDao = MainActivity.getInstance().getDatabase().dayDao();

        calendarView = v.findViewById(R.id.calendar);
        calendarView.setMinDate(minDate.getTime());
        calendarView.setMaxDate(maxDate.getTime());
        calendarView.setDate(calendarView.getMinDate());
        calendarView.setVisibility(View.GONE);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                currentDate = dayOfMonth + "." + (month < 10 ? "0" + month : month) + "." + year;
                MainActivity.getInstance().setTitle(diaryName + " " + currentDate);
                getTrainings();
                getTests();
                getRests();
                getInfo();
            }
        });

        trainingRecyclerView = v.findViewById(R.id.trainingItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        trainingRecyclerView.setLayoutManager(layoutManager);
        trainingAdapter = new TrainingAdapter();
        trainingRecyclerView.setAdapter(trainingAdapter);
        getTrainings();

        testRecyclerView = v.findViewById(R.id.testItems);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        testRecyclerView.setLayoutManager(layoutManager2);
        testAdapter = new TestAdapter();
        testRecyclerView.setAdapter(testAdapter);
        getTests();

        restRecyclerView = v.findViewById(R.id.restItems);
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

        sanButton = v.findViewById(R.id.sanButton);
        sanButton.setOnClickListener(this);
        dreamButton = v.findViewById(R.id.dreamButton);
        dreamButton.setOnClickListener(this);

        TabHost tabHost = v.findViewById(R.id.day_tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("day1");

        tabSpec.setContent(R.id.trainingTab);
        tabSpec.setIndicator(getString(R.string.Trainings));
        tabHost.addTab(tabSpec);

        FloatingActionButton addTraining = v.findViewById(R.id.addTraining);
        addTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
                    AddTrainingFragment addTrainingFragment = new AddTrainingFragment();
                    addTrainingFragment
                            .setDayId(dayId)
                            .setOption(EditOption.INSERT)
                            .setTitle(getString(R.string.addTraining));
                    addTrainingFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addTraining");
                } catch (ParseException e) {
                    e.printStackTrace();
                }}
        });

        FloatingActionButton addTest = v.findViewById(R.id.addTest);
        addTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
                    AddTestFragment addTestFragment = new AddTestFragment();
                    addTestFragment
                            .setDayId(dayId)
                            .setOption(EditOption.INSERT)
                            .setTitle(getString(R.string.addTest));
                    addTestFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addTests");
                } catch (ParseException e) {
                    e.printStackTrace();
                }}
        });

        FloatingActionButton addRest = v.findViewById(R.id.addRest);
        addRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
                    AddRestFragment addRestFragment = new AddRestFragment();
                    addRestFragment
                            .setDayId(dayId)
                            .setOption(EditOption.INSERT)
                            .setTitle(getString(R.string.addRest));
                    addRestFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addRest");
                } catch (ParseException e) {
                    e.printStackTrace();
                }}
        });

        tabSpec = tabHost.newTabSpec("day2");
        tabSpec.setContent(R.id.testTab);
        tabSpec.setIndicator(getString(R.string.Tests));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("day3");
        tabSpec.setContent(R.id.restTab);
        tabSpec.setIndicator(getString(R.string.Rest));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("day4");
        tabSpec.setContent(R.id.questionsTab);
        tabSpec.setIndicator(getString(R.string.Questionnaire));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("day5");
        tabSpec.setContent(R.id.dayInfoTab);
        tabSpec.setIndicator(getString(R.string.Information));
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        return v;
    }

    private void setEditObservers() {
        LiveData<List<TrainingsToAims>> trainingsToAims = sportDataBase.trainingsToAimsDao().getAll();
        trainingsToAims.observe(MainActivity.getInstance(), new Observer<List<TrainingsToAims>>() {
            @Override
            public void onChanged(@Nullable List<TrainingsToAims> trainingsToAims) {
                trainingAdapter.notifyDataSetChanged();
            }
        });
        LiveData<List<TrainingsToEquipments>> trainingsToEquipments = sportDataBase.trainingsToEquipmentsDao().getAll();
        trainingsToEquipments.observe(MainActivity.getInstance(), new Observer<List<TrainingsToEquipments>>() {
            @Override
            public void onChanged(@Nullable List<TrainingsToEquipments> trainingsToEquipments) {
                trainingAdapter.notifyDataSetChanged();
            }
        });
        sportDataBase.timeDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Time>>() {
            @Override
            public void onChanged(@Nullable List<Time> times) {
                trainingAdapter.notifyDataSetChanged();
                restAdapter.notifyDataSetChanged();
            }
        });
        sportDataBase.trainingPlaceDao().getAll().observe(MainActivity.getInstance(), new Observer<List<TrainingPlace>>() {
            @Override
            public void onChanged(@Nullable List<TrainingPlace> trainingPlaces) {
                trainingAdapter.notifyDataSetChanged();
            }
        });
        sportDataBase.aimDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Aim>>() {
            @Override
            public void onChanged(@Nullable List<Aim> aims) {
                trainingAdapter.notifyDataSetChanged();
            }
        });
        sportDataBase.equipmentDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Equipment>>() {
            @Override
            public void onChanged(@Nullable List<Equipment> equipment) {
                trainingAdapter.notifyDataSetChanged();
            }
        });
        sportDataBase.borgDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Borg>>() {
            @Override
            public void onChanged(@Nullable List<Borg> borgs) {
                trainingAdapter.notifyDataSetChanged();
            }
        });
        sportDataBase.testDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Test>>() {
            @Override
            public void onChanged(@Nullable List<Test> tests) {
                testAdapter.notifyDataSetChanged();
            }
        });
        sportDataBase.restPlaceDao().getAll().observe(MainActivity.getInstance(), new Observer<List<RestPlace>>() {
            @Override
            public void onChanged(@Nullable List<RestPlace> restPlaces) {
                restAdapter.notifyDataSetChanged();
            }
        });
        sportDataBase.blockDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Block>>() {
            @Override
            public void onChanged(@Nullable List<Block> blocks) {
                getInfo();
            }
        });
        sportDataBase.stageDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Stage>>() {
            @Override
            public void onChanged(@Nullable List<Stage> stages) {
                getInfo();
            }
        });
        sportDataBase.typeDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Type>>() {
            @Override
            public void onChanged(@Nullable List<Type> types) {
                getInfo();
            }
        });
        sportDataBase.campDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Camp>>() {
            @Override
            public void onChanged(@Nullable List<Camp> camps) {
                getInfo();
            }
        });
        sportDataBase.competitionDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Competition>>() {
            @Override
            public void onChanged(@Nullable List<Competition> competitions) {
                getInfo();
            }
        });
        sportDataBase.importanceDao().getAll().observe(MainActivity.getInstance(), new Observer<List<Importance>>() {
            @Override
            public void onChanged(@Nullable List<Importance> importanceList) {
                getInfo();
            }
        });
    }

    public void getTrainings() {
        try {
            final long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
            if (trainingLiveData != null) trainingLiveData.removeObservers(MainActivity.getInstance());
            trainingLiveData = sportDataBase.trainingDao().getAllLiveByDayId(dayId);
            trainingLiveData.observe(MainActivity.getInstance(), new Observer<List<Training>>() {
                @Override
                public void onChanged(@Nullable List<Training> elems) {
                    trainingAdapter.setTrainings(elems);
                    trainingAdapter.notifyDataSetChanged();
                    if (elems != null) {
                        int capacity = 0;
                        for(Training training : elems) {
                            capacity += training.getCapacity();
                        }
                        sportDataBase.dayDao().updateCapacityById(capacity, dayId);
                    }
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
            testLiveData.observe(MainActivity.getInstance(), new Observer<List<DayToTest>>() {
                @Override
                public void onChanged(@Nullable List<DayToTest> elems) {
                    testAdapter.setTests(elems);
                    testAdapter.notifyDataSetChanged();
                }
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
            restLiveData.observe(MainActivity.getInstance(), new Observer<List<Rest>>() {
                @Override
                public void onChanged(@Nullable List<Rest> elems) {
                    restAdapter.setRests(elems);
                    restAdapter.notifyDataSetChanged();
                }
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
            blockId.observe(instance, new Observer<Long>() {
                @Override
                public void onChanged(@Nullable Long id) {
                    String block = id == null ? "" : sportDataBase.blockDao().getNameById(id);
                    blockText.setText(String.format("%s: %s", getString(R.string.block), block));
                }
            });
            if (stageId != null) stageId.removeObservers(instance);
            stageId = dayDao.getLiveStageIdBySIdAndDate(seasonPlanId, date);
            stageId.observe(instance, new Observer<Long>() {
                @Override
                public void onChanged(@Nullable Long id) {
                    String stage = id == null ? "" : sportDataBase.stageDao().getNameById(id);
                    stageText.setText(String.format("%s: %s", getString(R.string.stage), stage));
                }
            });
            if (typeId != null) typeId.removeObservers(instance);
            typeId = dayDao.getLiveTypeIdBySIdAndDate(seasonPlanId, date);
            typeId.observe(instance, new Observer<Long>() {
                @Override
                public void onChanged(@Nullable Long id) {
                    String type = id == null ? "" : sportDataBase.typeDao().getNameById(id);
                    typeText.setText(String.format("%s: %s", getString(R.string.type), type));
                }
            });
            if (campId != null) campId.removeObservers(instance);
            campId = dayDao.getLiveCampIdBySIdAndDate(seasonPlanId, date);
            campId.observe(instance, new Observer<Long>() {
                @Override
                public void onChanged(@Nullable Long id) {
                    String camps = id == null ? "" : sportDataBase.campDao().getNameById(id);
                    campsText.setText(String.format("%s: %s", getString(R.string.camps), camps));
                }
            });
            if (CiId != null) CiId.removeObservers(instance);
            CiId = dayDao.getLiveCIIdBySIdAndDate(seasonPlanId, date);
            CiId.observe(instance, new Observer<Long>() {
                @Override
                public void onChanged(@Nullable Long competitionToImportanceId) {
                    String competitionToImportance;
                    if (competitionToImportanceId == null) {
                        competitionToImportance = "";
                    } else {
                        Long competitionId = sportDataBase.competitionToImportanceDao().getCompetitionIdById(competitionToImportanceId);
                        String competition = sportDataBase.competitionDao().getNameById(competitionId);
                        Long importanceId = sportDataBase.competitionToImportanceDao().getImportanceIdById(competitionToImportanceId);
                        String importance = importanceId == null ? "" : sportDataBase.importanceDao().getNameById(importanceId);
                        competitionToImportance = competition + " (" + importance + ")";
                    }
                    competitionText.setText(String.format("%s: %s", getString(R.string.competition), competitionToImportance));
                }
            });
            String capacity = String.valueOf(day.getCapacity());
            capacityText.setText(String.format("%s: %s", getString(R.string.capacity), capacity));
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
