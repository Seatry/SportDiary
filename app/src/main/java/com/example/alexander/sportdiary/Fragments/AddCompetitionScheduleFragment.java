package com.example.alexander.sportdiary.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.sportdiary.CollectionContracts.Edit;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.Utils.DateUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.alexander.sportdiary.CollectionContracts.Collections.COMPETITIONS;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.DAYS;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.DAY_COMPETITIONS;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.DIARIES;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.IMPORTANCES;
import static com.example.alexander.sportdiary.CollectionContracts.DayCompetition.COMPETITION_ID;
import static com.example.alexander.sportdiary.CollectionContracts.DayCompetition.IMPORTANCE_ID;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.START;
import static com.example.alexander.sportdiary.CollectionContracts.Edit.NAME;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;
import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolSpinner;

public class AddCompetitionScheduleFragment extends DialogFragment implements View.OnClickListener {
    private EditOption option;
    private String title;
    private FirebaseFirestore db;
    private EditText editDate;
    private Spinner nameSpinner;
    private Spinner importanceSpinner;
    private Pair<DocumentSnapshot, Date> updateItem;
    private Date maxDate;
    private Date minDate;
    private DocumentReference diary;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_competition_schedule, container, false);
        v.findViewById(R.id.cancelAddCompetition).setOnClickListener(this);
        v.findViewById(R.id.okAddCompetition).setOnClickListener(this);
        ((TextView)v.findViewById(R.id.add_competition_title)).setText(title);

        editDate = v.findViewById(R.id.editCompetitionDate);
        nameSpinner = v.findViewById(R.id.competitionNameSpinner);
        importanceSpinner = v.findViewById(R.id.competitionImportanceSpinner);

        db = MainActivity.getInstance().getDb();
        diary = db.collection(DIARIES).document(MainActivity.getSeasonPlanId());

        diary.get().addOnSuccessListener(documentSnapshot -> {
            minDate = documentSnapshot.getDate(START);
            maxDate = DateUtil.addDays(minDate, 365);

            if (option == EditOption.UPDATE) {
                editDate.setText(sdf.format(updateItem.second));
                String competitionId = updateItem.first.getString(COMPETITION_ID);
                db.collection(COMPETITIONS).document(competitionId).get(Source.CACHE).addOnSuccessListener(documentSnapshot1 -> {
                    String competition = documentSnapshot1.getString(NAME);
                    Object importanceIdObj = updateItem.first.get(IMPORTANCE_ID);
                    String importanceId = importanceIdObj == null ? "" : importanceIdObj.toString();
                    db.collection(IMPORTANCES).document(importanceId).get(Source.CACHE).addOnSuccessListener(documentSnapshot2 -> {
                        String importance = documentSnapshot2.exists() ? documentSnapshot2.getString(NAME) : null;
                        toolSpinner(COMPETITIONS, nameSpinner, new Edit(competitionId, competition));
                        toolSpinner(IMPORTANCES, importanceSpinner, importance == null ? null : new Edit(importanceId, importance));
                    });
                });
            } else {
                toolSpinner(COMPETITIONS, nameSpinner, null);
                toolSpinner(IMPORTANCES, importanceSpinner, null);
            }

            editDate.setOnClickListener(v1 -> {
                DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
                    month += 1;
                    String currentDate = dayOfMonth + "." + (month < 10 ? "0" + month : month) + "." + year;
                    editDate.setText(currentDate);
                };
                DatePickerDialog datePickerDialog;
                if (option == EditOption.UPDATE) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(updateItem.second);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = new DatePickerDialog(MainActivity.getInstance(),
                            listener, year, month, day);
                } else {
                    datePickerDialog = new DatePickerDialog(MainActivity.getInstance());
                    datePickerDialog.setOnDateSetListener(listener);
                }
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                datePickerDialog.show();
            });
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelAddCompetition:
                dismiss();
                break;
            case R.id.okAddCompetition:
                handleChange();
        }
    }

    public void handleChange() {
        String competitionId = nameSpinner.getSelectedItem() == null ? null : ((Edit) nameSpinner.getSelectedItem()).getId();
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
        if (date.after(maxDate) || date.before(minDate)) {
            Toast.makeText(MainActivity.getInstance(), R.string.date_in_season, Toast.LENGTH_SHORT).show();
            return;
        }
        String importanceId = importanceSpinner.getSelectedItem() == null ? null : ((Edit) importanceSpinner.getSelectedItem()).getId();
        int day = DateUtil.differenceInDays(minDate, date);
        Map<String, Object> data = new HashMap<>();
        data.put(COMPETITION_ID, competitionId);
        if (importanceId != null) {
            data.put(IMPORTANCE_ID, importanceId);
        }
        if (option == EditOption.UPDATE) {
            updateItem.first.getReference().update(data);
        } else {
            diary.collection(DAYS).document(String.valueOf(day)).collection(DAY_COMPETITIONS).add(data);
        }
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

    public void setUpdateItem(Pair<DocumentSnapshot, Date> updateItem) {
        this.updateItem = updateItem;
    }
}
