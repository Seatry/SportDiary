package com.example.alexander.sportdiary.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.Utils.DateUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.alexander.sportdiary.CollectionContracts.Collections.DAYS;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.DIARIES;
import static com.example.alexander.sportdiary.CollectionContracts.Day.DATE;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.HR_MAX;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.HR_REST;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.LAST_PERFORMANCE;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.MALE;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.NAME;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.START;
import static com.example.alexander.sportdiary.CollectionContracts.Diary.USER_ID;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class UpdateDiaryFragment extends DialogFragment implements View.OnClickListener {
    private EditText editNameText;
    private EditText editStartText;
    private Spinner maleSpinner;
    private EditText editHrMax;
    private EditText editHrRest;
    private EditText editPerformance;
    private String id;
    private DocumentReference diary;
    private FirebaseFirestore db;
    private String start;

    public void setItemId(String id) {
        this.id = id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_diary, container, false);
        v.findViewById(R.id.cancelUpdateDiary).setOnClickListener(this);
        v.findViewById(R.id.okUpdateDiary).setOnClickListener(this);
        v.findViewById(R.id.removeDiary).setOnClickListener(this);
        editNameText = v.findViewById(R.id.update_diary_name);
        editStartText = v.findViewById(R.id.update_diary_start);
        editStartText.setOnClickListener(v1 -> {
            final Calendar cal = Calendar.getInstance();
            diary.get().addOnSuccessListener(documentSnapshot -> cal.setTime(documentSnapshot.getTimestamp(START).toDate()));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.getInstance(),
                    (view, year1, month1, dayOfMonth) -> {
                        month1 +=1;
                        String currentDate = dayOfMonth + "." + (month1 < 10 ? "0" + month1 : month1) + "." + year1;
                        editStartText.setText(currentDate);
                    }, year, month, day
            );
            datePickerDialog.show();
        });
        maleSpinner = v.findViewById(R.id.update_maleSpinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getInstance(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(getString(R.string.male), getString(R.string.female));
        adapter.notifyDataSetChanged();
        maleSpinner.setAdapter(adapter);
        editHrMax = v.findViewById(R.id.update_hrMax);
        editHrRest = v.findViewById(R.id.update_hrRest);
        editPerformance = v.findViewById(R.id.update_performance);

        db =  MainActivity.getInstance().getDb();
        diary = db.collection(DIARIES).document(id);

        diary.get(Source.CACHE).addOnSuccessListener(documentSnapshot -> {
            Log.d("success", "diary update fragment");
            if (documentSnapshot == null) return;
            editNameText.setText(documentSnapshot.get(NAME).toString());
            start = sdf.format(documentSnapshot.getTimestamp(START).toDate());
            editStartText.setText(start);
            maleSpinner.setSelection(documentSnapshot.get(MALE).toString().equals(MainActivity.getInstance().getString(R.string.male)) ? 0 : 1);
            editHrMax.setText(documentSnapshot.get(HR_MAX).toString());
            editHrRest.setText(documentSnapshot.get(HR_REST).toString());
            editPerformance.setText(documentSnapshot.get(LAST_PERFORMANCE).toString());
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelUpdateDiary:
                dismiss();
                break;
            case R.id.okUpdateDiary:
                update();
                break;
            case R.id.removeDiary:
                remove();
        }
    }

    public void update() {
        String name = editNameText.getText().toString();
        Map<String, Object> diaryMap = new HashMap<>();
        diaryMap.put(USER_ID, MainActivity.getUserId());
        diaryMap.put(NAME, editNameText.getText().toString());
        diaryMap.put(HR_MAX, editHrMax.getText().length() > 0 ? Integer.parseInt(editHrMax.getText().toString()) : 200);
        diaryMap.put(HR_REST, editHrRest.getText().length() > 0 ? Integer.parseInt(editHrRest.getText().toString()) : 60);
        diaryMap.put(LAST_PERFORMANCE, editPerformance.getText().length() > 0 ? Integer.parseInt(editPerformance.getText().toString()) : 0);
        diaryMap.put(MALE, maleSpinner.getSelectedItem() != null ? maleSpinner.getSelectedItem().toString() : "М");
        try {
            final String dateText = editStartText.getText().toString();
            final Date date = sdf.parse(dateText);
            diaryMap.put(START, date);

            diary.update(diaryMap).addOnSuccessListener(aVoid -> {
                if (start.equals(dateText)) return;
                db.runBatch(writeBatch -> {
                    for (int i = 0; i < 366; i++) {
                        writeBatch.update(db.collection(DIARIES)
                                .document(id)
                                .collection(DAYS)
                                .document(String.valueOf(i)), DATE, DateUtil.addDays(date, i));
                    }
                });
            });

            if (MainActivity.getSeasonPlanId() != null && MainActivity.getSeasonPlanId().equals(id)) {
                DayFragment dayFragment = new DayFragment();
//                dayFragment.setSeasonPlanId(id);
                MainActivity.getInstance().setTitle("(" + name.charAt(0) + ")");
                MainActivity.getInstance().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_frame, dayFragment)
                        .commit();
                MainActivity.getInstance().setDayFragment(dayFragment);
            }

            dismiss();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.date_format_err, Toast.LENGTH_SHORT).show();
        }
    }

    public void remove() {
        Log.d("delete", "/" + DIARIES + "/" + diary.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("path", "/" + DIARIES + "/" + diary.getId());
        MainActivity.getInstance().getRecursiveDeleteFunction().call(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("delete", "Diary deleted");
                    }
                });
        this.diary.delete();
        dismiss();
    }

}
