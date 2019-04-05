package com.example.alexander.sportdiary.Fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.sportdiary.EditOption;
import com.example.alexander.sportdiary.Entities.CompetitionToImportance;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;
import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolSpinner;

public class AddCompetitionScheduleFragment extends DialogFragment implements View.OnClickListener {
    private EditOption option;
    private String title;
    private SportDataBase sportDataBase;
    private EditText editDate;
    private Spinner nameSpinner;
    private Spinner importanceSpinner;
    private Day updateDay = new Day();
    private long seasonPlanId;
    private SeasonPlan seasonPlan;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_competition_schedule, container, false);
        v.findViewById(R.id.cancelAddCompetition).setOnClickListener(this);
        v.findViewById(R.id.okAddCompetition).setOnClickListener(this);
        ((TextView)v.findViewById(R.id.add_competition_title)).setText(title);

        sportDataBase = MainActivity.getInstance().getDatabase();

        editDate = v.findViewById(R.id.editCompetitionDate);
        nameSpinner = v.findViewById(R.id.competitionNameSpinner);
        importanceSpinner = v.findViewById(R.id.competitionImportanceSpinner);

        seasonPlan = sportDataBase.seasonPlanDao().getSeasonPlanById(seasonPlanId);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String currentDate = dayOfMonth + "." + (month < 10 ? "0" + month : month) + "." + year;
                        editDate.setText(currentDate);
                    }
                };
                DatePickerDialog datePickerDialog;
                if (option == EditOption.UPDATE) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(updateDay.getDate());
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = new DatePickerDialog(MainActivity.getInstance(),
                            listener, year, month, day);
                } else {
                    datePickerDialog = new DatePickerDialog(MainActivity.getInstance());
                    datePickerDialog.setOnDateSetListener(listener);
                }
                datePickerDialog.getDatePicker().setMaxDate(DateUtil.addDays(seasonPlan.getStart(), 365).getTime());
                datePickerDialog.getDatePicker().setMinDate(seasonPlan.getStart().getTime());
                datePickerDialog.show();
            }
        });

        Long competitionToImportanceId = updateDay.getCompetitionToImportanceId();
        CompetitionToImportance competitionToImportance = sportDataBase.competitionToImportanceDao().getById(competitionToImportanceId);
        Long competitionId = competitionToImportance == null ? null : competitionToImportance.getCompetitionId();
        Long importanceId = competitionToImportance == null ? null : competitionToImportance.getImportanceId();
        toolSpinner(sportDataBase.competitionDao(), nameSpinner, sportDataBase.competitionDao().getNameById(competitionId));
        toolSpinner(sportDataBase.importanceDao(), importanceSpinner, sportDataBase.importanceDao().getNameById(importanceId));

        if (option == EditOption.UPDATE) {
            editDate.setText(sdf.format(updateDay.getDate()));
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelAddCompetition:
                dismiss();
                break;
            case R.id.okAddCompetition:
                switch (option) {
                    case INSERT:
                        add();
                        break;
                    case UPDATE:
                        update();
                        break;
                }
        }
    }

    public void add() {
        Long competitionId = nameSpinner.getSelectedItem() == null ? null
                : sportDataBase.competitionDao().getIdByName(nameSpinner.getSelectedItem().toString());
        if (competitionId == null) {
            Toast.makeText(MainActivity.getInstance(), R.string.competition_error, Toast.LENGTH_SHORT).show();
            return;
        }
        Date date;
        try {
            date = sdf.parse(editDate.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(MainActivity.getInstance(), R.string.date_format_err, Toast.LENGTH_SHORT).show();
            return;
        }
        Long importanceId = importanceSpinner.getSelectedItem() == null ? null
                : sportDataBase.importanceDao().getIdByName(importanceSpinner.getSelectedItem().toString());
        Day existDay = sportDataBase.dayDao().getDayByDateAndSeasonIdWhereCompetitionToImportanceNotNull(date, seasonPlanId);
        if (existDay != null) {
            this.setUpdateDay(existDay);
            update();
            return;
        }
        CompetitionToImportance competitionToImportance = new CompetitionToImportance(competitionId, importanceId);
        Long id = sportDataBase.competitionToImportanceDao().insert(competitionToImportance);
        sportDataBase.dayDao().updateCompetitionToImportanceByDateAndSeasonId(id, date, seasonPlanId);
        dismiss();
    }

    public void update() {
        Long competitionId = nameSpinner.getSelectedItem() == null ? null
                : sportDataBase.competitionDao().getIdByName(nameSpinner.getSelectedItem().toString());
        if (competitionId == null) {
            Toast.makeText(MainActivity.getInstance(), R.string.competition_error, Toast.LENGTH_SHORT).show();
            return;
        }
        Date date;
        try {
            date = sdf.parse(editDate.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(MainActivity.getInstance(), R.string.date_format_err, Toast.LENGTH_SHORT).show();
            return;
        }
        Long importanceId = importanceSpinner.getSelectedItem() == null ? null
                : sportDataBase.importanceDao().getIdByName(importanceSpinner.getSelectedItem().toString());
        Long competitionToImportanceId = sportDataBase.dayDao()
                .getCompetitionToImportanceIdByDateAndSeasonId(updateDay.getDate(), updateDay.getSeasonPlanId());
        sportDataBase.competitionToImportanceDao().deleteById(competitionToImportanceId);
        CompetitionToImportance competitionToImportance = new CompetitionToImportance(competitionId, importanceId);
        Long id = sportDataBase.competitionToImportanceDao().insert(competitionToImportance);
        sportDataBase.dayDao().updateCompetitionToImportanceByDateAndSeasonId(id, date, updateDay.getSeasonPlanId());
        dismiss();
    }

    public AddCompetitionScheduleFragment setOption(EditOption option) {
        this.option = option;
        return this;
    }

    public AddCompetitionScheduleFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public AddCompetitionScheduleFragment setSeasonPlanId(long seasonPlanId) {
        this.seasonPlanId = seasonPlanId;
        return this;
    }

    public void setUpdateDay(Day updateDay) {
        this.updateDay = updateDay;
    }
}
