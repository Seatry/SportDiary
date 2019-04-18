package com.example.alexander.sportdiary.Fragments;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.Utils.DateUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.*;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.*;
import static com.example.alexander.sportdiary.CollectionContracts.Day.*;

public class AddNewDiaryFragment extends DialogFragment implements View.OnClickListener {
    private EditText editNameText;
    private EditText editStartText;
    private Spinner maleSpinner;
    private EditText editHrMax;
    private EditText editHrRest;
    private EditText editPerformance;
    private FirebaseFirestore db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_diary, container, false);
        v.findViewById(R.id.cancelAddDiary).setOnClickListener(this);
        v.findViewById(R.id.okAddDiary).setOnClickListener(this);
        editNameText = v.findViewById(R.id.add_diary_name);
        editStartText = v.findViewById(R.id.add_diary_start);
        editStartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.getInstance());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month +=1;
                        String currentDate = dayOfMonth + "." + (month < 10 ? "0" + month : month) + "." + year;
                        editStartText.setText(currentDate);
                    }
                });
                datePickerDialog.show();
            }
        });
        maleSpinner = v.findViewById(R.id.maleSpinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getInstance(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(getString(R.string.male), getString(R.string.female));
        adapter.notifyDataSetChanged();
        maleSpinner.setAdapter(adapter);
        editHrMax = v.findViewById(R.id.hrMax);
        editHrRest = v.findViewById(R.id.hrRest);
        editPerformance = v.findViewById(R.id.performance);

        db =  MainActivity.getInstance().getDb();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelAddDiary:
                dismiss();
                break;
            case R.id.okAddDiary:
                add();
                break;
        }
    }

    public void add()  {
        Map<String, Object> diary = new HashMap<>();
        diary.put(USER_ID, MainActivity.getUserId());
        diary.put(NAME, editNameText.getText().toString());
        diary.put(HR_MAX, editHrMax.getText().length() > 0 ? Integer.parseInt(editHrMax.getText().toString()) : 200);
        diary.put(HR_REST, editHrRest.getText().length() > 0 ? Integer.parseInt(editHrRest.getText().toString()) : 60);
        diary.put(LAST_PERFORMANCE, editPerformance.getText().length() > 0 ? Integer.parseInt(editPerformance.getText().toString()) : 0);
        diary.put(MALE, maleSpinner.getSelectedItem() != null ? maleSpinner.getSelectedItem().toString() : getString(R.string.male));
        try {
            String dateText = editStartText.getText().toString();
            final Date date = sdf.parse(dateText);
            diary.put(START, date);
            db.collection(DIARIES)
                    .add(diary)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            final String id = documentReference.getId();
                            final Map<String, Object> day = new HashMap<>();
                            day.put(CAPACITY, 0);
                            day.put(HEALTH, 0);
                            day.put(MOOD, 0);
                            day.put(ACTIVITY, 0);
                            day.put(DREAM, 0);
                            db.runBatch(new WriteBatch.Function() {
                                @Override
                                public void apply(@NonNull WriteBatch writeBatch) {
                                    for (int i = 0; i < 366; i++) {
                                        day.put(DATE, DateUtil.addDays(date, i));
                                        writeBatch.set(db.collection(DIARIES).document(id)
                                                .collection(DAYS).document(String.valueOf(i)), day);
                                    }
                                }
                            });
                        }
                    });

            dismiss();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.unique_err, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.date_format_err, Toast.LENGTH_SHORT).show();

        }
    }

}