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
import android.widget.Toast;

import com.example.alexander.sportdiary.EditOption;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

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
    }
}
