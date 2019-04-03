package com.example.alexander.sportdiary.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TabHost;

import com.example.alexander.sportdiary.Adapters.EditAdapter;
import com.example.alexander.sportdiary.Adapters.TrainingAdapter;
import com.example.alexander.sportdiary.Dao.DayDao;
import com.example.alexander.sportdiary.EditOption;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class DayFragment extends Fragment {
    private long seasonPlanId;
    private String diaryName;
    private CalendarView calendarView;
    private String currentDate;
    private DayDao dayDao;
    private RecyclerView recyclerView;
    private TrainingAdapter adapter;
    private SportDataBase sportDataBase;

    public void setSeasonPlanId(long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
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
            }
        });

        recyclerView = v.findViewById(R.id.trainingItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TrainingAdapter();
        recyclerView.setAdapter(adapter);
        getTrainings();

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton addRest = v.findViewById(R.id.addRest);
        addRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
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

    public void getTrainings() {
        try {
            long dayId = dayDao.getDayIdByDateAndSeasonPlanId(sdf.parse(currentDate), seasonPlanId);
            LiveData<List<Training>> trainingLiveData = sportDataBase.trainingDao().getAllByDayId(dayId);
            trainingLiveData.observe(MainActivity.getInstance(), new Observer<List<Training>>() {
                @Override
                public void onChanged(@Nullable List<Training> elems) {
                    adapter.setTrainings(elems);
                    adapter.notifyDataSetChanged();
                }
            });
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
}
