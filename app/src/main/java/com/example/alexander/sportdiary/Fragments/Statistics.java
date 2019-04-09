package com.example.alexander.sportdiary.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.Utils.DateUtil;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class Statistics extends AppCompatActivity implements View.OnClickListener {
    private static Statistics instance;
    private long seasonPlanId;
    private LinearLayout inputLayout;
    private SportDataBase sportDataBase;
    private EditText fromEdit;
    private EditText toEdit;
    private SeasonPlan seasonPlan;
    private Date maxDate;
    private Date minDate;
    private GraphView banisterGraph;
    private GraphView trimpsGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.statistics);

        instance = this;

        Intent intent = getIntent();
        seasonPlanId = intent.getLongExtra("seasonPlanId", 0);

        inputLayout = findViewById(R.id.inputLayout);
        sportDataBase = MainActivity.getInstance().getDatabase();

        fromEdit = findViewById(R.id.fromEdit);
        toEdit = findViewById(R.id.toEdit);
        Button drawButton = findViewById(R.id.drawButton);
        drawButton.setOnClickListener(this);

        TabHost tabHost = findViewById(R.id.graph_tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("graph1");

        tabSpec.setContent(R.id.banisterTab);
        tabSpec.setIndicator(getString(R.string.banister_model));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("graph2");
        tabSpec.setContent(R.id.questionsGraphTab);
        tabSpec.setIndicator(getString(R.string.Questionnaire));
        tabHost.addTab(tabSpec);

        banisterGraph = findViewById(R.id.performanceGraph);
        banisterGraph.setTitle(getString(R.string.banister_model));
        banisterGraph.getGridLabelRenderer().setHorizontalAxisTitle(getString(R.string.date));
        banisterGraph.getGridLabelRenderer().setVerticalAxisTitle(getString(R.string.performance));
        banisterGraph.getGridLabelRenderer().setHorizontalLabelsAngle(35);
        trimpsGraph = findViewById(R.id.trimpsGraph);
        trimpsGraph.setTitle(getString(R.string.trimps_graph));
        trimpsGraph.getGridLabelRenderer().setHorizontalAxisTitle(getString(R.string.date));
        trimpsGraph.getGridLabelRenderer().setVerticalAxisTitle(getString(R.string.trimp));
        trimpsGraph.getGridLabelRenderer().setHorizontalLabelsAngle(35);

        seasonPlan = sportDataBase.seasonPlanDao().getSeasonPlanById(seasonPlanId);
        maxDate = DateUtil.addDays(seasonPlan.getStart(), 365);
        minDate = seasonPlan.getStart();

        setDatePickerDialog(fromEdit);
        setDatePickerDialog(toEdit);
    }

    private void setDatePickerDialog(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String currentDate = dayOfMonth + "." + (month < 10 ? "0" + month : month) + "." + year;
                        editText.setText(currentDate);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(Statistics.getInstance());
                datePickerDialog.setOnDateSetListener(listener);
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statisctics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.input:
                inputChangeVisibility();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void inputChangeVisibility() {
        inputLayout.setVisibility(inputLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public long getSeasonPlanId() {
        return seasonPlanId;
    }

    public static Statistics getInstance() {
        return instance;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawButton:
                draw();
                break;
        }
    }

    public void draw() {
        Date from, to;
        try {
            from = sdf.parse(fromEdit.getText().toString());
            to = sdf.parse(toEdit.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(this, R.string.date_format_err, Toast.LENGTH_SHORT).show();
            return;
        }
        if (from.after(maxDate) || from.before(minDate) || to.after(maxDate) || to.before(minDate)) {
            Toast.makeText(this, R.string.date_in_season, Toast.LENGTH_SHORT).show();
            return;
        }
        if (from.after(to)) {
            Toast.makeText(this, getString(R.string.from_to_err), Toast.LENGTH_SHORT).show();
            return;
        }
        double y = seasonPlan.getMale().equals("M") ? 1.92 : 1.67;
        double hrMax = seasonPlan.getHrMax();
        double hrRest = seasonPlan.getHrRest();
        int lastPerformance = seasonPlan.getLastPerformance();
        double k1 = 1, k2 = 1.9, r1 = 49.5, r2 = 11;
        List<Day> dayList = sportDataBase.dayDao().getAllBySeasonPlanId(seasonPlanId);
        HashMap<Date, Double> trimpToDate = new HashMap<>();
        for (Day day : dayList) {
            double trimp = 0;
            List<Training> trainings = sportDataBase.trainingDao().getAllByDayId(day.getId());
            for (Training training : trainings) {
                List<TrainingExercise> exercises = sportDataBase.trainingExerciseDao().getAllByTrainingId(training.getId());
                for (TrainingExercise exercise : exercises) {
                    int time = exercise.getMinutes();
                    double hrAvg = exercise.getHrAvg();
                    double hrReserve = (hrAvg - hrRest) / (hrMax - hrRest);
                    trimp += time * hrReserve * 0.64 * Math.exp(y * hrReserve);
                }
            }
            trimpToDate.put(day.getDate(), trimp);
        }

        LineGraphSeries<DataPoint> banisterSeries = new LineGraphSeries<>(new DataPoint[]{});
        LineGraphSeries<DataPoint> trimpSeries = new LineGraphSeries<>(new DataPoint[]{});

        int count = 0;
        double fitness = 0, fatigue = 0;
        for(Date start = seasonPlan.getStart(); start.before(from); start = DateUtil.addDays(start, 1)) {
            fitness = fitness * Math.exp(-1. / r1) + trimpToDate.get(start);
            fatigue = fatigue * Math.exp(-1. / r2) + trimpToDate.get(start);
        }
        for (Date start = from; start.before(to); start = DateUtil.addDays(start, 1), count++) {
            fitness = fitness * Math.exp(-1. / r1) + trimpToDate.get(start);
            fatigue = fatigue * Math.exp(-1. / r2) + trimpToDate.get(start);
            double performance = lastPerformance + fitness * k1  - fatigue * k2;
            banisterSeries.appendData(new DataPoint(start.getTime(), performance), false, 365);
            trimpSeries.appendData(new DataPoint(start.getTime(), trimpToDate.get(start)), false, 365);
        }

        banisterSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis((long) dataPoint.getX());
                String x = sdf.format(cal.getTime());
                String y = String.valueOf(dataPoint.getY());
                Toast.makeText(Statistics.getInstance(), "Banister model: On Data Point clicked: ("+ x + "," + y + ")", Toast.LENGTH_SHORT).show();
            }
        });

        trimpSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis((long) dataPoint.getX());
                String x = sdf.format(cal.getTime());
                String y = String.valueOf(dataPoint.getY());
                Toast.makeText(Statistics.getInstance(), "TRIMP graph: On Data Point clicked: ("+ x + "," + y + ")", Toast.LENGTH_SHORT).show();
            }
        });

        banisterGraph.removeAllSeries();
        trimpsGraph.removeAllSeries();
        banisterGraph.addSeries(banisterSeries);
        trimpsGraph.addSeries(trimpSeries);
        banisterGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, sdf));
        trimpsGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, sdf));
        banisterGraph.getGridLabelRenderer().setNumHorizontalLabels(count < 7 ? count : 7);
        trimpsGraph.getGridLabelRenderer().setNumHorizontalLabels(count < 7 ? count : 7);
        banisterGraph.getViewport().setMinX(from.getTime());
        banisterGraph.getViewport().setMaxX(to.getTime());
        trimpsGraph.getViewport().setMinX(from.getTime());
        trimpsGraph.getViewport().setMaxX(to.getTime());
    }
}
